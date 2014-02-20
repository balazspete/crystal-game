package com.example.crystalgame.library.events;

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
	
	public abstract void onGameInstruction(InstructionEvent event);
	
	/**
	 * Implementation of the eventHandlerHelper function of the listener manager
	 * @param listener The listener
	 * @param event The event
	 */
	public static void eventHandlerHelper(InstructionEventListener listener, InstructionEvent event) {
		switch(event.getInstruction().type) {
			case GROUP_INSTRUCTION:
				listener.onGroupInstruction(event);
				break;
			case GROUP_STATUS_INSTRUCTION:
				listener.onGroupStatusInstruction(event);
				break;
			case GAME_INSTRUCTION:
				listener.onGameInstruction(event);
				break;
			default:
				return;
		
		}
	}
	
}