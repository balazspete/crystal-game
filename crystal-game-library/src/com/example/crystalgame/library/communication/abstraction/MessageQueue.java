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
	public static final int MAX_TRIES = 3;
	
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
	public synchronized boolean put(String clientID, Message message) {
		return put(new MessageQueueElement(clientID, message));
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
				String communication = element.getCommunicationID();
				System.err.println(element.clientID);
				if (!manager.isClient() && communication == null) {
					throw new CommunicationFailureException("Client disconnected");
				}
				
				manager.sendData(communication, element.message);
			} catch (CommunicationFailureException e) {
				System.err.println(e.getMessage());
				
				if (element.incrementTryCount() < MAX_TRIES) {
					boolean success = false;
					do {
						// Message failed to send, put it back on the queue
						success = put(element);
						// Let's sleep for a bit before we try to send a message again...
						goToSleep();
					} while (!success);
				} else {
					manager.notifyOfDisconnectedClient(element.clientID);
				}
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
		
		private String clientID;
		private Message message;
		private int tries = 0;
		
		public MessageQueueElement(String clientID, Message message) {
			this.clientID = clientID;
			this.message = message;
		}
		
		public Message getMessage() {
			return message;
		}
		
		public String getCommunicationID() {
			return AbstractionModule.clientToCommunicationMap.get(clientID);
		}
		
		public int incrementTryCount() {
			return ++tries;
		}
		
	}
}
