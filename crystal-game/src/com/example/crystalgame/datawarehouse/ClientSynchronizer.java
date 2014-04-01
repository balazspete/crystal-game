package com.example.crystalgame.datawarehouse;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

import com.db4o.ObjectContainer;
import com.example.crystalgame.library.datawarehouse.LockManager;
import com.example.crystalgame.library.datawarehouse.Synchronizer;
import com.example.crystalgame.library.instructions.DataSynchronisationInstruction;

/**
 * An extension of the synchroniser for the client
 * @author Balazs Pete, Allen Thomas Varghese
 *
 */
public class ClientSynchronizer extends Synchronizer {

	private HashMap<String, LinkedBlockingQueue<DataSynchronisationInstruction>> queues;
	private String myID;
	private LockManager lockManager;
	
	protected ClientSynchronizer(ObjectContainer dataStore, LockManager lockManager, String myID) {
		super(dataStore);
		queues = new HashMap<String, LinkedBlockingQueue<DataSynchronisationInstruction>>();
		this.lockManager = lockManager;
		this.myID = myID;
	}

	@Override
	public void passInstruction(DataSynchronisationInstruction instruction) {
		switch (instruction.getDataSynchronisationInstructiontype()) {
		case PREPARE:
			// A request to prepare for commit (have to try to modify the resources)
			handlePrepareInstruction(instruction);
			break;
		case COMMIT:
			// A request to finalise the transaction
			handleCommitInstruction(instruction);
			break;
		default:
			// Just do nothing...
			break;
		}
	}
	
	@Override
	public LinkedBlockingQueue<DataSynchronisationInstruction> requestTransaction(
			DataSynchronisationInstruction instruction) {

		// Request a transaction from the server
		sendInstruction(instruction);

		// Create a special queue for the local client where we will send the result
		LinkedBlockingQueue<DataSynchronisationInstruction> queue = new LinkedBlockingQueue<DataSynchronisationInstruction>();
		queues.put(instruction.getTransactionID() + "-client", queue);
		
		return queue;
	}
	
	private void handlePrepareInstruction(DataSynchronisationInstruction instruction) {
		// A new transaction request! Create a queue...
		LinkedBlockingQueue<DataSynchronisationInstruction> queue = new LinkedBlockingQueue<DataSynchronisationInstruction>();
		queues.put(instruction.getTransactionID(), queue);

		// Put the instruction on the queue
		putOnQueue(queues.get(instruction.getTransactionID()), instruction);
		
		// Initiate the transaction
		pool.execute(new ClientDataWarehouseTransaction(this, queue, container, lockManager, myID));
	}

	private void handleCommitInstruction(final DataSynchronisationInstruction instruction) {
		// A commit request, send the instruction to the thread
		putOnQueue(queues.get(instruction.getTransactionID()), instruction);
	}

	@Override
	public void cleanUp(String transactionID) {
		queues.remove(transactionID);
		queues.remove(transactionID + "-client");
	}

	/**
	 * Allow transactions to send the result to the local client
	 * @param instruction the instruction to send
	 */
	protected void passInstructionToLocal(DataSynchronisationInstruction instruction) {
		putOnQueue(queues.get(instruction.getTransactionID() + "-client"), instruction);
	}
}
