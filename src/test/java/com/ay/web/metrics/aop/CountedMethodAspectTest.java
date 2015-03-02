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
import com.ay.web.metrics.aop.CountedMethodAspect;
import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.ryantenney.metrics.annotation.Counted;

/**
 * CountedMethodAspect Test class
 * 
 * @author Youssef Arfaoui
 *
 *         Feb 16, 2015
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CountedMethodAspectTest extends AbstractMethodTest<Counted, Counter> {
	@Mock
	CountedMethodAspect countedMethodAspect;
	@Mock
	Counted annotation;
	@Mock
	ProceedingJoinPoint pjp;
	@Mock
	MethodSignature signature;
	@Mock
	AnnotationMetricPair<Counted, Counter> pair;
	MetricRegistry metricRegistry = new MetricRegistry();
	Counter counter;

	@Before
	public void setUp() throws Exception {
		Mockito.when(countedMethodAspect.getAnnotationClass()).thenReturn(getAnnotationClass());
		Mockito.when(countedMethodAspect.getMetricRegistry()).thenReturn(metricRegistry);
		Mockito.when(annotation.absolute()).thenReturn(true);
		Mockito.when(annotation.name()).thenReturn(getMetricName());

		counter = metricRegistry.counter(getMetricName());
		Mockito.when(pjp.getTarget()).thenReturn(new MeterClass());
		Mockito.when(pjp.getSignature()).thenReturn(signature);
		Mockito.when(signature.getMethod()).thenReturn(
				ReflectionUtils.findMethod(targetClass, getAnnotatedMethodName()));
		Mockito.when(pair.getMeter()).thenReturn(counter);
		Mockito.when(pair.getAnnotation()).thenReturn(annotation);
		Mockito.when(pair.getAnnotation().monotonic()).thenReturn(true);

	}

	@Test
	public void testInvokeProceedingJoinPointAnnotationMetricPairOfTimedTimer() throws Throwable {

		Mockito.when(countedMethodAspect.invoke(pjp, pair)).thenCallRealMethod();
		countedMethodAspect.invoke(pjp, pair);
		Assertions.assertThat(metricRegistry.counter(getMetricName()).getCount()).isEqualTo(1);

	}

	@Test
	public void testBuildMetricNameClassOfQMethodTimed() {
		Method method = ReflectionUtils.findMethod(targetClass, getAnnotatedMethodName());

		Mockito.when(countedMethodAspect.buildMetricName(targetClass, method, annotation)).thenCallRealMethod();
		String expected = countedMethodAspect.buildMetricName(targetClass, method, annotation);
		Assertions.assertThat(getMetricName()).isEqualTo(expected);
	}

	@Test
	public void testBuildMetricMetricRegistryStringTimed() {
		Mockito.when(countedMethodAspect.buildMetric(metricRegistry, getMetricName(), annotation)).thenCallRealMethod();
		Counter expected = countedMethodAspect.buildMetric(metricRegistry, getMetricName(), annotation);
		Assertions.assertThat(counter).isEqualTo(expected);
	}

	@Test
	public void testGetAnnotationClass() {
		Assertions.assertThat(countedMethodAspect.getAnnotationClass()).isEqualTo(getAnnotationClass());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<Counted> getAnnotationClass() {
		return Counted.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMetricName() {
		return "test.method.counted";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAnnotatedMethodName() {
		return "countedMethod";
	}
}