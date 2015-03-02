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
import com.codahale.metrics.annotation.Metered;

/**
 * 
 * Aspect to handle all {@link Meter} aspects
 * 
 * @author Youssef Arfaoui
 *
 *         Feb 13, 2015
 *
 * @param <Metered>
 *            The {@link Metered} annotation associated to the {@link Meter}
 *            class
 * @param <Timer>
 *            The {@link Meter} class
 */
@Aspect
public class MeteredMethodAspect extends AbstractMetricMethodAspect<Metered, Meter> implements Ordered {
	private static final Logger LOG = LoggerFactory.getLogger(TimedMethodAspect.class);

	public MeteredMethodAspect() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object invoke(ProceedingJoinPoint pjp, AnnotationMetricPair<Metered, Meter> pair) throws Throwable {
		LOG.debug("Start - invoke(...)");
		pair.getMeter().mark();
		return pjp.proceed();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String buildMetricName(Class<?> targetClass, Method method, Metered annotation) {

		return MetricsUtils.forMeteredMethod(targetClass, method, annotation);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Meter buildMetric(MetricRegistry metricRegistry, String metricName, Metered annotation) {
		return metricRegistry.meter(metricName);
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
	public int getOrder() {
		return HIGHEST_PRECEDENCE;
	}
}
