/**
 * 
 */
package com.ay.web.metrics.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.util.ReflectionUtils;

import com.ay.web.metrics.AnnotationMetricPair;
import com.ay.web.metrics.MeterClass;
import com.ay.web.metrics.aop.MeteredMethodAspect;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.annotation.Metered;

/**
 * MeteredMethodAspect Test class
 * 
 * @author Youssef Arfaoui
 *
 *         Feb 16, 2015
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class MeteredMethodAspectTest extends AbstractMethodTest<Metered, Meter> {
	@Mock
	MeteredMethodAspect meteredMethodAspect;
	@Mock
	Metered annotation;
	@Mock
	ProceedingJoinPoint pjp;
	@Mock
	MethodSignature signature;
	@Mock
	AnnotationMetricPair<Metered, Meter> pair;
	MetricRegistry metricRegistry = new MetricRegistry();
	Meter meter;

	@Before
	public void setUp() throws Exception {
		Mockito.when(meteredMethodAspect.getAnnotationClass()).thenReturn(getAnnotationClass());
		Mockito.when(meteredMethodAspect.getMetricRegistry()).thenReturn(metricRegistry);
		Mockito.when(annotation.absolute()).thenReturn(true);
		Mockito.when(annotation.name()).thenReturn(getMetricName());
		meter = metricRegistry.meter(getMetricName());
		Mockito.when(pjp.getTarget()).thenReturn(new MeterClass());
		Mockito.when(pjp.getSignature()).thenReturn(signature);
		Mockito.when(signature.getMethod()).thenReturn(
				ReflectionUtils.findMethod(targetClass, getAnnotatedMethodName()));
		Mockito.when(pair.getMeter()).thenReturn(meter);

	}

	@Test
	public void testInvokeProceedingJoinPointAnnotationMetricPairOfTimedTimer() throws Throwable {

		Mockito.when(meteredMethodAspect.invoke(pjp, pair)).thenCallRealMethod();
		meteredMethodAspect.invoke(pjp, pair);
		Assertions.assertThat(metricRegistry.meter(getMetricName()).getCount()).isEqualTo(1);

	}

	@Test
	public void testBuildMetricNameClassOfQMethodTimed() {
		Method method = ReflectionUtils.findMethod(targetClass, getAnnotatedMethodName());

		Mockito.when(meteredMethodAspect.buildMetricName(targetClass, method, annotation)).thenCallRealMethod();
		String expected = meteredMethodAspect.buildMetricName(targetClass, method, annotation);
		Assertions.assertThat(getMetricName()).isEqualTo(expected);
	}

	@Test
	public void testBuildMetricMetricRegistryStringTimed() {
		Mockito.when(meteredMethodAspect.buildMetric(metricRegistry, getMetricName(), annotation)).thenCallRealMethod();
		Meter expected = meteredMethodAspect.buildMetric(metricRegistry, getMetricName(), annotation);
		Assertions.assertThat(meter).isEqualTo(expected);
	}

	@Test
	public void testGetAnnotationClass() {
		Assertions.assertThat(meteredMethodAspect.getAnnotationClass()).isEqualTo(getAnnotationClass());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<Metered> getAnnotationClass() {
		return Metered.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMetricName() {
		return "test.method.metered";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAnnotatedMethodName() {
		return "meteredMethod";
	}
}