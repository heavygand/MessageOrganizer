import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import de.ks.messageOrg.model.Person;

public class SachenProbieren {

	public static void main(String[] args) {
		
		EntityManager em = Person.getEm();
		
		em.getTransaction().begin();
		
		em.createNativeQuery("SET NAMES utf8mb4").executeUpdate();
		
		List resultList = em.createNativeQuery("select * from ausnahmen").getResultList();

		em.getTransaction().commit();
		em.close();
		
		
		for(Object nameObj : resultList) {
			
			String name = (String) nameObj;
			System.out.println(nameObj);
		}
	}
}
