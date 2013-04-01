package sh.exec.keywordharvester.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.util.Log4jConfigListener;

public class KeywordHarvesterInitializer implements WebApplicationInitializer {

	public void onStartup(ServletContext servletContext)
			throws ServletException {
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.scan("sh.exec.keywordharvester.config");
		
		servletContext.addListener(new ContextLoaderListener(context));

		ServletRegistration.Dynamic dispatcherServlet = servletContext
				.addServlet("dispatcher", new DispatcherServlet(
						context));
		dispatcherServlet.setLoadOnStartup(1);
		dispatcherServlet.addMapping("/");

		servletContext.setInitParameter("log4jConfigLocation", "classpath:/log4j.xml");
		servletContext.addListener(new Log4jConfigListener());
	}
}
