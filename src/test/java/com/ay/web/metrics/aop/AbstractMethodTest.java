package com.ay.web.metrics.aop;

import java.lang.annotation.Annotation;

import com.ay.web.metrics.MeterClass;

/**
 * AbstractMethod Test class
 * 
 * @author Youssef Arfaoui
 *
 *         Feb 17, 2015
 *
 * @param <A>
 */
public abstract class AbstractMethodTest<A extends Annotation, M> {
	public static final Class<?> targetClass = MeterClass.class;

	public AbstractMethodTest() {
		super();
	}

	/**
	 * @return
	 */
	public abstract Class<A> getAnnotationClass();

	/**
	 * @return
	 */
	public abstract String getMetricName();

	/**
	 * @return
	 */
	public abstract String getAnnotatedMethodName();

}