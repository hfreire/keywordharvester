package sh.exec.keywordharvester.service;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

import sh.exec.keywordharvester.exception.KeywordHarvesterException;
import sh.exec.keywordharvester.model.KeywordModel;

@Aspect
@Order(1)
public class WebKnoxLoggingAspect {
	private Log log = LogFactory.getLog(WebKnoxService.class);

	@Around("execution(* WebKnoxService.*(..))")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		try {
			KeywordModel keyword = (KeywordModel) joinPoint.proceed();
			
			log.info("Harvested " + keyword);
			
			return keyword;
		} catch (Exception e) {
			System.out.println(ExceptionUtils.getStackTrace(e));
			if ((e instanceof KeywordHarvesterException && e.getCause() == null)
					|| (e instanceof KeywordHarvesterException && (e.getCause() != null && (e
							.getCause() instanceof KeywordHarvesterException))))
				log.info(e.getMessage());
			else
				log.error(ExceptionUtils.getStackTrace(e));
			throw e;
		}
	}
}
