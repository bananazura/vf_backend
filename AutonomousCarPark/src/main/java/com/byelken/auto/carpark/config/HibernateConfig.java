package com.byelken.auto.carpark.config;

import java.util.Optional;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.byelken.auto.carpark.model.Garage;
import com.byelken.auto.carpark.model.User;

@EnableTransactionManagement
@PropertySource("classpath:db.properties")
public class HibernateConfig
{
	private static SessionFactory sessionFactory;

	static
	{
		sessionFactory = new Configuration().configure().addAnnotatedClass(Garage.class).addAnnotatedClass(User.class)
				.buildSessionFactory();
	}

	public static SessionFactory getSessionFactory()
	{
		return sessionFactory;
	}

	@Bean
	public AuditorAware<String> auditor()
	{
		return () -> Optional.ofNullable(SecurityContextHolder.getContext()).map(SecurityContext::getAuthentication)
				.filter(Authentication::isAuthenticated).map(Authentication::getPrincipal).map(UserDetails.class::cast)
				.map(u -> u.getUsername());
	}

}
