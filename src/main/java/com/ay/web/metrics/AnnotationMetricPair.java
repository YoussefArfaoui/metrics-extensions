package com.ay.web.metrics;

import java.lang.annotation.Annotation;

/**
 * 
 * Present the association meter annotation - meter
 * 
 * @author Youssef Arfaoui
 *
 *         Feb 13, 2015
 *
 * @param <A>
 *            The annotation associated to the meter class
 * @param <M>
 *            The meter class
 */
public class AnnotationMetricPair<A extends Annotation, M> {

	private final A annotation;
	private final M meter;

	public AnnotationMetricPair(final A annotation, final M meter) {
		this.annotation = annotation;
		this.meter = meter;
	}

	public A getAnnotation() {
		return annotation;
	}

	public M getMeter() {
		return meter;
	}

}
