package sh.exec.keywordharvester.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "sh.exec.keyword.harvester.web.controller")
public class WebConfig extends WebMvcConfigurerAdapter {

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
