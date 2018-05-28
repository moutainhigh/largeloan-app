package com.xianjinxia.cashman.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
public class ServerConfig extends WebMvcConfigurerAdapter{
	
	@Autowired
	private MyGsonHttpMessageConverter myGsonHttpMessageConverter;

	
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

         converters.add(0, myGsonHttpMessageConverter);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public RestTemplate restTemplateWithAbsoluteUrl(){
        return new RestTemplate();
    }


    @Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

}
