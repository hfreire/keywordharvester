package sh.exec.keywordharvester.config;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;

import sh.exec.keywordharvester.service.KeywordHarvesterAdapter;
import sh.exec.keywordharvester.service.VeryRelatedLoggingAspect;
import sh.exec.keywordharvester.service.VeryRelatedService;
import sh.exec.keywordharvester.service.WebKnoxLoggingAspect;
import sh.exec.keywordharvester.service.WebKnoxService;

@ComponentScan(basePackages = "sh.exec.keywordharvester")
@Configuration
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
@EnableCaching
@EnableAspectJAutoProxy
public class AppConfig implements ApplicationContextAware {
	private ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Bean
	public PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
		PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
		final Resource[] resources = new ClassPathResource[] {
				new ClassPathResource("veryrelated.properties"),
				new ClassPathResource("webknox.properties") };
		propertyPlaceholderConfigurer.setLocations(resources);
		return propertyPlaceholderConfigurer;
	}

	/**
	 * @return
	 */
	@Bean
	public DefaultHttpClient httpClient() {
		PoolingClientConnectionManager poolingClientConnectionManager = new PoolingClientConnectionManager();
		poolingClientConnectionManager.setMaxTotal(100);
		
		DefaultHttpClient httpClient = new DefaultHttpClient(poolingClientConnectionManager);
		
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
				new Integer(50000000));
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				new Integer(50000000));
		httpClient.getParams().setParameter(CoreConnectionPNames.TCP_NODELAY, true);
		httpClient
				.getParams()
				.setParameter(
						CoreProtocolPNames.USER_AGENT,
						"Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
		
		return httpClient;
	}

	@Bean
	public HttpComponentsClientHttpRequestFactory httpClientFactory() {
		HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpComponentsClientHttpRequestFactory.setHttpClient(httpClient());
		return httpComponentsClientHttpRequestFactory;
	}

	@Bean
	public Jaxb2Marshaller jaxb2Marshaller() {
		Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
		Class<?>[] classesToBeBound = new Class<?>[] { sh.exec.keywordharvester.service.impl.veryrelated.ResultSet.class };
		jaxb2Marshaller.setClassesToBeBound(classesToBeBound);
		return jaxb2Marshaller;
	}

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate(httpClientFactory());

		List<HttpMessageConverter<?>> messageConverters = new LinkedList<HttpMessageConverter<?>>();
		messageConverters.add(new FormHttpMessageConverter());
		messageConverters.add(new StringHttpMessageConverter());

		MarshallingHttpMessageConverter marshallingHttpMessageConverter = new MarshallingHttpMessageConverter();
		Jaxb2Marshaller jaxb2Marshaller = jaxb2Marshaller();
		marshallingHttpMessageConverter.setMarshaller(jaxb2Marshaller);
		marshallingHttpMessageConverter.setUnmarshaller(jaxb2Marshaller);
		messageConverters.add(marshallingHttpMessageConverter);

		return restTemplate;
	}

	@Bean
	public KeywordHarvesterAdapter keywordHarvesterService() {
		KeywordHarvesterAdapter keywordHarvesterAdapter = new KeywordHarvesterAdapter();
		keywordHarvesterAdapter.getApis().put("veryrelated",
				applicationContext.getBean(VeryRelatedService.class));
		keywordHarvesterAdapter.getApis().put("webknox",
				applicationContext.getBean(WebKnoxService.class));

		return keywordHarvesterAdapter;
	}
	
	@Bean
	public VeryRelatedLoggingAspect veryRelatedLoggingAspect() {
		return new VeryRelatedLoggingAspect();
	}
	
	@Bean
	public WebKnoxLoggingAspect webKnoxLoggingAspect() {
		return new WebKnoxLoggingAspect();
	}
	
//	@Bean
//    public CacheManager cacheManager() {
//        SimpleCacheManager cacheManager = new SimpleCacheManager();
//        List<ConcurrentMapCache> caches = new ArrayList<ConcurrentMapCache>();
//        caches.add(new ConcurrentMapCache("veryRelatedCache"));
//        caches.add(new ConcurrentMapCache("webKnoxCache"));
//        cacheManager.setCaches(caches);
//        return cacheManager;
//    }
}
