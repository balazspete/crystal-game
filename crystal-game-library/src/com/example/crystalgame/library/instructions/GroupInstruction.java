package com.example.crystalgame.library.instructions;

import java.io.Serializable;
import java.util.Map;

import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.library.instructions.GroupStatusInstruction.GroupStatusInstructionType;

/**
 * A group control instruction
 * @author Balazs Pete, Rajan Verma
 *
 */
public class GroupInstruction extends Instruction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5556039251569433592L;

	/**
	 * The type of a {@link GroupInstruction}
	 * @author Balazs Pete, Rajan Verma
	 *
	 */
	public enum GroupInstructionType implements Serializable {
		CREATE, JOIN, LEAVE,ALLMEMBER, MEMBER_LIST_RESPONSE,
		SUCCESS, FAILURE,
		IS_MEMBER, IS_MEMBER_REPLY
	}
	
	public final GroupInstructionType groupInstructionType;
	
	/**
	 * Create a group control instruction
	 * @param type The instruction type
	 * @param arguments The arguments
	 */
	private GroupInstruction(GroupInstructionType type, Serializable... arguments) {
		super(InstructionType.GROUP_INSTRUCTION, arguments);
		this.groupInstructionType = type;
	}
	
	/**
	 * 
	 * Create a group CREATE instruction
	 * @param groupName The desired group name
	 * @param maxPlayers The desired number of maximum players
	 * @param playerAlias The player's alias
	 * @param upperLeft First corner of the game location
	 * @param upperRight Second corner of the game location
	 * @param lowerRight Third corner of the game location
	 * @param lowerLeft Fourth corner of the game location
	 * @return The instruction
	 */
	public static GroupInstruction createGroup(String groupName, int maxPlayers, String playerAlias, Location upperLeft, Location upperRight, Location lowerRight, Location lowerLeft) {
		return new GroupInstruction(GroupInstructionType.CREATE, groupName, ""+maxPlayers, playerAlias, upperLeft, upperRight, lowerRight, lowerLeft);
	}
	
	/**
	 * Create a group JOIN instruction
	 * @param clientId The client's ID
	 * @param playerAlias The player's alias
	 * @return The instruction
	 */
	public static GroupInstruction joinGroup(String clientId, String playerAlias) {
		return new GroupInstruction(GroupInstructionType.JOIN, clientId, playerAlias);
	}
	
	/**
	 * Create a group LEAVE instruction
	 * @return The instruction
	 */
	public static GroupInstruction leaveGroup() {
		return new GroupInstruction(GroupInstructionType.LEAVE);
	}
	
	/**
	 * Create a SUCCESS instruction reply
	 * @param groupId The group ID
	 * @return The instruction
	 */
	public static GroupInstruction successReply(String groupId) {
		return new GroupInstruction(GroupInstructionType.SUCCESS, groupId);
	}
	
	/**
	 * Create a FAILURE instruction reply
	 * @param message The error message
	 * @return The instruction
	 */
	public static GroupInstruction failureReply(String message) {
		return new GroupInstruction(GroupInstructionType.FAILURE, message);
	}
	
	/**
	 * Create group membership list request instruction
	 * @return the instruction
	 */
	public static GroupInstruction createMembershipListRequestInstruction(){
		return new GroupInstruction(GroupInstructionType.ALLMEMBER);
	}
	
	/**
	 * Create a group membership list instruction reply
	 * @param data The client data 
	 * @return The instruction
	 */
	public static GroupInstruction createMembershipListResponseInstruction(Map<String, String> data) 
	{
		String[] args = new String[data.size()*2];
		int i = 0;
		for(String key : data.keySet()) 
		{
			args[i++] = key;
			args[i++] = data.get(key);
		}
		
		return new GroupInstruction(GroupInstructionType.MEMBER_LIST_RESPONSE, args);
	}
	
	/**
	 * Create a query to determine if client is a member of the specified group
	 * @param groupID The group ID
	 * @return The instruction
	 */
	public static GroupInstruction createIsMemberQueryInstruction(String groupID) {
		return new GroupInstruction(GroupInstructionType.IS_MEMBER, groupID);
	}
	
	/**
	 * Create a response to an IS_MEMBER query
	 * @param response The response value, true if is member
	 * @return The instruction
	 */
	public static GroupInstruction createIsMemberReplyInstruction(boolean response) {
		return new GroupInstruction(GroupInstructionType.IS_MEMBER_REPLY, response);
	}
	
}
