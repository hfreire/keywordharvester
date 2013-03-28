package sh.exec.keywordharvester.service;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

import sh.exec.keywordharvester.model.KeywordModel;

@Aspect
@Order(2)
public class KeywordHarvesterCachingAspect {
	private Log log = LogFactory.getLog(KeywordHarvesterService.class);
	
	@Inject
	private Map<String, KeywordModel> keywordCache;

	@Around("execution(* VeryRelatedService.*(..))")
	public Object cacheAround(ProceedingJoinPoint joinPoint) throws Throwable {
		try {
			String stringKeyword = (String) joinPoint.getArgs()[0];
			if (keywordCache.containsKey(stringKeyword))
				return keywordCache.get(stringKeyword);
			else {
				KeywordModel keyword = (KeywordModel) joinPoint.proceed();
				keywordCache.put(keyword.getText(), keyword);
				return keyword;
			}
		} catch (Exception e) {
			log.info(e.getMessage());
			throw e;
		}
	}
}
