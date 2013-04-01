package sh.exec.keywordharvester.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@PropertySource("classpath:database.properties")
public class DatabaseConfig {
	@Autowired
    private Environment env;
	
	@Bean
	public JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
		jedisConnectionFactory.setUsePool(true);
		jedisConnectionFactory.setHostName(env.getProperty("redis.hostname"));
		jedisConnectionFactory.setPort(env.getProperty("redis.port", Integer.class));
		jedisConnectionFactory.setPassword(env.getProperty("redis.password"));
		return jedisConnectionFactory;
	}
	
	@SuppressWarnings("rawtypes")
	@Bean
	public RedisTemplate redisTemplate() {
		RedisTemplate redisTemplate = new RedisTemplate();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		return redisTemplate;
	}
	
	@Bean
	public RedisCacheManager redisCacheManager() {
		return new RedisCacheManager(redisTemplate());
	}
}
