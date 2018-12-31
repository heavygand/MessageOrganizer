import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import de.ks.messageOrg.model.Person;
import helpers.H;

public class SachenProbieren {

	public static void main(String[] args) {
		
//		EntityManager em = Person.getEm();
		
//		em.getTransaction().begin();
//		
//		em.createNativeQuery("SET NAMES utf8mb4").executeUpdate();
		
//		Query query = em.createNativeQuery("select * from ausnahmen where title = 'Jindřich Prášil' or title = 'Leon Klaßen'");//

//		em.getTransaction().commit();
//		em.close();
		
		for (String kackLine : H.getLines("./docs/ausgenommen")) {

			String line = H.cleanUp(kackLine);
		}
	}
}
