package com.example.crystalgame.library.datawarehouse;

import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.example.crystalgame.library.instructions.DataSynchronisationInstruction;
import com.example.crystalgame.library.instructions.DataSynchronisationInstruction.DataSynchronisationInstructionType;

/**
 * A task used to request an update in the data warehouse
 * @author Balazs Pete, Allen Thomas Varhese
 *
 */
public class DataWarehouseUpdateTask implements Callable<Boolean> {
	
	private DataSynchronisationInstruction instruction;
	private Synchronizer synchronizer;
	
	public DataWarehouseUpdateTask(Synchronizer synchronizer, DataSynchronisationInstruction instruction) {
		this.synchronizer = synchronizer;
		this.instruction = instruction;
	}
	
	@Override
	public Boolean call() throws Exception {
		// Get the queue or this task from the synchroniser, the queue is used to receive messages
		LinkedBlockingQueue<DataSynchronisationInstruction> queue = synchronizer.requestTransaction(instruction);
		
		while (true) {
			// Block until there is an instruction
			DataSynchronisationInstruction instruction = queue.poll(Long.MAX_VALUE, TimeUnit.DAYS);
			
			// We are only interested about the result of the update
			if (instruction.getDataSynchronisationInstructiontype() == DataSynchronisationInstructionType.COMMIT) {
				// Determine whether update was successful
				boolean commit = (Boolean) instruction.arguments[0];

				System.out.println("DataWarehouseUpdateTask|call: Received 'COMMIT' instruction. Result=" + commit);
				
				if (commit) {
					return true;
				} else {
					// Update failed, throw an exception (as required by the interface)
					throw new DataWarehouseTaskException();
				}
			} else {
				System.out.println("DataWarehouseUpdateTask|call: Received non 'COMMIT' instruction");
			}
		}
	}
	
	/**
	 * Just a plain exception
	 * @author Balazs Pete, Allen Thomas Varghese
	 *
	 */
	public class DataWarehouseTaskException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = -5843935287248519887L;
		
	}
}
