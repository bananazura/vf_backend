package com.byelken.auto.carpark.repository;

import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;
import org.springframework.util.DigestUtils;

import com.byelken.auto.carpark.config.HibernateConfig;
import com.byelken.auto.carpark.exception.InvalidTokenException;
import com.byelken.auto.carpark.filter.CustomAuthenticationManager;
import com.byelken.auto.carpark.filter.JwtTokenProvider;
import com.byelken.auto.carpark.model.User;

/**
 * @author Berkay.Yelken
 */

@Repository
public class UserRepository implements IUserRepository
{
	private static final String splitter = ":";

	private static SessionFactory sessionFactory;

	private static AuthenticationManager authenticationManager;

	private static JwtTokenProvider jwtTokenProvider;

	static
	{
		sessionFactory = HibernateConfig.getSessionFactory();
		authenticationManager = new CustomAuthenticationManager();
		jwtTokenProvider = new JwtTokenProvider();
	}

	public static Map<String, String> doSignup(User user)
	{
		doSignupTx(user);
		return getModel(user.getEmail(), user.getPassword());
	}

	public static void doSignupTx(User user)
	{
		Session session = sessionFactory.openSession();

		String email = user.getEmail();
		String token = email + splitter + user.getPassword();
		String password = DigestUtils.md5DigestAsHex(token.getBytes());
		user.setPassword(password);

		Transaction tx = session.beginTransaction();
		session.persist(user);
		tx.commit();

		session.close();
	}

	private static Map<String, String> getModel(String email, String password)
	{
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(email, password));
		String authToken = jwtTokenProvider.createToken(authentication);

		Map<String, String> model = new ConcurrentHashMap<>();
		model.put("email", email);
		model.put("token", authToken);
		return model;
	}

	public static Map<String, String> doLogin(String encryToken) throws InvalidTokenException
	{

		Session session = sessionFactory.openSession();
		String token = new String(Base64.getDecoder().decode(encryToken));

		if (!token.contains(splitter) || token.split(splitter).length != 2)
			throwException(session, encryToken);

		String[] parts = token.split(splitter);
		String email = parts[0];
		String password = DigestUtils.md5DigestAsHex(token.getBytes());

		Map<String, String> model = getModel(email, password);

		session.close();
		return model;
	}

	private static void throwException(Session session, String token) throws InvalidTokenException
	{
		session.close();
		throw new InvalidTokenException("Token is invalid. Token: " + token);
	}

	@Override
	public Optional<User> findByEmail(String email)
	{
		return Optional.of(findUserByEmail(email));
	}

	public static User findUserByEmail(String email)
	{
		Session session = sessionFactory.openSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<User> criteria = builder.createQuery(User.class);
		Root<User> root = criteria.from(User.class);

		criteria.select(root).where(root.get("email").in(email));

		TypedQuery<User> query = session.createQuery(criteria);
		return query.getSingleResult();
	}

}
