package com.ay.web.metrics.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import com.ay.web.metrics.AnnotationMetricPair;
import com.ay.web.metrics.MethodKey;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;

/**
 * 
 * Aspect <M> metrics aspects
 * 
 * @author Youssef Arfaoui
 *
 *         Feb 13, 2015
 *
 * @param <A>
 *            The annotation associated to the <M> meter class
 * @param <M>
 *            The meter class
 */
public abstract class AbstractMetricMethodAspect<A extends Annotation, M> {
	private static final Logger LOG = LoggerFactory.getLogger(AbstractMetricMethodAspect.class);

	private MetricRegistry metricRegistry;
	private HealthCheckRegistry healthCheckRegistry;
	private final Map<MethodKey, AnnotationMetricPair<A, M>> metrics;

	/**
	 * 
	 */
	public AbstractMetricMethodAspect() {
		this.metrics = new HashMap<MethodKey, AnnotationMetricPair<A, M>>();
	}

	/**
	 * Invoke the method and trigger the meter logic
	 * 
	 * @param pjp
	 *            the proceeding joinPoint
	 * @param pair
	 *            the annotation metric pair
	 * @return the result object
	 * @throws Throwable
	 */
	abstract protected Object invoke(ProceedingJoinPoint pjp, AnnotationMetricPair<A, M> pair) throws Throwable;

	/**
	 * Invoke the method and trigger the meter logic
	 * 
	 * @param pjp
	 *            the proceeding joinPoint
	 * @return the result object
	 * @throws Throwable
	 */
	@Around(" @annotation(com.codahale.metrics.annotation.ExceptionMetered) "
			+ "||@annotation(com.codahale.metrics.annotation.Gauge) "
			+ "||@annotation(com.codahale.metrics.annotation.Metered)"
			+ "||@annotation(com.codahale.metrics.annotation.Timed)"
			+ "||@annotation(com.ryantenney.metrics.annotation.CachedGauge)"
			+ "||@annotation(com.ryantenney.metrics.annotation.Counted)"
			+ "||@annotation(com.ryantenney.metrics.annotation.Metric)")
	public Object arroundMethod(ProceedingJoinPoint pjp) throws Throwable {
		try {
			LOG.debug("Start - arroundMethod(...)");
			AnnotationMetricPair<A, M> pair = getAnnotationMetricPair(pjp.getStaticPart().getSignature(),
					getAnnotationClass());
			if (pair != null) {
				return invoke(pjp, pair);
			} else {
				return pjp.proceed();
			}
		} catch (Exception ex) {
			LOG.error("Exception , re-throwing");
			throw ex;
		}
	}

	/**
	 * Return the metric annotation class
	 * 
	 * @return the annotation class
	 */
	abstract public Class<A> getAnnotationClass();

	/**
	 * It returns an annotationMetric pair from the method signature for an
	 * annotation class
	 * 
	 * @param signature
	 *            the method signature
	 * @return the annotationMetricPair<A, M>
	 */
	public AnnotationMetricPair<A, M> getAnnotationMetricPair(Signature signature, Class<A> annotationClass) {
		Class<?> targetClass = signature.getDeclaringType();

		String methodName = signature.getName();
		LOG.debug("handling {} metric for the {} {} method", new Object[] { annotationClass.getName(), targetClass,
				methodName });
		Method method = ReflectionUtils.findMethod(targetClass, methodName);
		AnnotationMetricPair<A, M> pair = null;
		if (method != null) {
			pair = getAnnotationMetricPair(method, targetClass, annotationClass);
		} else {
			LOG.debug("Method {} not found for Class {}", methodName, targetClass);

		}
		return pair;
	}

	/**
	 * It returns an annotationMetric pair based on the annotation configuration
	 * 
	 * @param method
	 *            the annotation decorating the method
	 * @return the annotationMetricPair<A, M>
	 */
	public AnnotationMetricPair<A, M> getAnnotationMetricPair(Method method, Class<?> targetClass,
			Class<A> annotationClass) {
		MethodKey methodKey = MethodKey.forMethod(method);
		AnnotationMetricPair<A, M> annotationMetricPair = metrics.get(methodKey);
		if (annotationMetricPair == null) {
			// create the pair and register it in the metrics map
			createAnnotationMetricPair(method, targetClass, annotationClass);
			annotationMetricPair = metrics.get(methodKey);
		}
		return annotationMetricPair;
	}

	/**
	 * Create an annotationMetricPair and register it in the metricRegistry
	 * {@link MetricRegistry}
	 * 
	 * @param method
	 *            the invoked method
	 * @param targetClass
	 *            the target class
	 * @param annotationClass
	 *            the meter annotation
	 */
	protected void createAnnotationMetricPair(Method method, Class<?> targetClass, Class<A> annotationClass) {
		final A annotation = method.getAnnotation(annotationClass);
		if (annotation != null) {
			final MethodKey methodKey = MethodKey.forMethod(method);
			final String metricName = buildMetricName(targetClass, method, annotation);
			final M metric = buildMetric(metricRegistry, metricName, annotation);

			if (metric != null) {
				metrics.put(methodKey, new AnnotationMetricPair<A, M>(annotation, metric));

				LOG.debug("Created {} {} for method {}", new Object[] { metric.getClass().getSimpleName(), metricName,
						methodKey });

			}
		}

	}

	/**
	 * create an annotationMetric pair
	 * 
	 * @param <A>
	 *            The annotation associated to the meter class
	 * @param <M>
	 *            The meter class
	 * @return the annotationMetricPair<A, M>
	 */
	public AnnotationMetricPair<A, M> createAnnotationMetricPair(A annotation, M meter) {
		return new AnnotationMetricPair<A, M>(annotation, meter);
	}

	/**
	 * Build the metric name from annotation definition
	 * 
	 * @param method
	 *            the invoked method
	 * @param targetClass
	 *            the target class
	 * @param annotationClass
	 *            the meter annotation
	 * @return the metric name
	 */
	protected abstract String buildMetricName(Class<?> targetClass, Method method, A annotation);

	/**
	 * Build the meter instance
	 * 
	 * @param method
	 *            the invoked method
	 * @param targetClass
	 *            the target class
	 * @param annotationClass
	 *            the meter annotation
	 * @return the meter <M> implementation
	 */
	protected abstract M buildMetric(MetricRegistry metricRegistry, String metricName, A annotation);

	/**
	 * @return the metricRegistry
	 */
	public MetricRegistry getMetricRegistry() {
		return metricRegistry;
	}

	/**
	 * @param metricRegistry
	 *            the metricRegistry to set
	 */
	public void setMetricRegistry(MetricRegistry metricRegistry) {
		this.metricRegistry = metricRegistry;
	}

	/**
	 * @return the healthCheckRegistry
	 */
	public HealthCheckRegistry getHealthCheckRegistry() {
		return healthCheckRegistry;
	}

	/**
	 * @param healthCheckRegistry
	 *            the healthCheckRegistry to set
	 */
	public void setHealthCheckRegistry(HealthCheckRegistry healthCheckRegistry) {
		this.healthCheckRegistry = healthCheckRegistry;
	}

}