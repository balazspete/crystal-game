package com.example.crystalgame.library.events;

import java.util.Arrays;
import java.util.EventListener;

/**
 * An instruction event listener
 * @author Balazs Pete, Allen Thomas Varghese, Rajan Verma
 *
 */
public abstract class InstructionEventListener implements EventListener {

	/**
	 * Get group instruction events
	 * @param event The event
	 */
	public abstract void onGroupInstruction(InstructionEvent event);
	
	/**
	 * Get group status instruction events
	 * @param event The event
	 */
	public abstract void onGroupStatusInstruction(InstructionEvent event);
	
	/**
	 * Get game instruction events
	 * @param event the event
	 */
	public abstract void onGameInstruction(InstructionEvent event);
	
	/**
	 * Get data synchronisation instruction events
	 * @param event The event
	 */
	public abstract void onDataSynchronisationInstruction(InstructionEvent event);
	
	/**
	 * Get data transfer instruction events
	 * @param event The event
	 */
	public abstract void onDataTransferInstruction(InstructionEvent event);
	
	/**
	 * Get communication status instruction events
	 * @param event The event
	 */
	public abstract void onCommunicationStatusInstruction(InstructionEvent event);
	
	/**
	 * Get character interaction instruction events
	 * @param event The event
	 */
	public abstract void onCharacterInteractionInstruction(InstructionEvent event);
	
	/**
	 * Implementation of the eventHandlerHelper function of the listener manager
	 * @param listener The listener
	 * @param event The event
	 */
	public static void eventHandlerHelper(InstructionEventListener listener, InstructionEvent event) {
		switch(event.getInstruction().type) {
			case GROUP_INSTRUCTION:
				System.out.println(Arrays.toString(event.getInstruction().arguments));
				listener.onGroupInstruction(event);
				break;
			case GROUP_STATUS_INSTRUCTION:
				listener.onGroupStatusInstruction(event);
				break;
			case GAME_INSTRUCTION:
				listener.onGameInstruction(event);
				break;
			case DATA_SYNCRONISATION:
				listener.onDataSynchronisationInstruction(event);
				break;
			case DATA_TRANSFER:
				listener.onDataTransferInstruction(event);
				break;
			case COMMUNICATION_STATUS:
				listener.onCommunicationStatusInstruction(event);
				break;
			case CHARACTER_INTERACTION:
				listener.onCharacterInteractionInstruction(event);
				break;
			default:
				return;
		
		}
	}
	
}
