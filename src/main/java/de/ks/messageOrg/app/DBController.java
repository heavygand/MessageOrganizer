package de.ks.messageOrg.app;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import de.ks.messageOrg.model.Person;

@SuppressWarnings("rawtypes")
public class DBController {

	private static EntityManager em = Person.getEm();

	public static List<String> getAll() {
		
		return select("title", "persons");
	}

	public static List<String> getUnwritten() {
		
		return select("title", "unwritten");
	}

	public static List<String> getWritten() {

		return select("title", "written");
	}

	public static List<String> getGroupSend() {

		return select("title", "group_send");
	}

	public static List<String> getInGroup() {

		return select("title", "in_group");
	}

	public static List<String> getVideo() {

		return select("title", "video");
	}

	public static List<String> getCustomers() {

		return select("title", "customers");
	}

	public static List<String> getNachzufassen() {

		return select("title", "nachfassen");
	}

	public static List<String> getTable(String id) {

		return select("title", id);
	}

	public static List getPerson(String name) {

		Query query = em.createNativeQuery("SELECT * FROM persons where title = ?");
		query.setParameter(1, name); // title
		
		List<String> select = select(query);

		if(select.size() == 0 || select == null) {
			
			System.err.println("getPerson() hat kein Ergebis gehabt für name = " + name);
			return null;
		}
		if(select.size() > 1) System.err.println("getPerson() hat mehr als 1 Ergebis gehabt für name = " + name);
		
		return select; 
	}

	public static List getMessages(String id) {

		Query query = em.createNativeQuery("SELECT * FROM messages where id = ?");
		query.setParameter(1, id); // title
		
		List<String> select = select(query);
		
		return select;
	}

	public static void savePersonNotes(String title, String state, String notes, long nachfassen, long friendsSince) {

		em.getTransaction().begin();
		em.createNativeQuery("SET NAMES utf8mb4").executeUpdate();
			
		Query query = em.createNativeQuery( "UPDATE properties " +
											"SET state = ?, notes = ?, nachfassen = ?" +
											"WHERE title = ? and friendsSince = ?"
											);
		query.setParameter(1, state);
		query.setParameter(2, notes);
		query.setParameter(3, nachfassen);
		query.setParameter(4, title);
		query.setParameter(5, friendsSince);
		
		em.getTransaction().commit();
	}
	
	/*
	 * 
	 * H E L P E R S
	 * 
	 */
	private static List<String> select(String select, String from) {
		
		return select(select, from, "");
	}
	
	@SuppressWarnings("unchecked")
	private static List<String> select(Query query) {
		
		em.getTransaction().begin();
		em.createNativeQuery("SET NAMES utf8mb4").executeUpdate();
			
		List<String> resultList = query.getResultList();
		
		em.getTransaction().commit();
//		em.close();
		return resultList;
	}

	/**
	 * @param select Comma separated list of columns
	 * @param from Table
	 * @return resultList
	 */
	@SuppressWarnings("unchecked")
	private static List<String> select(String select, String from, String where) {

		em.getTransaction().begin();
		em.createNativeQuery("SET NAMES utf8mb4").executeUpdate();
			
		if (!where.equals("")) {
			
			where = " WHERE " + where;
		}
		
		Query query = em.createNativeQuery("SELECT " + select + " FROM " + from + where);
		List<String> resultList = query.getResultList();
		
		em.getTransaction().commit();
		return resultList;
	}
}