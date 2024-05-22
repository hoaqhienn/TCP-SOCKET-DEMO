package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import dao.GuestDAO;
import entities.Guest;
import jakarta.persistence.EntityManager;
import services.EntityManagerFactoryUtil;
import services.GuestService;

public class Server {

	public static void main(String[] args) {
		EntityManagerFactoryUtil entityManagerFactory = new EntityManagerFactoryUtil();
		EntityManager entityManager = entityManagerFactory.getEnManager();

		try (ServerSocket welcomeSocket = new ServerSocket(6789)) {

			GuestDAO guestDAO = new GuestService(entityManager);

			while (true) {
				try (Socket connectionSocket = welcomeSocket.accept();
						ObjectOutputStream outToClient = new ObjectOutputStream(connectionSocket.getOutputStream());
						BufferedReader inFromClient = new BufferedReader(
								new InputStreamReader(connectionSocket.getInputStream()))) {

					String clientSentence = inFromClient.readLine();
					System.out.println("Received from client: " + clientSentence);

					if ("GET_GUEST_LIST".equals(clientSentence)) {
						List<Guest> guestList = guestDAO.guests();
						outToClient.writeObject(guestList);
					} else if ("COUNT_GUEST".equals(clientSentence)) {
						int count = guestDAO.count();
						outToClient.writeObject(count);
//                    } else if (clientSentence.contains("GET_GUEST_BY_ID")) {
					} else if (clientSentence.startsWith("GET_GUEST_BY_ID")) {
						Long guestId = Long.parseLong(clientSentence.split(" ")[1]);
						Guest guest = guestDAO.guestById(guestId);
						outToClient.writeObject(guest);
					} else if (clientSentence.startsWith("GET_FIRST_N_GUESTS")) {
						int n = Integer.parseInt(clientSentence.split(" ")[1]);
						List<Guest> guestList = guestDAO.intFirstGuest(n);
						outToClient.writeObject(guestList);
					} else {
						String response = "Unknown command: " + clientSentence;
						outToClient.writeObject(response);
					}
				} catch (IOException e) {
					System.err.println("Error communicating with client: " + e.getMessage());
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			System.err.println("Error starting server: " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}
	}
}
