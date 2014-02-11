package com.example.crystalgame.library.groups;

import java.util.List;
import java.util.ArrayList;

import com.example.crystalgame.library.util.RandomID;

/**
 * An object describing a group
 * @author Balazs Pete, Rajan Verma
 *
 */
public class Group {

	public static final int DEFAULT_MAX_PLAYERS = 20;
	public final String groupId;
	
	private String name;
	private int maxPlayers = -1;
	
	private List<Client> clients;
	
	/**
	 * Create a group with a name
	 * @param name The name of the group
	 */
	public Group(String name, Client client) {
		this(name, DEFAULT_MAX_PLAYERS, client);
	}
	
	/**
	 * Create a group with a name and a specified max number of players
	 * @param name The name of the group
	 * @param maxPlayers The maximum number of players
	 */
	public Group(String name, int maxPlayers, Client client) {
		groupId = RandomID.getRandomId();
		this.name = name;
		this.maxPlayers = maxPlayers;
		
		clients = new ArrayList<Client>(maxPlayers);
		clients.add(client);
	}
	
	/**
	 * Get the name of the group
	 * @return The name of the group
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Get the maximum number of players
	 * @return the maximum number of players
	 */
	public int getMaxPlayers() {
		return maxPlayers < 0 ? DEFAULT_MAX_PLAYERS : maxPlayers;
	}
	
	/**
	 * Add a client to the group
	 * @param client The client to add
	 * @return True if added
	 */
	public synchronized boolean addClient(Client client) {
		if(clients.size() > maxPlayers) {
			return false;
		}
		
		return clients.add(client);
	}
	
	/**
	 * Remove a client from the group
	 * @param client The client to remove
	 * @return True if removed
	 * @throws GroupException Thrown if client is the last member of the group
	 */
	public synchronized boolean removeClient(Client client) throws GroupException {
		if (clients.size() == 1) {
			// We are not allowing a member to leave the group if he/she is the last one
			throw GroupException.LAST_MEMBER;
		}
		
		return clients.remove(client);
	}
	
	/**
	 * Get the client with the specified ID
	 * @param id The id of the client
	 * @return The client or null
	 */
	public synchronized Client getClient(String id) {
		String _id = id.intern();
		for(Client client : clients) {
			if(client.getId() == _id) {
				return client;
			}
		}
		return null;
	}
	
	/**
	 * Determine if the client is in the group
	 * @param id The id of the client
	 * @return True if in the group, false otherwise
	 */
	public synchronized boolean isClientInGroup(String id) {
		String _id = id.intern();
		for(Client client : clients) {
			if(client.getId() == _id) {
				return true;
			}
		}
		return false;
	}
}
