package de.ks.messageOrg.DataReader;


import java.util.List;

import javax.persistence.*;

@Entity
public class Car {

	// JPA stuff that we need
	private static final String			PERSISTENCE_UNIT_NAME	= "cars";
	private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	private static EntityManager em = factory.createEntityManager();
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer cid;
	private String cname;
	private String color;
	private Integer speed;
	private String mfdctry;

	public Car() {
	}

	public Car(Integer cid, String cname, String color, Integer speed, String mfdctry) {
		this.cid = cid;
		this.cname = cname;
		this.color = color;
		this.speed = speed;
		this.mfdctry = mfdctry;
	}

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Integer getSpeed() {
		return speed;
	}

	public void setSpeed(Integer speed) {
		this.speed = speed;
	}

	public String getMfdctry() {
		return mfdctry;
	}

	public void setMfdctry(String mfdctry) {
		this.mfdctry = mfdctry;
	}

    @Override
    public String toString() {
        return "Car [cid=" + cid + ", cname=" + cname + ", color=" + color + ", speed=" + speed + ", mfdctry=" + mfdctry + "]";
    }

	@SuppressWarnings("unchecked")
	public static List<Car> getCars() {

		// read the existing entries and write to console
		Query q = em.createQuery("select t from Car t");
		return q.getResultList();
	}

	public void persistAndCommit() {

		em.getTransaction().begin();
		em.persist(this);
		em.getTransaction().commit();
		em.close();
	}

}