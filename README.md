# metrics-extensions project

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

```xml
<servlet>
		<servlet-name>metrics</servlet-name>
		<servlet-class>com.ay.web.metrics.servlets.AyAdminServlet</servlet-class>
		<load-on-startup>1</load-on-startup>
</servlet>
```
```xml
	<import resource="classpath*:*spring/metrics-config.xml" />
```
