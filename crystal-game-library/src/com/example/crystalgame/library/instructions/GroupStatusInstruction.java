package com.example.crystalgame.library.instructions;

import java.io.Serializable;
import java.util.Map;

/**
 * An instruction to retrieve or forward information regarding a group's status
 * @author Balazs Pete, Shen Chen
 *
 */
public class GroupStatusInstruction extends Instruction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4539411033702746584L;
	public enum GroupStatusInstructionType implements Serializable  {
		MEMBER_LIST_REQUEST, MEMBER_LIST_RESPONSE
	}
	
	public final GroupStatusInstructionType type;
	
	/**
	 * Create a new group status instruction
	 * @param type The type of the instruction
	 * @param arguments the arguments
	 */
	private GroupStatusInstruction(GroupStatusInstructionType type, Serializable... arguments) {
		super(InstructionType.GROUP_STATUS_INSTRUCTION, arguments);
		this.type = type;
	}
	
	/**
	 * Create a membership list request
	 * @return The instruction
	 */
	public static GroupStatusInstruction createGroupMembershipListRequestIntruction() {
		return new GroupStatusInstruction(GroupStatusInstructionType.MEMBER_LIST_REQUEST);
	}
	
	/**
	 * Create a membership list response
	 * @param data The membership list in <ClientID, ClientAlias> format
	 * @return The instruction
	 */
	public static GroupStatusInstruction createGroupMembershipListResponseInstruction(Map<String, String> data) {
		String[] args = new String[data.size()*2];
		int i = 0;
		for(String key : data.keySet()) {
			args[i++] = key;
			args[i++] = data.get(key);
		}
		
		return createGroupMembershipListResponseInstruction(args);
	}
	
	/**
	 * Create a membership list response
	 * @param args The client list in client1ID, client1Alias, client2ID, client2Alias, ... format
	 * @return The instruction
	 */
	public static GroupStatusInstruction createGroupMembershipListResponseInstruction(String[] args) {
		return new GroupStatusInstruction(GroupStatusInstructionType.MEMBER_LIST_RESPONSE, args);
	}
	
}
