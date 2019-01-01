import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import de.ks.messageOrg.DataReader.DataReader;
import de.ks.messageOrg.model.Person;
import helpers.H;

public class SachenProbieren {
	private static String				cyPath				= "./docs/CY.txt";

	public static void main(String[] args) {
		
		EntityManager em = Person.getEm();
		
		em.getTransaction().begin();
		em.createNativeQuery("SET NAMES utf8mb4").executeUpdate();
		
		H.getLines(cyPath).forEach(line -> {
			
			handleThis(line, em);
		});

		em.getTransaction().commit();
		em.close();
	}

	/**
	 * @param line
	 * @param em 
	 */
	private static void handleThis(String line, EntityManager em) {
		
//		String line = H.cleanUp(dirtyLine);

		// Wir checken ob das eine Ausnahme ist
		if(DataReader.isAusnahme(line, em)) return;
		
		// Wir holen uns das FriendsSince
		Query query = em.createNativeQuery("select friendsSince from persons where title = ?");
		query.setParameter(1, line);
		List resultList = query.getResultList();
		long friendsSince = 0;
		try {
			
			friendsSince = (long)resultList.get(0);
		}
		catch (ArrayIndexOutOfBoundsException e) {
			
			System.err.println(line + " wurde in der Liste der Personen nicht gefunden");
		}
		
		// Wir speichern das Ganze in die DB
		Query query2 = em.createNativeQuery("INSERT IGNORE INTO cy "
										+ "(title, friendsSince) "
										+ "values (?, ?)");
		query2.setParameter(1, line);			// title
		query2.setParameter(2, friendsSince);	// friendsSince
		query2.executeUpdate();
	}
}
