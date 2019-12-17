package dummyapi.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;

import dummyapi.common.CustomObjectMapper;
import dummyapi.common.NullToEmptyStringSerializer;

@Configuration
public class JsonCustomConfigure {
	
	@Bean
    public MappingJackson2HttpMessageConverter converter() {

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

        CustomObjectMapper mapper = new CustomObjectMapper();

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        converter.setObjectMapper(mapper);

        return converter;

    }
	
	@Bean
	public ObjectMapper jacksonObjectMapper() {
		
		ObjectMapper objectMapper = new ObjectMapper();
	    
		DefaultSerializerProvider sp = new DefaultSerializerProvider.Impl();
	    
		sp.setNullValueSerializer(new NullToEmptyStringSerializer());
	    
		objectMapper.setSerializerProvider(sp);
	    
		return objectMapper;
	}
	
}