package com.example.crystalgame.datawarehouse;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.db4o.ObjectContainer;
import com.example.crystalgame.library.data.HasID;
import com.example.crystalgame.library.datawarehouse.DB4OInterface;
import com.example.crystalgame.library.datawarehouse.DataWarehouseTransaction;
import com.example.crystalgame.library.datawarehouse.LockManager;
import com.example.crystalgame.library.datawarehouse.Synchronizer;
import com.example.crystalgame.library.instructions.DataSynchronisationInstruction;
import com.example.crystalgame.library.instructions.DataSynchronisationInstruction.DataSynchronisationInstructionType;

/**
 * An extension of the DW transaction for client nodes
 * @author Balazs Pete
 *
 */
public class ClientDataWarehouseTransaction extends DataWarehouseTransaction {

	private enum State {
		BEGIN, PREPARED, DONE
	}
	
	private State myState = State.BEGIN;
	private boolean running = true;
	private String myID;
	
	private DB4OInterface store;
	
	public ClientDataWarehouseTransaction(Synchronizer synchronizer,
			LinkedBlockingQueue<DataSynchronisationInstruction> queue,
			ObjectContainer container,
			LockManager lockManager,
			String myID) {
		super(synchronizer, queue, container, lockManager);
		this.myID = myID;  
	}

	@Override
	public void run() {
		while (running) {
			try {
				DataSynchronisationInstruction instruction = queue.poll(Long.MAX_VALUE, TimeUnit.DAYS);
				switch (instruction.getDataSynchronisationInstructiontype()) {
					case COMMIT:
						handleCommitInstruction(instruction);
						break;
					case PREPARE:
						handlePrepareInstruction(instruction);
						break;
					default:
						// Just do nothing...
						break;
				}
			} catch (InterruptedException e) {
				System.err.println("Transaction aborted. Error=" + e.getMessage());
			}
		}
	}

	private void handleCommitInstruction(DataSynchronisationInstruction instruction) {
		if (myState != State.PREPARED) {
			return;
		}
		
		boolean commit = (Boolean) instruction.arguments[0];

		System.out.println((commit ? "COMMITTING" : "ABORTING") + " transaction. TransactionID=" + instruction.getTransactionID());
		
		if (commit) {
			container.commit();
		} else {
			container.rollback();
		}
		
		store.releaseLocks();
		
		synchronizer.passInstruction(instruction);
		sendInstruction(DataSynchronisationInstruction.createCommitInstructionReply(instruction.getTransactionID(), myID));
		
		((ClientSynchronizer) synchronizer).passInstructionToLocal(instruction);
		
		// Clean up & finish off
		myState = State.DONE;
		cleanUp();
		running = false;
	}
	
	private void handlePrepareInstruction(DataSynchronisationInstruction instruction) {
		if (myState != State.BEGIN) {
			return;
		}
		
		// Get an abstraction for the container
		store = new DB4OInterface(lockManager, container);
		
		boolean success = true;
		
		// Retrieve the instruction
		DataSynchronisationInstruction _instruction = (DataSynchronisationInstruction) instruction.arguments[0];
		
		// Get the type of the wrapped data (first argument is the type)
		String type = (String) _instruction.arguments[0];
		
		System.out.println("Starting " + _instruction.getDataSynchronisationInstructiontype() + " transaction. DataType=" + 
				type + " TransactionID=" + instruction.getTransactionID());
		
		if (_instruction.getDataSynchronisationInstructiontype() == DataSynchronisationInstructionType.UPDATE_REQUEST) {
			HasID[] data = (HasID[]) _instruction.arguments[1];
			// Store the transmitted data items (second to n arguments are the data items)
			for (int i = 0; i < data.length; i++) {
				success = success && (store.put(type, data[i]) != null);  
			}
		} else {
			String[] data = (String[]) _instruction.arguments[1];
			// Store the transmitted data items (second to n arguments are the data items)
			for (int i = 0; i < data.length; i++) {
				success = success && store.delete(type, data[i]); 
			}
		}
				
		System.out.println("Executed " +_instruction.getDataSynchronisationInstructiontype() + " transaction. DataType=" + type +
				 "Result=" + success + " TransactionID=" + instruction.getTransactionID());
		
		sendInstruction(DataSynchronisationInstruction.createPrepareInstructionReply(instruction.getTransactionID(), myID, success));		
		myState = State.PREPARED;
	}
}
