package com.example.crystalgame.library.instructions;

import java.io.Serializable;

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
		CREATE, JOIN, LEAVE, 
		SUCCESS, FAILURE
	}
	
	public final GroupInstructionType groupInstructionType;
	
	/**
	 * Create a group control instruction
	 * @param type The instruction type
	 * @param arguments The arguments
	 */
	private GroupInstruction(GroupInstructionType type, String... arguments) {
		super(InstructionType.GROUP_INSTRUCTION, arguments);
		this.groupInstructionType = type;
	}
	
	/**
	 * Create a group CREATE instruction
	 * @param groupName The desired group name
	 * @param maxPlayers The desired number of maximum players
	 * @param playerAlias The player's alias
	 * @return The instruction
	 */
	public static GroupInstruction createGroup(String groupName, int maxPlayers, String playerAlias) {
		return new GroupInstruction(GroupInstructionType.CREATE, groupName, ""+maxPlayers, playerAlias);
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
		return new GroupInstruction(GroupInstructionType.LEAVE, new String());
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
}
