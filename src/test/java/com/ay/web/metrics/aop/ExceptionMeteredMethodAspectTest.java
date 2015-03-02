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
import com.ay.web.metrics.aop.ExceptionMeteredMethodAspect;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.annotation.ExceptionMetered;

/**
 * ExceptionMeteredMethodAspect Test class
 * 
 * @author Youssef Arfaoui
 *
 *         Feb 16, 2015
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ExceptionMeteredMethodAspectTest extends AbstractMethodTest<ExceptionMetered, Meter> {
	@Mock
	ExceptionMeteredMethodAspect exceptionMeteredMethodAspect;
	@Mock
	ExceptionMetered annotation;
	@Mock
	ProceedingJoinPoint pjp;
	@Mock
	MethodSignature signature;
	@Mock
	AnnotationMetricPair<ExceptionMetered, Meter> pair;
	MetricRegistry metricRegistry = new MetricRegistry();
	Meter meter;

	@Before
	public void setUp() throws Throwable {
		Mockito.when(exceptionMeteredMethodAspect.getAnnotationClass()).thenReturn(getAnnotationClass());
		Mockito.when(exceptionMeteredMethodAspect.getMetricRegistry()).thenReturn(metricRegistry);
		Mockito.when(annotation.absolute()).thenReturn(true);
		Mockito.when(annotation.name()).thenReturn(getMetricName());
		meter = metricRegistry.meter(getMetricName());
		MeterClass value = new MeterClass();
		Mockito.when(pjp.getTarget()).thenReturn(value);
		Mockito.when(pjp.getSignature()).thenReturn(signature);
		Mockito.when(signature.getMethod()).thenReturn(
				ReflectionUtils.findMethod(targetClass, getAnnotatedMethodName()));
		Mockito.when(pair.getMeter()).thenReturn(meter);
		Method method = ReflectionUtils.findMethod(targetClass, getAnnotatedMethodName());
		ExceptionMetered ann = method.getAnnotation(getAnnotationClass());
		Mockito.when(pair.getAnnotation()).thenReturn(ann);

	}

	@Test
	public void testInvokeProceedingJoinPointAnnotationMetricPairOfExceptionMeteredMeter() {

		try {
			Mockito.when(pjp.proceed()).thenThrow(new RuntimeException("Test"));
			Mockito.when(exceptionMeteredMethodAspect.invoke(pjp, pair)).thenCallRealMethod();
			exceptionMeteredMethodAspect.invoke(pjp, pair);
		} catch (Throwable e) {
		}
		Assertions.assertThat(metricRegistry.meter(getMetricName()).getCount()).isEqualTo(1);

	}

	@Test
	public void testBuildMetricNameClassOfQMethodExceptionMetered() {
		Method method = ReflectionUtils.findMethod(targetClass, getAnnotatedMethodName());

		Mockito.when(exceptionMeteredMethodAspect.buildMetricName(targetClass, method, annotation))
				.thenCallRealMethod();
		String expected = exceptionMeteredMethodAspect.buildMetricName(targetClass, method, annotation);
		Assertions.assertThat(getMetricName()).isEqualTo(expected);
	}

	@Test
	public void testBuildMetricMetricRegistryStringExceptionMetered() {
		Mockito.when(exceptionMeteredMethodAspect.buildMetric(metricRegistry, getMetricName(), annotation))
				.thenCallRealMethod();
		Meter expected = exceptionMeteredMethodAspect.buildMetric(metricRegistry, getMetricName(), annotation);
		Assertions.assertThat(meter).isEqualTo(expected);
	}

	@Test
	public void testGetAnnotationClass() {
		Assertions.assertThat(exceptionMeteredMethodAspect.getAnnotationClass()).isEqualTo(getAnnotationClass());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<ExceptionMetered> getAnnotationClass() {
		return ExceptionMetered.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMetricName() {
		return "test.method.exceptionMetered";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAnnotatedMethodName() {
		return "exceptionMeteredMethod";
	}
}
