package services;

import java.util.List;

import dao.GuestDAO;
import entities.Guest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

public class GuestService implements GuestDAO {
	private EntityManager entityManager;

	public GuestService(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<Guest> guests() {
		Query query = entityManager.createQuery("SELECT p FROM Guest p");
		return query.getResultList();
	}

	@Override
	public Guest guestById(Long guestId) {
		return entityManager.find(Guest.class, guestId);
	}

	@Override
	public int count() {
		return entityManager.createQuery("SELECT COUNT(e) FROM Guest e", Long.class).getSingleResult().intValue();
	}

	@Override
	public List<Guest> intFirstGuest(int i) {
		Query query = entityManager.createQuery("SELECT p FROM Guest p");
		query.setMaxResults(i);
		return query.getResultList();
	}

	@Override
	public Guest guestByName(String guestName) {
		Query query = entityManager.createQuery("SELECT p FROM Guest p WHERE p.firstName = :name");
		query.setParameter("name", guestName);
		try {
			return (Guest) query.getSingleResult();
		} catch (NoResultException e) {
			// Handle case where no guest is found with the ID (optional)
			return null; // Or throw an exception if not finding a guest is an error
		}
	}

	@Override
	public boolean create(Guest guest) {
		EntityTransaction trans = entityManager.getTransaction();
		try {
			trans.begin();
			entityManager.persist(guest);
			trans.commit();
			return true;
		} catch (Exception ex) {
			if (trans.isActive()) {
				trans.rollback();
			}
			ex.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean updateGuest(Guest guest) {
		EntityTransaction trans = entityManager.getTransaction();
		try {
			trans.begin();
			entityManager.merge(guest);
			trans.commit();
			return true;
		} catch (Exception ex) {
			if (trans.isActive()) {
				trans.rollback();
			}
			ex.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean delete(Long guestId) {
		EntityTransaction trans = entityManager.getTransaction();
		try {
			trans.begin();
			Guest e = entityManager.find(Guest.class, guestId);
			entityManager.remove(e);
			trans.commit();
			return true;
		} catch (Exception ex) {
			if (trans.isActive()) {
				trans.rollback();
			}
			ex.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean deleteAll() {
		EntityTransaction trans = entityManager.getTransaction();
		try {
			trans.begin();
			List<Guest> list = guests();
			for (Guest g : list) {
				entityManager.remove(g);
			}
			trans.commit();
			return true;
		} catch (Exception ex) {
			if (trans.isActive()) {
				trans.rollback();
			}
			ex.printStackTrace();
		}
		return false;
	}

}
