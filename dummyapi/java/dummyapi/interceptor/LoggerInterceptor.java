package dummyapi.interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggerInterceptor extends HandlerInterceptorAdapter{
	
//	private Map<String,Object> paramMAP;
//	
//	public LoggerInterceptor() {
//		super();
//		paramMAP = new HashMap<String, Object>();
//	}
	
	/*
	 * API 요청에 대한 컨트롤러 호출 전 전처리 서비스
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		log.debug("======================================          START         ======================================");
		log.debug(" Request URI \t:  " + request.getRequestURI());
		
		/*
		 * 전처리에서 중복요청 제어 시 사용할 수 있을 것 같음.
		 */
//		boolean flag = false; 
//		String method = request.getMethod();
//		String queryString = "";
//		
//		if(method.equals("GET") || method.equals("get"))
//		{
//			queryString = request.getQueryString();
//		} else {
//			queryString = getBody(request);
//		}
//		
//		if(queryString != null && queryString.equals("") == false){
//        	if(paramMAP.containsKey(queryString) == true){
//
//        		flag = false;
//        		request.getRequestDispatcher("/overlappingRequest.jsp").forward(request, response);
//        		return flag;
//        		
//        	}else{
//
//        		paramMAP.put(queryString, queryString);
//        		flag = true;
//        	}
//        }
		
		return super.preHandle(request, response, handler);
	}
	
	/*
	 * API 요청에 대한 컨트롤러 호출 후 후처리 서비스
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		
		log.debug("======================================           END          ======================================\n");
	}
	
	/**
	 * POST 요청정보 BODY KEY,VALUE 추출
	 */
    public String getBody(HttpServletRequest request) {
        String body = null;
        StringBuffer sf = new StringBuffer();
        String contentType = request.getContentType();
		String pName = "";
   	  	String pValue = "";
   	  	
        boolean first=true;
        
        if(contentType.indexOf("multipart") == -1) {
        	Enumeration<String> parameterNames = request.getParameterNames();
        	 while (parameterNames.hasMoreElements()) {
        		pName = parameterNames.nextElement();
           	  	pValue = request.getParameter(pName);
           	  	if(first==true){
           	  		sf.append(pName+"="+pValue)	;
           	  	}else{
           	  		sf.append("&"+pName+"="+pValue);
           	  	}
           	  	if(first==true) first=false;
        	 }
        }

        body = sf.toString();
        return body;
    }
}