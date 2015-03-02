/**
 * 
 */
package com.ay.web.metrics.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;

import com.ay.web.metrics.AnnotationMetricPair;
import com.ay.web.metrics.MetricsUtils;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.Timer.Context;
import com.codahale.metrics.annotation.Timed;

/**
 * 
 * Aspect to handle all {@link Timer} aspects
 * 
 * @author Youssef Arfaoui
 *
 *         Feb 13, 2015
 *
 * @param <Timed>
 *            The {@link Timed} annotation associated to the {@link Timer} class
 * @param <Timer>
 *            The {@link Timer} class
 */
@Aspect
public class TimedMethodAspect extends AbstractMetricMethodAspect<Timed, Timer> implements Ordered {
	private static final Logger LOG = LoggerFactory.getLogger(TimedMethodAspect.class);

	public TimedMethodAspect() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object invoke(ProceedingJoinPoint pjp, AnnotationMetricPair<Timed, Timer> pair) throws Throwable {
		LOG.debug("Start - invoke(...)");
		final Context timerCtx = pair.getMeter().time();
		try {
			return pjp.proceed();
		} finally {
			timerCtx.close();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String buildMetricName(Class<?> targetClass, Method method, Timed annotation) {
		return MetricsUtils.forTimedMethod(targetClass, method, annotation);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Timer buildMetric(MetricRegistry metricRegistry, String metricName, Timed annotation) {

		return metricRegistry.timer(metricName);
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
	public int getOrder() {
		return HIGHEST_PRECEDENCE;
	}

}
