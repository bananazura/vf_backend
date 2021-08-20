package com.byelken.auto.carpark;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.byelken.auto.carpark.config.HibernateConfig;
import com.byelken.auto.carpark.config.SecurityConfig;
import com.byelken.auto.carpark.config.SwaggerConfig;

/**
 * @author Berkay.Yelken
 *
 */
@Configuration
@Import({ HibernateConfig.class, SecurityConfig.class, SwaggerConfig.class })
@PropertySource("classpath:server.properties")
@ComponentScans(@ComponentScan(basePackages = {"com.byelken.auto.carpark"}))
public class ApplicationConfig
{
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}
