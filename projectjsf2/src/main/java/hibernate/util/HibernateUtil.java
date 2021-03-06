package hibernate.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class HibernateUtil {
	/*Esta classe lê o arquivo de configuração persistence.xml e inicia uma conexão com o banco*/
	public static EntityManagerFactory factory = null;
	
	static {
		if(factory == null) {
			factory = Persistence.createEntityManagerFactory("projectjsf2");
		}
	}
	
	public static EntityManager getEntityManager() {
		return factory.createEntityManager();/*Prove a parte de persistencia*/
	}

	public static Object getPrimaryKey(Object entity) {/*Retorna a primary key(id) de uma entidade*/
		return factory.getPersistenceUnitUtil().getIdentifier(entity);
	}
}
