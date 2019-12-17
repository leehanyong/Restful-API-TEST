package dummyapi.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import dummyapi.interceptor.LoggerInterceptor;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
	
	@Override
	public void addInterceptors(InterceptorRegistry registry){
		
		registry.addInterceptor(new LoggerInterceptor());
		/*RESTful API 프로젝트에서는 화면을 사용하지 않기 때문에 제외*/
		//.excludePathPatterns("/assets/**/", "/**/assets/**/", "/include/**/", "/jpa/include/**/", "/js/**/");
	}
	
	@Bean
	public CommonsMultipartResolver multipartResolver() {
		
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
		commonsMultipartResolver.setDefaultEncoding("UTF-8");
		commonsMultipartResolver.setMaxUploadSizePerFile(5 * 1024 * 1024); // 5 * 1024 * 1024 (5mb)
		return commonsMultipartResolver;
	}
	
}