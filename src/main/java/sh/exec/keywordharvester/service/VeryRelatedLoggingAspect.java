package sh.exec.keywordharvester.service;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

import sh.exec.keywordharvester.exception.KeywordHarvesterException;

@Aspect
@Order(1)
public class VeryRelatedLoggingAspect {
	private Log log = LogFactory.getLog(VeryRelatedService.class);

	@Around("execution(* VeryRelatedService.*(..))")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		try {
			return joinPoint.proceed();
		} catch (Exception e) {
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
