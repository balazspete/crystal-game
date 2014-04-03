package com.example.crystalgame.library.datawarehouse;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import com.db4o.ObjectContainer;
import com.example.crystalgame.library.events.InstructionEvent;
import com.example.crystalgame.library.events.InstructionEventListener;
import com.example.crystalgame.library.events.ListenerManager;
import com.example.crystalgame.library.instructions.DataSynchronisationInstruction;

public abstract class Synchronizer {
	
	protected ObjectContainer container;
	private ListenerManager<InstructionEventListener, InstructionEvent> manager;
	protected ExecutorService pool;
	
	public Synchronizer(ObjectContainer container) {
		this.container = container;
		this.pool = Executors.newFixedThreadPool(100);
		manager = new ListenerManager<InstructionEventListener, InstructionEvent>() {
			@Override
			protected void eventHandlerHelper(InstructionEventListener listener, InstructionEvent event) {
				InstructionEventListener.eventHandlerHelper(listener, event);
			}
		};
	}
	
	/**
	 * Request a transaction
	 * @param instruction The instruction to execute
	 */
	public abstract LinkedBlockingQueue<DataSynchronisationInstruction> requestTransaction(DataSynchronisationInstruction instruction);
	
	/***************************
	*** INSTRUCTIONS PASSING ***
	***************************/
	
	/**
	 * Pass an instruction to the synchroniser
	 * @param instruction The instruction
	 */
	public abstract void passInstruction(DataSynchronisationInstruction instruction);
		
	/**
	 * Add an instruction event listener to the synchroniser 
	 * @param listener The listener
	 */
	public void addInstructionEventListener(InstructionEventListener listener) {
		manager.addEventListener(listener);
	}
	
	/**
	 * Remove an instruction event listener from the synchroniser 
	 * @param listener The listener
	 */
	public void removeInstructionEventListener(InstructionEventListener listener) {
		manager.removeEventListener(listener);
	}

	/**
	 * Forward the instruction to the client synchronisers
	 * @param instruction The instruction
	 */
	protected void sendInstruction(DataSynchronisationInstruction instruction) {
		manager.send(new InstructionEvent(instruction));
	}
	
	/**
	 * Clean up after a transaction
	 * @param transactionID The transaction ID
	 */
	public abstract void cleanUp(String transactionID);
	
	protected void putOnQueue(LinkedBlockingQueue<DataSynchronisationInstruction> queue, DataSynchronisationInstruction instruction) {
		if (queue == null) {
			return;
		}
		
		while(true) {
			try {
				queue.put(instruction);
				break;
			} catch (InterruptedException e) {
				// try again...
			}
		}
	}
}
