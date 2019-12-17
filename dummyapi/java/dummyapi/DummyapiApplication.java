package dummyapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(exclude= {MultipartAutoConfiguration.class})
public class DummyapiApplication extends SpringBootServletInitializer {

	private static final Logger logger = LoggerFactory.getLogger(DummyapiApplication.class);

	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DummyapiApplication.class);
    }
	
	public static void main(String[] args) {
		SpringApplication.run(DummyapiApplication.class, args);
	}

}
