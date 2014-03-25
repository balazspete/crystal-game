package com.example.crystalgame.library.instructions;

import java.io.Serializable;
import java.util.List;

import com.example.crystalgame.library.data.HasID;
import com.example.crystalgame.library.util.RandomID;

/**
 * An instruction to control data synchronisation
 * @author Balazs Pete, Allen Thomas Varghese
 *
 */
public class DataSynchronisationInstruction extends Instruction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8688057254275807103L;

	public enum DataSynchronisationInstructionType implements Serializable {
		UPDATE_REQUEST, DELETE_REQUEST,
		PREPARE, PREPARE_REPLY, 
		COMMIT, COMMIT_REPLY
	}
	
	private DataSynchronisationInstructionType dataSyncInstructionType;
	private String transactionID;
	
	private DataSynchronisationInstruction(DataSynchronisationInstructionType dataSyncInstructionType, String transactionID, Serializable... arguments) {
		super(InstructionType.DATA_SYNCRONISATION, arguments);
		this.dataSyncInstructionType = dataSyncInstructionType;
		this.transactionID = transactionID;
	}

	/**
	 * Get the instruction type
	 * @return The type
	 */
	public DataSynchronisationInstructionType getDataSynchronisationInstructiontype() {
		return dataSyncInstructionType;
	}
	
	/**
	 * Get the transaction ID
	 * @return The ID
	 */
	public String getTransactionID() {
		return transactionID;
	}
	
	/**
	 * Create a request to update a data entry
	 * @param data The entry
	 * @return The instruction
	 */
	public static DataSynchronisationInstruction createUpdateRequestInstruction(@SuppressWarnings("rawtypes") Class type, List<HasID> values) {
		return new DataSynchronisationInstruction(DataSynchronisationInstructionType.UPDATE_REQUEST, RandomID.getRandomId(), type.toString(), values.toArray(new HasID[0]));
	}
	
	/**
	 * Create a request to delete a data entry
	 * @param data The entry
	 * @return The instruction
	 */
	public static DataSynchronisationInstruction createDeleteRequestInstruction(@SuppressWarnings("rawtypes") Class type, List<String> ids) {
		return new DataSynchronisationInstruction(DataSynchronisationInstructionType.DELETE_REQUEST, RandomID.getRandomId(), type.toString(), ids.toArray(new String[0]));
	}
	
	/**
	 * Create a transaction prepare instruction to be sent to all clients
	 * @param transactionID The ID of the transaction
	 * @param instruction The instruction to process
	 * @return The instruction
	 */
	public static DataSynchronisationInstruction createPrepareInstruction(String transactionID, DataSynchronisationInstruction instruction) {
		return new DataSynchronisationInstruction(DataSynchronisationInstructionType.PREPARE, transactionID, instruction);
	}
	
	/**
	 * Create a transaction commit instruction to be sent to all clients
	 * @param instruction The transaction ID
	 * @return The instruction
	 */
	public static DataSynchronisationInstruction createCommitInstruction(String transactionID, Boolean commit) {
		return new DataSynchronisationInstruction(DataSynchronisationInstructionType.COMMIT, transactionID, commit);
	}
	
	/**
	 * Create a reply to a prepare instruction
	 * @param transactionID The transaction ID
	 * @param success True if prepared successfully
	 * @return The instruction
	 */
	public static DataSynchronisationInstruction createPrepareInstructionReply(String transactionID, String clientID, Boolean success) {
		return new DataSynchronisationInstruction(DataSynchronisationInstructionType.PREPARE_REPLY, transactionID, success, clientID);
	}
	
	/**
	 * Create a reply to a commit instruction
	 * @param transactionID The transaction ID
	 * @return The instruction
	 */
	public static DataSynchronisationInstruction createCommitInstructionReply(String transactionID, String clientID) {
		return new DataSynchronisationInstruction(DataSynchronisationInstructionType.COMMIT_REPLY, transactionID, clientID);
	}
}
