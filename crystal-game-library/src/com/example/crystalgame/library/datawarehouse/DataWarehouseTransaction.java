package com.example.crystalgame.library.datawarehouse;

import java.util.concurrent.LinkedBlockingQueue;

import com.db4o.ObjectContainer;
import com.example.crystalgame.library.instructions.DataSynchronisationInstruction;

/**
 * An object coordinating a transaction within the data warehouse
 * @author Balazs Pete, Allen Thomas Varghese
 *
 */
public abstract class DataWarehouseTransaction implements Runnable {

	protected Synchronizer synchronizer;
	protected LinkedBlockingQueue<DataSynchronisationInstruction> queue;
	protected DB4OInterface storage;
	
	public DataWarehouseTransaction(Synchronizer synchronizer, 
			LinkedBlockingQueue<DataSynchronisationInstruction> queue, 
			ObjectContainer container) {
		this.synchronizer = synchronizer;
		this.queue = queue;
		this.storage = new DB4OInterface(container);
	}
	
	@Override
	public abstract void run();
	
	/**
	 * Send the input instruction to the other parties 
	 * @param instruction The instruction
	 */
	protected void sendInstruction(DataSynchronisationInstruction instruction) {
//		System.out.println("DataWarehouseTransaction|sendInstruction: TransactionID=" + instruction.getTransactionID() + " Type=" + instruction.getDataSynchronisationInstructiontype());
		synchronizer.sendInstruction(instruction);
	}

	/**
	 * Release the resources held by the transaction
	 */
	public void cleanUp() {
		System.out.println("DataWarehouseTransaction|cleanUp: Cleaning up transaction resources.");
		
		queue = null;
		storage.close();
	}
	
}
