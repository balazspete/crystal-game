package com.example.crystalgame.library.communication.abstraction;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.example.crystalgame.library.communication.CommunicationFailureException;
import com.example.crystalgame.library.communication.CommunicationManager;
import com.example.crystalgame.library.communication.messages.Message;

/**
 * A Message queue
 * @author Balazs Pete, Rajan Verma
 *
 */
public class MessageQueue extends Thread {

	public static final int SLEEP_TIME = 5000;
	
	private CommunicationManager manager;
	private static LinkedBlockingQueue<MessageQueueElement> messageQueue;
	private boolean sendMessages = true;
	
	/**
	 * Create a message queue
	 * @param manager The Communication manager to use
	 */
	public MessageQueue(CommunicationManager manager) {
		this.manager = manager;
		messageQueue = new LinkedBlockingQueue<MessageQueue.MessageQueueElement>();
	}
	
	/**
	 * Add a message to send
	 * @param communicationId The ID of the communication channel to use
	 * @param message The message
	 * @return True of added
	 */
	public synchronized boolean put(String communicationId, Message message) {
		return put(new MessageQueueElement(communicationId, message));
	}
	
	private synchronized boolean put(MessageQueueElement element) {
		try {
			messageQueue.put(element);
		} catch (InterruptedException e) {
			System.err.println(e.getMessage());
			return false;
		}
		
		return true;
	}
	
	@Override
	public void run() {
		sendService();
	}
	
	/**
	 * Send the messages from the queue
	 */
	private void sendService() {
		while (sendMessages) {
			// Get the first message in the queue (block until message in queue)
			MessageQueueElement element = null;
			try {
				element = messageQueue.poll(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e2) {
				// Element is gonna be null, so we are going to handle it in the next step...
			}
			
			if (element == null) {
				// Let's put the thread to sleep for a bit, then try again... 
				goToSleep();
				continue;
			}
			
			try {
				// try to send the data
				manager.sendData(element.communicationId, element.message);
			} catch (CommunicationFailureException e) {
				System.err.println(e.getMessage());
				boolean success = false;
				do {
					// Message failed to send, put it back on the queue
					success = put(element);
					// Let's sleep for a bit before we try to send a message again...
					goToSleep();
				} while (!success);
			}
		}
	}
	
	private void goToSleep() {
		try {
			sleep(SLEEP_TIME);
		} catch (InterruptedException e1) {
			System.err.println(e1.getMessage());
		}
	}
	
	/**
	 * An element on the message queue
	 * @author Balazs Pete, Rajan Verma
	 *
	 */
	public class MessageQueueElement {
		
		public final String communicationId;
		public final Message message;
		
		public MessageQueueElement(String communicationId, Message message) {
			this.communicationId = communicationId;
			this.message = message;
		}
		
	}
}
