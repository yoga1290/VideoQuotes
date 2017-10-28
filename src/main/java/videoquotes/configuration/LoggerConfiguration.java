package videoquotes.configuration;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class LoggerConfiguration {


	// https://stackoverflow.com/a/11506352/1683797
	@Around("execution(* videoquotes.*.*.*(..))")
	public Object beforeLogger(ProceedingJoinPoint pjp) throws Throwable {
		String packageName = pjp.getSignature().getDeclaringTypeName();
		String methodName = pjp.getSignature().getName();
		Logger logger = LoggerFactory.getLogger(packageName);
		if(!pjp.getSignature().getName().equals("initBinder")) {
			logger.info("Entering method [" + methodName +  "]");
		}

		Object output = pjp.proceed();

		if(!methodName.equals("initBinder")) {
			logger.info("Exiting method [" + methodName + "]");
		}
		return output;
	}
}
