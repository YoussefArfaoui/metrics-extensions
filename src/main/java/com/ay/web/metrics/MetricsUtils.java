/**
 * 
 */
package com.ay.web.metrics;

import static com.codahale.metrics.MetricRegistry.name;

import java.lang.reflect.Member;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Gauge;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import com.ryantenney.metrics.annotation.CachedGauge;
import com.ryantenney.metrics.annotation.Counted;
import com.ryantenney.metrics.annotation.Metric;

/**
 * 
 * Util class for Metrics name processing
 * 
 * @author Youssef Arfaoui
 *
 *         Feb 16, 2015
 *
 */
public class MetricsUtils {

	private MetricsUtils() {
	}

	public static String forTimedMethod(Class<?> targetClass, Member member, Timed annotation) {
		return chooseName(annotation.name(), annotation.absolute(), targetClass, member);
	}

	public static String forMeteredMethod(Class<?> targetClass, Member member, Metered annotation) {
		return chooseName(annotation.name(), annotation.absolute(), targetClass, member);
	}

	public static String forGauge(Class<?> targetClass, Member member, Gauge annotation) {
		return chooseName(annotation.name(), annotation.absolute(), targetClass, member);
	}

	public static String forCachedGauge(Class<?> targetClass, Member member, CachedGauge annotation) {
		return chooseName(annotation.name(), annotation.absolute(), targetClass, member);
	}

	public static String forExceptionMeteredMethod(Class<?> targetClass, Member member, ExceptionMetered annotation) {
		return chooseName(annotation.name(), annotation.absolute(), targetClass, member,
				ExceptionMetered.DEFAULT_NAME_SUFFIX);
	}

	public static String forCountedMethod(Class<?> targetClass, Member member, Counted annotation) {
		return chooseName(annotation.name(), annotation.absolute(), targetClass, member);
	}

	public static String forMetricField(Class<?> targetClass, Member member, Metric annotation) {
		return chooseName(annotation.name(), annotation.absolute(), targetClass, member);
	}

	public static String chooseName(String explicitName, boolean absolute, Class<?> targetClass, Member member,
			String... suffixes) {
		if (explicitName != null && !explicitName.isEmpty()) {
			if (absolute) {
				return explicitName;
			}
			return name(targetClass.getCanonicalName(), explicitName);
		}
		return name(name(targetClass.getCanonicalName(), member.getName()), suffixes);
	}
}