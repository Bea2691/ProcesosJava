package PetnovaHN;


import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static final SessionFactory sessionFactory = buildSessionFactory();

	private static SessionFactory buildSessionFactory() {
		try {
			// Crea el SessionFactory desde hibernate.cfg.xml
			return new Configuration().configure()
					.buildSessionFactory(new StandardServiceRegistryBuilder().configure().build());
		}

		catch (Throwable ex) {
			// Tratamiento excepción que podría ocurrir
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}