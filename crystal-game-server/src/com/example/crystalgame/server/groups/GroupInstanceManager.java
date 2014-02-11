package com.example.crystalgame.server.groups;

import java.util.concurrent.ConcurrentHashMap;

import com.example.crystalgame.library.communication.messages.ControlMessage;
import com.example.crystalgame.library.events.ControlMessageEventListener;
import com.example.crystalgame.library.events.ListenerManager;
import com.example.crystalgame.library.groups.Client;
import com.example.crystalgame.library.groups.Group;
import com.example.crystalgame.library.groups.GroupException;
import com.example.crystalgame.library.instructions.GroupInstruction;
import com.example.crystalgame.library.instructions.Instruction;
import com.example.crystalgame.library.instructions.InstructionType;

/**
 * A component to manage groups and offer an interface for groups management
 * @author Balazs Pete, Rajan Verma
 *
 */
public class GroupInstanceManager {

	private ListenerManager<ControlMessageEventListener, ControlMessage> manager;
	private ConcurrentHashMap<String, Group> groups;
	
	public GroupInstanceManager() {
		groups = new ConcurrentHashMap<String, Group>();
		
		// Allow components to subscribe to ControlMessageEvents
		manager = new ListenerManager<ControlMessageEventListener, ControlMessage>() {
			@Override
			protected void eventHandlerHelper(ControlMessageEventListener listener, ControlMessage data) {
				// Forward the control message event to the listener
				listener.messageEvent(data);
			}
		};
	}
	
	/**
	 * Create a group with DEFAULT_MAX_PLAYERS max players
	 * @param groupName The desired name for the group
	 * @param initiator The initiating client
	 * @return The created group's ID
	 */
	public String createGroup(String groupName, Client initiator) {
		return createGroup(groupName, Group.DEFAULT_MAX_PLAYERS, initiator);
	}
	
	/**
	 * Create a group with a specified number of max players
	 * @param groupName The desired group name
	 * @param maxPlayers The maximum number of players 
	 * @param initiator The initiating client's ID
	 * @return The group ID
	 */
	public String createGroup(String groupName, int maxPlayers, Client initiator) {
		Group group;
		do {
			group = new Group(groupName, initiator);
			// If another group with the same ID exists, regenerate group
		} while (groups.get(group.groupId) != null);
		
		groups.put(group.groupId, group);
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
		for(Group group : groups.values()) {
			// Determine if other client is in the group
			if (group.isClientInGroup(otherClientId)) {
				if (group.addClient(client)) {
					// If we managed to add the client, return the group ID
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
			group.removeClient(client);
		} catch (GroupException e) {
			deleteGroup(groupId);
		}
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
		groups.remove(id);
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
		switch(instruction.groupInstructionType) {
			case CREATE:
				int maxPlayers = Integer.parseInt(instruction.arguments[1]);
				groupId = this.createGroup(
						instruction.arguments[0], maxPlayers, 
						new Client(message.getSenderId(), instruction.arguments[2]));
				// Return group ID if we have succeeded
				reply = GroupInstruction.successReply(groupId);
				break;
			case JOIN:
				try {
					groupId = this.joinGroup(new Client(message.getSenderId(), instruction.arguments[1]), instruction.arguments[0]);
				} catch (GroupException e) {
					// We have failed, return the error message...
					reply = GroupInstruction.failureReply(e.getMessage());
				}
				// Return group ID if we have succeeded
				reply = GroupInstruction.successReply(groupId);
				break;
			case LEAVE:
				Group group = groups.get(message.getGroupId());
				this.leaveGroup(group.groupId, group.getClient(message.getSenderId()));
				// Just return a blank success message
				reply = GroupInstruction.successReply(null);
				break;
			default: 
				reply = GroupInstruction.failureReply("Unsupported group command!");
				break;
		}
		
		ControlMessage replyMessage = new ControlMessage();
		replyMessage.setReceiverId(message.getSenderId());
		replyMessage.setData(reply);
		
		manager.send(replyMessage);
	}
	
	/**
	 * Add a control message event listener
	 * @param listener The listener
	 */
	public void addControlMessageEventListener(ControlMessageEventListener listener) {
		manager.addEventListener(listener);
	}
	
	/**
	 * Remove a control message event listener
	 * @param listener The listener
	 */
	public void removeControlMessageEventListener(ControlMessageEventListener listener) {
		manager.removeEventListener(listener);
	}
}
