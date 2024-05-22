package dao;

import java.util.List;

import entities.Guest;

public interface GuestDAO {
	public int count();

	public List<Guest> guests();

	public List<Guest> intFirstGuest(int i);

	public Guest guestById(Long guestId);

	public Guest guestByName(String guestName);

	public boolean create(Guest guest);

	public boolean updateGuest(Guest guest);

	public boolean delete(Long guestId);

	public boolean deleteAll();

}
