/**
 * 
 */
package com.ay.web.metrics;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import com.ryantenney.metrics.annotation.Counted;

/**
 * Util class for testing
 * 
 * @author Youssef Arfaoui
 *
 *         Feb 17, 2015
 *
 */
public class MeterClass {

	@Timed
	public void timedMethod() {

	}

	@Counted
	public void countedMethod() {

	}

	@Metered
	public void meteredMethod() {

	}

	@ExceptionMetered(cause = RuntimeException.class)
	public void exceptionMeteredMethod() {

	}

}
