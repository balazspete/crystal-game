package com.example.crystalgame.server.groups;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.crystalgame.library.communication.messages.ControlMessage;
import com.example.crystalgame.library.communication.messages.GroupStatusMessage;
import com.example.crystalgame.library.communication.messages.InstructionRelayMessage;
import com.example.crystalgame.library.communication.messages.Message;
import com.example.crystalgame.library.communication.messages.MulticastMessage;
import com.example.crystalgame.library.communication.messages.UnicastMessage;
import com.example.crystalgame.library.data.GameBoundary;
import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.library.events.ListenerManager;
import com.example.crystalgame.library.events.MessageEvent;
import com.example.crystalgame.library.events.MessageEventListener;
import com.example.crystalgame.library.instructions.GroupInstruction;
import com.example.crystalgame.library.instructions.GroupStatusInstruction;
import com.example.crystalgame.library.instructions.Instruction;
import com.example.crystalgame.library.instructions.InstructionType;

/**
 * A component to manage groups and offer an interface for groups management
 * @author Balazs Pete, Rajan Verma
 *
 */
public class GroupInstanceManager {

	public final int MAX_GROUPS = 10;
	
	private ListenerManager<MessageEventListener, MessageEvent> messageEventManager;
	private ConcurrentHashMap<String, Group> groups;
	private ConcurrentHashMap<String, Client> clients;
	
	private ExecutorService groupInstancePool;
	private MessageEventListener groupMessageEventListener;
	
	public GroupInstanceManager() {
		groups = new ConcurrentHashMap<String, Group>();
		clients = new ConcurrentHashMap<String, Client>();
		groupInstancePool = Executors.newFixedThreadPool(MAX_GROUPS);
		
		// Allow components to subscribe to ControlMessageEvents
		messageEventManager = new ListenerManager<MessageEventListener, MessageEvent>() {

			@Override
			// Forward the message event to the listener
			protected void eventHandlerHelper(MessageEventListener listener, MessageEvent data) {
				listener.onMessageEvent(data);
			}
		};
	}
	
	/**
	 * Create a group with DEFAULT_MAX_PLAYERS max players
	 * @param groupName The desired name for the group
	 * @param initiator The initiating client
	 * @return The created group's ID
	 * @throws GroupException Thrown in case the maximum limit for the number of groups has been reached
	 */
	public String createGroup(String groupName, Client initiator, GameBoundary boundary) throws GroupException {
		return createGroup(groupName, Group.DEFAULT_MAX_PLAYERS, initiator, boundary);
	}
	
	/**
	 * Create a group with a specified number of max players
	 * @param groupName The desired group name
	 * @param maxPlayers The maximum number of players 
	 * @param initiator The initiating client's ID
	 * @return The group ID
	 * @throws GroupException Thrown in case the maximum limit for the number of groups has been reached
	 */
	public String createGroup(String groupName, int maxPlayers, Client initiator, GameBoundary boundary) throws GroupException {
		if (groups.size() >= MAX_GROUPS) {
			throw GroupException.CANNOT_CREATE_LIMIT;
		}
		
		Client client = getClient(initiator.getId());
		if(client != null && getGroup(client.getGroupID()) != null) {
			throw GroupException.ALREADY_IN_GROUP;
		}
		
		Group group;
		do {
			group = new Group(groupName, initiator, boundary);
			// If another group with the same ID exists, regenerate group
		} while (groups.get(group.groupId) != null);
		
		initiator.setGroupID(group.groupId);
		clients.put(initiator.getId(), initiator);
		groups.put(group.groupId, group);
		
		group.getGroupInstance().addMessageEventListener(groupMessageEventListener);
		
		groupInstancePool.execute(group.getGroupInstance());
		
		return group.groupId;
	}

	/**
	 * Join a group
	 * @param client The client wishing to join a group
	 * @param otherClientId A client already contained in the group
	 * @return The group ID
	 * @throws GroupException Thrown if join failed
	 */
	public String joinGroup(Client client, String otherClientId) throws GroupException {
		Client _client = getClient(client.getId());
		if(_client != null && getGroup(_client.getGroupID()) != null) {
			throw GroupException.ALREADY_IN_GROUP;
		}
		
		for(Group group : groups.values()) {
			// Determine if other client is in the group
			if (group.isClientInGroup(otherClientId)) {
				if (group.addClient(client)) {
					// If we managed to add the client, return the group ID
					client.setGroupID(group.groupId);
					clients.put(client.getId(), client);
					return group.groupId;
				} else {
					// We failed to add the client
					throw GroupException.CANNOT_JOIN;
				}
			}
		}
		
		// If we're here, either the group ID or the other client ID does not exist
		throw GroupException.NO_SUCH_GROUP;
	}
	
	/**
	 * Leave a group
	 * @param groupId The OD of the group to leave
	 * @param client The client wishing to leave the group
	 */
	public void leaveGroup(String groupId, Client client) {
		Group group = groups.get(groupId);
		try {
			GroupInstance instance = group.getGroupInstance();
			if (instance != null) {
				instance.removeClientFromGame(client.getId());
			}
			
			group.removeClient(client);
		} catch (GroupException e) {
			deleteGroup(groupId);
		}
		
		clients.remove(client.getId());
		client.setGroupID(null);
	}
	
	public Client getClient(String id) {
		return clients.get(id);
	}
	
	/**
	 * Get a group
	 * @param id The ID of the group
	 * @return The group
	 */
	public Group getGroup(String id) {
		if (id == null) {
			return null;
		}
		
		return groups.get(id);
	}
	
	/**
	 * Delete a group
	 * @param id The ID of the group to delete
	 */
	public void deleteGroup(String id) {
		groups.remove(id).getGroupInstance().stopInstance();
	}

	/**
	 * Process the input control message
	 * @param message The control message to process
	 */
	public void processControlMessage(ControlMessage message) {
		Instruction instruction;
		instruction = (Instruction) message.getData();

		if (instruction.type == InstructionType.GROUP_INSTRUCTION) {
			handleGroupInstruction(message, (GroupInstruction) instruction);
		}
	}
	
	/**
	 * Handle group control instructions
	 * @param message The control message
	 * @param instruction The group control instruction
	 */
	public void handleGroupInstruction(ControlMessage message, GroupInstruction instruction) {
		String groupId = null;
		GroupInstruction reply = null;
		Group group = null;
		switch(instruction.groupInstructionType) {
		 	case ALLMEMBER:
				Map<String, String> membersList = new HashMap<String, String>();
		 		for(Client client : clients.values())
		 		{
		 			membersList.put(client.getId(), client.getName());
		 		}
		 		//Return member list if succeeded
		 		reply = GroupInstruction.createMembershipListResponseInstruction(membersList);
		 		break;
		 	case IS_MEMBER:
		 		String groupID = (String) instruction.arguments[0];
		 		group = groups.get(groupID);
		 		
		 		boolean result = false;
		 		if (group != null) {
		 			result = group.isClientInGroup(message.getSenderId());
		 		}
		 		
		 		reply = GroupInstruction.createIsMemberReplyInstruction(result);
		 		break;
			case CREATE:
				int maxPlayers = Integer.parseInt((String) instruction.arguments[1]);
				try {
					ArrayList<Location> locations = new ArrayList<Location>();
					locations.add((Location) instruction.arguments[3]);
					locations.add((Location) instruction.arguments[4]);
					locations.add((Location) instruction.arguments[5]);
					locations.add((Location) instruction.arguments[6]);
					
					GameBoundary gameBoundary = new GameBoundary(locations);
					groupId = this.createGroup(
							(String) instruction.arguments[0], maxPlayers, 
							new Client(message.getSenderId(), (String) instruction.arguments[2]),
							gameBoundary);
					// Return group ID if we have succeeded
					reply = GroupInstruction.successReply(groupId);
				} catch (GroupException e) {
					// Thrown if the maximum group number has been reached
					reply = GroupInstruction.failureReply(e.getMessage());
				}
				break;
			case JOIN:
				try {
					groupId = this.joinGroup(new Client(message.getSenderId(), (String) instruction.arguments[0]), (String) instruction.arguments[1]);
					// Return group ID if we have succeeded
					reply = GroupInstruction.successReply(groupId);
				} catch (GroupException e) {
					// We have failed, return the error message...
					reply = GroupInstruction.failureReply(e.getMessage());
				}
				break;
			case LEAVE:
				groupId = message.getGroupId();
				System.out.println("GID:"+groupId);
				if(groupId!=null) {
					group = groups.get(message.getGroupId());
					// Remove player from group
					if (group != null) {
						this.leaveGroup(group.groupId, group.getClient(message.getSenderId()));
					}
				}
				// Just return a blank success message (even if client was not in a group)
				reply = GroupInstruction.successReply(null);
				break;
			default: 
				reply = GroupInstruction.failureReply("Unsupported group command!");
				break;
		}
		
		System.out.println("Group ID: " + groupId);
		System.out.println("Client ID: " + message.getSenderId());
		
		
		InstructionRelayMessage replyMessage = new InstructionRelayMessage(message.getSenderId());
		replyMessage.setData(reply);
		
		MessageEvent event = new MessageEvent(replyMessage);
		event.setReceiverId(message.getSenderId());
		messageEventManager.send(event);
	}
	
	/**
	 * Add a control message event listener
	 * @param listener The listener
	 */
	public void addMessageEventListener(MessageEventListener listener) {
		messageEventManager.addEventListener(listener);
	}
	
	/**
	 * Remove a control message event listener
	 * @param listener The listener
	 */
	public void removeMessageEventListener(MessageEventListener listener) {
		messageEventManager.removeEventListener(listener);
	}
	
	/**
	 * Add a sequencer event listener
	 * @param listener The listener
	 */
	public void setGroupMessageEventListener(MessageEventListener listener) {
		this.groupMessageEventListener = listener;
	}
	
	/**
	 * Remove a sequencer event listener
	 * @param listener The listener
	 */
	public void removeGroupMessageEventListener() {
		for (Group group : groups.values()) {
			group.getGroupInstance().removeMessageEventListener(groupMessageEventListener);
		}
		
		this.groupMessageEventListener = null;
	}
	
	/**
	 * Forward the input message to the group it belongs to
	 * @param message
	 */
	public void forwardMessage(Message message) {
		if (message == null) {
			return;
		}
		
		String groupId = message.getGroupId();
		if(groupId == null) {
			return;
		}
		
		Group group = groups.get(groupId);
		if(group == null) {
			return;
		}
		
		if (message.isMulticastMessage()) {
			group.getGroupInstance().sendMessageToAll((MulticastMessage) message);
		} else {
			group.getGroupInstance().sendMessageToOne((UnicastMessage) message);
		}	
	}
	
	/**
	 * method to handle {@link GroupStatusMessage}s
	 * @param message The message
	 */
	public void handleGroupStatusMessage(GroupStatusMessage message) {
		GroupStatusInstruction instruction = (GroupStatusInstruction) message.getData();
		GroupStatusInstruction reply = null;
		switch(instruction.type) {
			case MEMBER_LIST_REQUEST :
				String[] args = new String[clients.size()*2];
				int i = 0;
				for(Client client : clients.values()) {
					args[i++] = client.getId();
					args[i++] = client.getName();
				}
				reply = GroupStatusInstruction.createGroupMembershipListResponseInstruction(args);
				break;
			default:
				break;
		}
		
		GroupStatusMessage replyMessage = new GroupStatusMessage(message.getSenderId());
		replyMessage.setData(reply);
		
		MessageEvent event = new MessageEvent(replyMessage);
		event.setReceiverId(message.getSenderId());
		messageEventManager.send(event);
	}
}
