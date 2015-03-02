/**
 * 
 */
package com.ay.web.metrics.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ay.web.metrics.AnnotationMetricPair;
import com.ay.web.metrics.MetricsUtils;
import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.ryantenney.metrics.annotation.Counted;

/**
 * 
 * Aspect to handle all {@link Counter} aspects
 * 
 * @author Youssef Arfaoui
 *
 *         Feb 13, 2015
 *
 * @param <Counted>
 *            The {@link Counted} annotation associated to the {@link Counter}
 *            class
 * @param <Timer>
 *            The {@link Counter} class
 */
@Aspect
public class CountedMethodAspect extends AbstractMetricMethodAspect<Counted, Counter> {

	private static final Logger LOG = LoggerFactory.getLogger(TimedMethodAspect.class);

	/**
	 * 
	 */
	public CountedMethodAspect() {
		super();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object invoke(ProceedingJoinPoint pjp, AnnotationMetricPair<Counted, Counter> pair) throws Throwable {
		LOG.debug("Start - invoke(...)");
		try {
			pair.getMeter().inc();
			return pjp.proceed();
		} finally {
			if (!pair.getAnnotation().monotonic()) {
				pair.getMeter().dec();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String buildMetricName(Class<?> targetClass, Method method, Counted annotation) {
		return MetricsUtils.forCountedMethod(targetClass, method, annotation);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Counter buildMetric(MetricRegistry metricRegistry, String metricName, Counted annotation) {
		return metricRegistry.counter(metricName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<Counted> getAnnotationClass() {

		return Counted.class;
	}

}
