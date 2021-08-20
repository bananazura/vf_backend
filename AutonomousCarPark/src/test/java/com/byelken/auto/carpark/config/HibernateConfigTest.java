package com.byelken.auto.carpark.config;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

public class HibernateConfigTest
{
	@Test
	public void testHibernateSetup()
	{
		Session session = HibernateConfig.getSessionFactory().openSession();
		Transaction tx = session.getTransaction();
		tx.commit();
		session.close();
		HibernateConfig.getSessionFactory().close();
	}
}
