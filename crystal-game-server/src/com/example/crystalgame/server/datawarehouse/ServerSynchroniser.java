package com.example.crystalgame.server.datawarehouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import com.db4o.ObjectContainer;

import com.example.crystalgame.library.datawarehouse.LockManager;
import com.example.crystalgame.library.datawarehouse.Synchronizer;
import com.example.crystalgame.library.instructions.DataSynchronisationInstruction;
import com.example.crystalgame.server.groups.Client;
import com.example.crystalgame.server.groups.Group;

/**
 * An extension of the Synchroniser for the server
 * @author Balazs Pete, Allen Thomas Varghese
 *
 */
public class ServerSynchroniser extends Synchronizer {

	private HashMap<String, LinkedBlockingQueue<DataSynchronisationInstruction>> queues;
	private Group group;
	private LockManager lockManager;
	
	protected ServerSynchroniser(ObjectContainer container, Group group, LockManager lockManager) {
		super(container);
		queues = new HashMap<String, LinkedBlockingQueue<DataSynchronisationInstruction>>();
		this.group = group;
		this.lockManager = lockManager;
	}

	@Override
	public void passInstruction(DataSynchronisationInstruction instruction) {
		switch(instruction.getDataSynchronisationInstructiontype()) {
			case COMMIT_REPLY:
				// Reply to a commit instruction from clients
				handleCommitReply(instruction);
				break;
			case PREPARE_REPLY:
				// Reply to a prepare instruction from clients
				handlePrepareReply(instruction);
				break;
			case DELETE_REQUEST:
				// A request to delete items
				handleDeleteRequest(instruction);
				break;
			case UPDATE_REQUEST:
				// A request to update items
				handleUpdateRequest(instruction);
				break;
			default:
				// Just do nothing...
				break;
		}
	}
	
	@Override
	public LinkedBlockingQueue<DataSynchronisationInstruction> requestTransaction(DataSynchronisationInstruction instruction) {
		// Initiate a transaction for the local client
		LinkedBlockingQueue<DataSynchronisationInstruction> queue = new LinkedBlockingQueue<DataSynchronisationInstruction>();
		// A custom queue, we will send the result of the transaction to this queue
		queues.put(instruction.getTransactionID() + "-client", queue);
		
		// Pretend that this instruction came from a client
		passInstruction(instruction);
		
		return queue;
	}
	
	/**
	 * Get the synchroniser corresponding to the container
	 * @param container The container
	 * @return The synchroniser
	 */
	public static ServerSynchroniser getGameSynchroniser(ObjectContainer container, Group group, LockManager lockManager) {	
		return new ServerSynchroniser(container, group, lockManager);
	}
	
	/**
	 * Allow transactions to send the result to the local client
	 * @param instruction the instruction to send
	 */
	protected void passInstructionToLocal(DataSynchronisationInstruction instruction) {
		putOnQueue(queues.get(instruction.getTransactionID() + "-client"), instruction);
	}
	
	/**
	 * Delete the queues of a transaction
	 * @param transactionID The transaction ID
	 */
	public void removeQueue(String transactionID) {
		queues.remove(transactionID);
		queues.remove(transactionID + "-client");
	}

	private void handleCommitReply(DataSynchronisationInstruction instruction) {
		putOnQueue(queues.get(instruction.getTransactionID()), instruction);
	}
	
	private void handlePrepareReply(DataSynchronisationInstruction instruction) {
		putOnQueue(queues.get(instruction.getTransactionID()), instruction);
	}
	
	private void handleDeleteRequest(DataSynchronisationInstruction instruction) {
		handleRequest(instruction);
	}
	
	private void handleUpdateRequest(DataSynchronisationInstruction instruction) {
		handleRequest(instruction);
	}
	
	private void handleRequest(DataSynchronisationInstruction instruction) {
		// A request, check if a queue already exist, if not create it an initiate a transaction
		LinkedBlockingQueue<DataSynchronisationInstruction> queue = queues.get(instruction.getTransactionID());
		if (queue == null) {
			queue = new LinkedBlockingQueue<DataSynchronisationInstruction>();
			queues.put(instruction.getTransactionID(), queue);
			
			System.out.println("ServerSynchroniser|handleRequest: Creating new transaction. TransactionID=" + instruction.getTransactionID() + " Type=" + instruction.getDataSynchronisationInstructiontype());
			pool.execute(new DataWarehouseServerTransaction(this, queue, container, lockManager, getClientIDs()));
		}

		// Put the instruction on the queue
		putOnQueue(queues.get(instruction.getTransactionID()), instruction);
	}
	
	@Override
	public void cleanUp(String transactionID) {
		removeQueue(transactionID);
	}
	
	private List<String> getClientIDs() {
		// Just convert the client IDs to a list...
		List<String> list = new ArrayList<String>();
		for (Client client : group.getClients()) {
			list.add(client.getId());
		}
		return list;
	}
	
}
