package sh.exec.keywordharvester.web.servlet.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

public class KeywordHarvesterExceptionResolver extends SimpleMappingExceptionResolver {

	protected ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception exception) {

		ModelAndView modelAndView = new ModelAndView(this.determineViewName(
				exception, request));

		return modelAndView;
	}
}