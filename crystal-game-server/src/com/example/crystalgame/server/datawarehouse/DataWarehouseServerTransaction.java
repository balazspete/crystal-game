package com.example.crystalgame.server.datawarehouse;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import com.db4o.ObjectContainer;
import com.example.crystalgame.library.data.HasID;
import com.example.crystalgame.library.datawarehouse.DB4OInterface;
import com.example.crystalgame.library.datawarehouse.DataWarehouseTransaction;
import com.example.crystalgame.library.datawarehouse.LockManager;
import com.example.crystalgame.library.datawarehouse.Synchronizer;
import com.example.crystalgame.library.instructions.DataSynchronisationInstruction;

/**
 * A DW transaction coordinator
 * @author Balazs Pete, Allen Thomas varghese
 *
 */
public class DataWarehouseServerTransaction extends DataWarehouseTransaction {

	private enum State {
		INITIAL, PREPARED, CONFIRMED
	}
	
	private DataSynchronisationInstruction last;
	private boolean running = true;
	private HashMap<String, State> clientMap;
	private int count;
	
	private boolean shouldCommit;
	
	private DB4OInterface store;
	
	private State myState;
	
	public DataWarehouseServerTransaction(Synchronizer synchronizer, 
			LinkedBlockingQueue<DataSynchronisationInstruction> queue, 
			ObjectContainer container,
			LockManager lockManager,
			List<String> clientIDs) {
		super(synchronizer, queue, container, lockManager);
		clientMap = new HashMap<String, State>();
		for (String id : clientIDs) {
			clientMap.put(id, State.INITIAL);
		}
		count = 0;
		myState = State.INITIAL;
	}
	
	@Override
	public void run() {
		while (running) {
			try {
				// Listen to instructions
				DataSynchronisationInstruction instruction = queue.poll(Long.MAX_VALUE, TimeUnit.DAYS);
				
				switch (instruction.getDataSynchronisationInstructiontype()) {
					case COMMIT_REPLY:
						// Reply to a COMMIT instruction from clients
						completeTransaction(instruction);
						break;
					case PREPARE_REPLY:
						// Reply to a PREPARE instruction from clients
						commitTransaction(instruction);
						break;
					case DELETE_REQUEST:
						// A request to delete
						handleDeleteRequest(instruction);
						break;
					case UPDATE_REQUEST:
						// A request to update
						handleUpdateRequest(instruction);
						break;
					default:
						// Ignore
						break;
				}

			} catch (InterruptedException e) {
				// Try again...
			}
		}
		
		// Clean up when done..
		cleanUp();
	}

	private void handleDeleteRequest(DataSynchronisationInstruction instruction) {
		if (myState != State.INITIAL) {
			// Don't accept the instruction, if we are not in the right state...
			return;
		}
		
		// Get an abstraction for the container
		store = new DB4OInterface(lockManager, container);
		
		// Get the type of the wrapped data (first argument is the type)
		String type = (String) instruction.arguments[0];
		
		String[] data = (String[]) instruction.arguments[1];
		
		System.out.println("Starting transaction to UPDATE " + data.length + " items. Type=" + type + " TransactionID=" + instruction.getTransactionID());
		
		boolean success = true;
		
		// Store the transmitted IDs (second to n arguments are the data items)
		for (int i = 0; i < data.length; i++) {
			success = success && store.delete(type, (String) data[i]); 
		}
		
		shouldCommit = success;
		
		// Forward the instruction to the clients
		last = DataSynchronisationInstruction.createPrepareInstruction(instruction.getTransactionID(), instruction);
		sendInstruction(last);
		
		// Change the state
		myState = State.PREPARED;
	}

	private void handleUpdateRequest(DataSynchronisationInstruction instruction) {
		if (myState != State.INITIAL) {
			// Don't accept the instruction, if we are not in the right state...
			return;
		}
		
		// Get an abstraction for the container
		store = new DB4OInterface(lockManager, container);
		
		// Get the type of the wrapped data (first argument is the type)
		String type = (String) instruction.arguments[0];
		
		HasID[] data = (HasID[]) instruction.arguments[1];

		System.out.println("Starting transaction to UPDATE " + data.length + " items. Type=" + type + " TransactionID=" + instruction.getTransactionID());
		
		boolean success = true;
		
		// Store the transmitted data items (second to n arguments are the data items)
		for (int i = 0; i < data.length; i++) {
			success = success && store.put(type, (HasID) data[i]) !=  null; 
		}
		
		shouldCommit = success;
		
		// Forward the instruction to the clients
		last = DataSynchronisationInstruction.createPrepareInstruction(instruction.getTransactionID(), instruction);
		sendInstruction(last);
		
		// Change the state
		myState = State.PREPARED;
	}
	
	private void commitTransaction(DataSynchronisationInstruction instruction) {
		if (myState != State.PREPARED) {
			// Don't accept the instruction, if we are not in the right state...
			return;
		}
		
		System.out.println("commit"+instruction.getDataSynchronisationInstructiontype());
		
		// Let's get the details contained in the instruction
		// For formatting, check the class
		boolean commitResult = (Boolean) instruction.arguments[0];
		String clientID = (String) instruction.arguments[1];
		
		// Let's get the current state of the client so that we can tell if it has talked to us before (in this state)
		State state = clientMap.get(clientID);

		boolean commit = shouldCommit;
		if (shouldCommit && commitResult) {
			// Client replied positively and is now in the prepraed state
			if (state != State.PREPARED) {
				// If the client has not checked in before, update status and count
				clientMap.put(clientID, State.PREPARED);
				count++;
			}
			
			if (count < clientMap.size()) {
				// If not all clients have replied, end here...
				return;
			}
		
			// Okay, so everyone replied yes, commit the changes...
			container.commit();
			commit = true;
		} else {
			// Client replied abort, we are going to abort the transaction
			// No need to wait for further messages
			container.rollback();
		}
		
		store.releaseLocks();
		
		// Send a commit instruction with the result (true: commit, false: abort)
		last = DataSynchronisationInstruction.createCommitInstruction(instruction.getTransactionID(), commit);
		sendInstruction(last);
		
		System.out.println("DataWarehouseServerTransaction|commitTransaction: Finishing up transaction. Result=" + commit + " TransactionID=" + instruction.getTransactionID());
		
		// Send result to local client
		((ServerSynchroniser) synchronizer).passInstructionToLocal(last);
		
		// We are going to the next state, reset the counter...
		count = 0;
		myState = State.CONFIRMED;
	}
	
	private void completeTransaction(DataSynchronisationInstruction instruction) {
		if (myState != State.CONFIRMED) {
			// Don't accept the instruction, if we are not in the right state...
			return;
		}
		
		// Let's get the client ID out from the instruction
		String clientID = (String) instruction.arguments[0];
		
		// Retrieve the client's state so we can tell if it has talked to us in this state before
		State state = clientMap.get(clientID);
		
		if (state != State.CONFIRMED) {
			// If the client has not checked in before, update status and count
			clientMap.put(clientID, State.CONFIRMED);
			count++;
		}
		
		if (count < clientMap.size()) {
			// If not all clients have replied, end here...
			return;
		}
		
		// We managed to get here, so that means that all clients have confirmed the commit/abort
		// Do some clean up and stop the thread
		synchronizer.cleanUp(instruction.getTransactionID());
		running = false;
	}
	
}
