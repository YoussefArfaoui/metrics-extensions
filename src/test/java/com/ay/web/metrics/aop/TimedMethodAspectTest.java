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
import com.ay.web.metrics.aop.TimedMethodAspect;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.annotation.Timed;

/**
 * 
 * TimedMethodAspect Test class
 * 
 * @author Youssef Arfaoui
 *
 *         Feb 17, 2015
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class TimedMethodAspectTest extends AbstractMethodTest<Timed, Timer> {
	@Mock
	TimedMethodAspect timedMethodAspect;
	@Mock
	Timed annotation;
	@Mock
	ProceedingJoinPoint pjp;
	@Mock
	MethodSignature signature;
	@Mock
	AnnotationMetricPair<Timed, Timer> pair;
	MetricRegistry metricRegistry = new MetricRegistry();
	Timer timer;

	@Before
	public void setUp() throws Exception {
		Mockito.when(timedMethodAspect.getAnnotationClass()).thenReturn(getAnnotationClass());
		Mockito.when(timedMethodAspect.getMetricRegistry()).thenReturn(metricRegistry);
		Mockito.when(annotation.absolute()).thenReturn(true);
		Mockito.when(annotation.name()).thenReturn(getMetricName());
		timer = metricRegistry.timer(getMetricName());
		Mockito.when(pjp.getTarget()).thenReturn(new MeterClass());
		Mockito.when(pjp.getSignature()).thenReturn(signature);
		Mockito.when(signature.getMethod()).thenReturn(
				ReflectionUtils.findMethod(targetClass, getAnnotatedMethodName()));
		Mockito.when(pair.getMeter()).thenReturn(timer);

	}

	@Test
	public void testInvokeProceedingJoinPointAnnotationMetricPairOfTimedTimer() throws Throwable {

		Mockito.when(timedMethodAspect.invoke(pjp, pair)).thenCallRealMethod();
		timedMethodAspect.invoke(pjp, pair);
		Assertions.assertThat(metricRegistry.timer(getMetricName()).getCount()).isEqualTo(1);

	}

	@Test
	public void testBuildMetricNameClassOfQMethodTimed() {
		Method method = ReflectionUtils.findMethod(targetClass, getAnnotatedMethodName());

		Mockito.when(timedMethodAspect.buildMetricName(targetClass, method, annotation)).thenCallRealMethod();
		String expected = timedMethodAspect.buildMetricName(targetClass, method, annotation);
		Assertions.assertThat(getMetricName()).isEqualTo(expected);
	}

	@Test
	public void testBuildMetricMetricRegistryStringTimed() {
		Mockito.when(timedMethodAspect.buildMetric(metricRegistry, getMetricName(), annotation)).thenCallRealMethod();
		Timer expected = timedMethodAspect.buildMetric(metricRegistry, getMetricName(), annotation);
		Assertions.assertThat(timer).isEqualTo(expected);
	}

	@Test
	public void testGetAnnotationClass() {
		Assertions.assertThat(timedMethodAspect.getAnnotationClass()).isEqualTo(getAnnotationClass());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<Timed> getAnnotationClass() {
		return Timed.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMetricName() {
		return "test.method.timed";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAnnotatedMethodName() {
		return "timedMethod";
	}
}
