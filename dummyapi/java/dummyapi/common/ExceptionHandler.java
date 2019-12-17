package dummyapi.common;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ExceptionHandler {
	
	@org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
	public Map<String, Object>  defaultExceptionHandler(HttpServletRequest request, Exception exception){
		log.error("************************************* defaultExceptionHandler *****************************************", exception);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		resultMap.put("exception", exception.toString());
		
		return resultMap;
	}
}
