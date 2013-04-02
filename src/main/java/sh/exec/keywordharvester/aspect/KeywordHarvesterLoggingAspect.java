package sh.exec.keywordharvester.aspect;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

import sh.exec.keywordharvester.exception.KeywordHarvesterException;

@Aspect
@Order(1)
public class KeywordHarvesterLoggingAspect {
	private Log log = null;

	@AfterReturning(value = "@annotation(Loggable)", returning = "object")
	public void afterReturning(JoinPoint joinPoint, Object object) {
		log = LogFactory.getLog(joinPoint.getTarget().getClass()
				.getCanonicalName().replaceAll("Impl", ""));

		log.info("Harvested " + object);
	}

	@AfterThrowing(value = "@annotation(Loggable)", throwing = "throwable")
	public void afterThrowing(JoinPoint joinPoint, Throwable throwable) {
		log = LogFactory.getLog(joinPoint.getTarget().getClass()
				.getCanonicalName().replaceAll("Impl", ""));

		if ((throwable instanceof KeywordHarvesterException && throwable
				.getCause() == null)
				|| (throwable instanceof KeywordHarvesterException && (throwable
						.getCause() != null && (throwable.getCause() instanceof KeywordHarvesterException))))
			log.info(throwable.getMessage());
		else
			log.error(ExceptionUtils.getStackTrace(throwable));
	}
}
