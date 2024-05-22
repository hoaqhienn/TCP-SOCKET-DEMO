package socket;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import entities.Guest;

public class GuestListClient {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int choice;

		do {
			for (int i = 0; i < 50; i++) { // Adjust number of backspaces as needed
				System.out.print("-");
			}
			System.out.print("\r");
			System.out.println("\nGuest List Client Menu:");
	        System.out.println("1. All guests");
	        System.out.println("2. Count guests");
	        System.out.println("3. Get guest by ID");
	        System.out.println("4. Get first n guests");
	        System.out.println("5. Create new guest");
	        System.out.println("6. Update guest");
	        System.out.println("7. Delete guest by ID");
	        System.out.println("0. Disconnect");
	        System.out.print("Enter your choice: ");

			try {
				choice = scanner.nextInt();
			} catch (InputMismatchException e) {
				System.err.println("Invalid choice. Please enter between 0 and 7");
				scanner.nextLine(); // Clear the scanner buffer
				choice = -1; // Set choice to an invalid value to prompt for input again
				continue;
			}

			switch (choice) {
			case 1:
				processGuestListRequest();
				break;
			case 2:
				processCountGuest();
				break;
			case 3:
				System.out.print("Enter guest ID: ");
				Long guestId = scanner.nextLong();
				processGetGuestById("GET_GUEST_BY_ID " + guestId);
				break;
			case 4:
				System.out.print("Enter number: ");
				int i = scanner.nextInt();
				processNFirstGuess("GET_FIRST_N_GUESTS " + i);
				break;
			case 0:
				System.out.println("Disconnecting from server...");
				break;
			default:
				System.err.println("Invalid choice. Please enter between 0 and 7");
			}
		} while (choice != 0);

		scanner.close();
	}

	private static void processNFirstGuess(String string) {
		InetAddress serverAddress;
		int port = 6789;

		try {
			serverAddress = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			System.err.println("Error: Could not find server. Please check the hostname or IP address.");
			e.printStackTrace();
			return;
		}

		try (Socket connectionSocket = new Socket(serverAddress, port);
				DataOutputStream outToServer = new DataOutputStream(connectionSocket.getOutputStream());
				ObjectInputStream inFromServer = new ObjectInputStream(connectionSocket.getInputStream())) {

			// Send request for guest list
			outToServer.writeBytes("GET_FIRST_N_GUESTS" + string + "\n");

			// Receive guest list from server
			List<Guest> guestList = (List<Guest>) inFromServer.readObject();
			System.out.println("\nGuest List:");
			for (Guest guest : guestList) {
				System.out.println(guest);
			}

		} catch (IOException | ClassNotFoundException e) {
			System.err.println(
					"Error: Could not connect to server. Please check the network connection or server status.");
			e.printStackTrace();
		}
		
	}

	private static void processGetGuestById(String string) {
		InetAddress serverAddress;
		int port = 6789;

		try {
			serverAddress = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			System.err.println("Error: Could not find server. Please check the hostname or IP address.");
			e.printStackTrace();
			return;
		}

		try (Socket connectionSocket = new Socket(serverAddress, port);
				DataOutputStream outToServer = new DataOutputStream(connectionSocket.getOutputStream());
				ObjectInputStream inFromServer = new ObjectInputStream(connectionSocket.getInputStream())) {

			// Send request for guest list
			outToServer.writeBytes("GET_GUEST_BY_ID" + string + "\n");

			// Receive guest list from server
			List<Guest> guestList = (List<Guest>) inFromServer.readObject();
			System.out.println("\nGuest List:");
			for (Guest guest : guestList) {
				System.out.println(guest);
			}

		} catch (IOException | ClassNotFoundException e) {
			System.err.println(
					"Error: Could not connect to server. Please check the network connection or server status.");
			e.printStackTrace();
		}

	}

	private static void processGuestListRequest() {
		InetAddress serverAddress;
		int port = 6789;

		try {
			serverAddress = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			System.err.println("Error: Could not find server. Please check the hostname or IP address.");
			e.printStackTrace();
			return;
		}

		try (Socket connectionSocket = new Socket(serverAddress, port);
				DataOutputStream outToServer = new DataOutputStream(connectionSocket.getOutputStream());
				ObjectInputStream inFromServer = new ObjectInputStream(connectionSocket.getInputStream())) {

			// Send request for guest list
			outToServer.writeBytes("GET_GUEST_LIST\n");

			// Receive guest list from server
			List<Guest> guestList = (List<Guest>) inFromServer.readObject();
			System.out.println("\nGuest List:");
			for (Guest guest : guestList) {
				System.out.println(guest);
			}

		} catch (IOException | ClassNotFoundException e) {
			System.err.println(
					"Error: Could not connect to server. Please check the network connection or server status.");
			e.printStackTrace();
		}
	}

	private static void processCountGuest() {
		InetAddress serverAddress;
		int port = 6789;

		try {
			serverAddress = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			System.err.println("Error: Could not find server. Please check the hostname or IP address.");
			e.printStackTrace();
			return;
		}

		try (Socket connectionSocket = new Socket(serverAddress, port);
				DataOutputStream outToServer = new DataOutputStream(connectionSocket.getOutputStream());
				ObjectInputStream inFromServer = new ObjectInputStream(connectionSocket.getInputStream())) {

			// Send request for guest list
			outToServer.writeBytes("COUNT_GUEST\n");

			int count = (int) inFromServer.readObject();
			System.out.println(count);

		} catch (IOException | ClassNotFoundException e) {
			System.err.println(
					"Error: Could not connect to server. Please check the network connection or server status.");
			e.printStackTrace();
		}
	}
}
