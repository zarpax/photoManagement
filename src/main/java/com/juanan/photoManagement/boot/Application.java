package com.juanan.photoManagement.boot;


import javax.servlet.MultipartConfigElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ComponentScan("com.juanan")
@SpringBootApplication
@EnableJpaRepositories
@EnableAsync
@EntityScan("com.juanan")
public class Application {

	@Autowired
	private static Environment env;

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize("5120KB");
		factory.setMaxRequestSize("5120KB");
		return factory.createMultipartConfig();
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/fileService").allowedOrigins("*");
			}
		};
	}

	public static void main(String[] args) {
		//    	SpringApplication application = new SpringApplication(Application.class);
		//    	ConfigurableEnvironment environment = new StandardEnvironment();
		//    	String property = environment.getProperty("photo.base.path");
		//    	FilesHelper.SET_PHOTO_BASE_PATH(property);
		//
		//    	application.run(args);

		SpringApplication.run(Application.class, args);
	}
}
