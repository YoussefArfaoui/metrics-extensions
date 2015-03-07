# metrics-extensions project

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



