package com.example.crystalgame.server.groups;

import com.example.crystalgame.library.communication.messages.Message;
import com.example.crystalgame.library.communication.messages.MulticastMessage;
import com.example.crystalgame.library.communication.messages.UnicastMessage;
import com.example.crystalgame.library.events.MessageEventListener;
import com.example.crystalgame.server.sequencer.Sequencer;

/**
 * An object handling a group instance
 * @author Balazs Pete, Shen Chen
 *
 */
public class GroupInstance implements Runnable {

	public final Group group;
	
	private boolean running = true;
	private Sequencer sequencer;
	
	/**
	 * Create a new group instance
	 * @param group The corresponding group
	 */
	public GroupInstance(Group group) {
		this.group = group;
		this.sequencer = new Sequencer(group);
	}

	@Override
	public void run() {
		System.out.println(group.groupId);
		// TODO: do group stuff here
		while(running) {
			System.out.println("Infinite looping here... (GroupInstance§run)");
			synchronized(this) {
				try {
					wait(2000);
				} catch (InterruptedException e) {
					// Ignored
				}
			}
		}
	}
	
	/**
	 * Stop the instance, terminating execution
	 */
	public void stopInstance() {
		running = false;
		// Stop child threads here
	}
	
	/**
	 * Send a message to the group
	 * @param message The message
	 */
	public void send(Message message) {
		sequencer.send(group.getClient(message.getSenderId()), message);
	}
	
	/**
	 * Add a sequencer event listener 
	 * @param listener the event listener
	 */
	public void addMessageEventListener(MessageEventListener listener) {
		sequencer.addSequencerEventListener(listener);
	}
	
	/**
	 * Remove a sequencer event listener
	 * @param listener The listener
	 */
	public void removeMessageEventListener(MessageEventListener listener) {
		sequencer.removeSequencerEventListener(listener);
	}
	
	/**
	 * Send a message to all members of the group
	 * @param message The message
	 */
	public void sendMessageToAll(MulticastMessage message) {
		sequencer.sendMessageToAll(message);
	}
	
	/**
	 * Send a message to one member of the group
	 * @param message The message
	 */
	public void sendMessageToOne(UnicastMessage message) {
		sequencer.sendMessageToOne(message);
	}
	
}
