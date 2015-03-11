# metrics-extensions project

[ ![Build Status] [travis-image] ] [travis]
[ ![Release] [release-image] ] [releases]
[ ![License] [license-image] ] [license]

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

[release-image]: https://github.com/YoussefArfaoui/metrics-extensions/releases/release-v0.0.1-blue.svg?style=flat
[releases]: https://github.com/YoussefArfaoui/metrics-extensions/releases

[license-image]: http://img.shields.io/badge/license-Apache--2-blue.svg?style=flat
[license]: http://www.apache.org/licenses/LICENSE-2.0

