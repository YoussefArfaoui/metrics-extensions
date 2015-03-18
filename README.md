# metrics-extensions project

[ ![Build Status] [travis-image] ] [travis]
[ ![Issues] [issues-image] ] [issues]
[ ![Release] [release-image] ] [releases]
[ ![Tags] [tag-image] ] [tag]
[![Coverage Status](https://coveralls.io/repos/YoussefArfaoui/metrics-extensions/badge.svg)](https://coveralls.io/r/YoussefArfaoui/metrics-extensions)
[ ![License] [license-image] ] [license]
[ ![Forks] [forks-image] ] [forks]
[![Maven Central](https://img.shields.io/maven-central/v/org.apache.maven/apache-maven.svg)]()
[![Issue Stats](http://issuestats.com/github/YoussefArfaoui/metrics-extensions/badge/pr)](http://issuestats.com/github/YoussefArfaoui/metrics-extensions)
[![Issue Stats](http://issuestats.com/github/YoussefArfaoui/metrics-extensions/badge/issue)](http://issuestats.com/github/YoussefArfaoui/metrics-extensions)



 Add the metrics-config.xml file in your spring context by importing it in the correct spring configuration file.
 
```xml
	<import resource="classpath*:*spring/metrics-config.xml" />
```

Add AyAdminServlet servelet configuration in the web.xml configuration file to have web metrics access.

```xml
<servlet>
		<servlet-name>metrics</servlet-name>
		<servlet-class>com.ay.web.metrics.servlets.AyAdminServlet</servlet-class>
		<load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
        <servlet-name>metrics</servlet-name>
        <url-pattern>/metrics/*</url-pattern>
</servlet-mapping>
```

All metrics annotations can be used with this module. example :

```java
	@RequestMapping(value = "/monitor/version")
	@ResponseBody
	@Counted(name = "monitor.version.counts", absolute = true, monotonic = true)
	@Timed(name = "monitor.version.timings", absolute = true)
	@ExceptionMetered(name = "monitor.version.exceptions", absolute = true, cause = Exception.class)
	public ResponseEntity<String> getVersion() {
	...
	}
```



[travis-image]: https://travis-ci.org/YoussefArfaoui/metrics-extensions.svg?branch=master
[travis]: https://travis-ci.org/YoussefArfaoui/metrics-extensions

[release-image]: https://img.shields.io/github/release/qubyte/rubidium.svg?style=plastic
[releases]: https://github.com/YoussefArfaoui/metrics-extensions/releases

[license-image]: http://img.shields.io/badge/license-Apache--2-blue.svg?style=plastic
[license]: http://www.apache.org/licenses/LICENSE-2.0
[forks-image]: https://img.shields.io/github/forks/badges/shields.svg?style=plastic
[forks]:https://github.com/YoussefArfaoui/metrics-extensions/network
[issues-image]: https://img.shields.io/github/issues/badges/shields.svg?style=plastic
[issues-raw-image]: https://img.shields.io/github/issues-raw/badges/shields.svg?style=plastic
[issues]:https://github.com/YoussefArfaoui/metrics-extensions/issues
[maven-image]: https://img.shields.io/maven-central/v/org.apache.maven/apache-maven.svg
[tag-image]: https://img.shields.io/github/tag/strongloop/express.svg?style=plastic
[tag]: https://github.com/YoussefArfaoui/metrics-extensions/tags




