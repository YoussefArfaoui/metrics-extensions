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
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.annotation.ExceptionMetered;

/**
 * 
 * Aspect to handle all {@link Meter} aspects
 * 
 * @author Youssef Arfaoui
 *
 *         Feb 13, 2015
 *
 * @param <ExceptionMetered>
 *            The {@link ExceptionMetered} annotation associated to the
 *            {@link Meter} class
 * @param <Timer>
 *            The {@link Meter} class
 */
@Aspect
public class ExceptionMeteredMethodAspect extends AbstractMetricMethodAspect<ExceptionMetered, Meter> implements
		Ordered {
	private static final Logger LOG = LoggerFactory.getLogger(TimedMethodAspect.class);

	public ExceptionMeteredMethodAspect() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object invoke(ProceedingJoinPoint pjp, AnnotationMetricPair<ExceptionMetered, Meter> pair)
			throws Throwable {
		LOG.debug("Start - invoke(...)");
		try {
			return pjp.proceed();
		} catch (Throwable t) {
			if (pair.getAnnotation().cause().isAssignableFrom(t.getClass())) {
				pair.getMeter().mark();
			}
			throw t;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String buildMetricName(Class<?> targetClass, Method method, ExceptionMetered annotation) {
		return MetricsUtils.forExceptionMeteredMethod(targetClass, method, annotation);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Meter buildMetric(MetricRegistry metricRegistry, String metricName, ExceptionMetered annotation) {

		return metricRegistry.meter(metricName);
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
	public int getOrder() {
		return HIGHEST_PRECEDENCE;
	}
}
