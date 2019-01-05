package de.ks.messageOrg.app;

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

	public static List getPerson(String name) {

		Query query = em.createNativeQuery("SELECT * FROM persons where title = ?");
		query.setParameter(1, name); // title
		
		List<String> select = select(query);

		if(select.size() == 0 || select == null) {
			
			System.err.println("getPerson() hat kein Ergebis gehabt f�r name = " + name);
			return null;
		}
		if(select.size() > 1) System.err.println("getPerson() hat mehr als 1 Ergebis gehabt f�r name = " + name);
		
		return select; 
	}

	public static List getMessages(String id) {

		Query query = em.createNativeQuery("SELECT * FROM messages where id = ?");
		query.setParameter(1, id); // title
		
		List<String> select = select(query);
		
		return select;
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
			
		String wherestatement = " WHERE " + where;
		Query query = em.createNativeQuery("SELECT " + select + " FROM " + from + wherestatement);
		List<String> resultList = query.getResultList();
		
		em.getTransaction().commit();
		em.close();
		return resultList;
	}
}