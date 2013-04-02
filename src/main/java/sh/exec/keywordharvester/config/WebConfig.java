package sh.exec.keywordharvester.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import sh.exec.keywordharvester.web.servlet.handler.KeywordHarvesterExceptionResolver;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "sh.exec.keyword.harvester.web.controller")
public class WebConfig extends WebMvcConfigurerAdapter {

	@Bean
	public HandlerExceptionResolver keyworExceptionResolver() {
		KeywordHarvesterExceptionResolver keywordHarvesterExceptionResolver = new KeywordHarvesterExceptionResolver();
		keywordHarvesterExceptionResolver.setDefaultErrorView("error");
		
		Properties exceptionMappings = new Properties();
		exceptionMappings.put("java.lang.RuntimeException", "error");
		keywordHarvesterExceptionResolver.setExceptionMappings(exceptionMappings);
		
		return keywordHarvesterExceptionResolver;
	}
	
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("**")
                .addResourceLocations("/");
    }
    
    @Bean
	public ViewResolver viewResolver() {
		UrlBasedViewResolver resolver = new UrlBasedViewResolver();
		resolver.setViewClass(JstlView.class);
		resolver.setPrefix("/jsp/");
		resolver.setSuffix(".jsp");
		return resolver;
	}
}
