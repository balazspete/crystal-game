package com.example.crystalgame.library.communication.abstraction;

import java.util.concurrent.LinkedBlockingQueue;

import com.example.crystalgame.library.communication.CommunicationFailureException;
import com.example.crystalgame.library.communication.CommunicationManager;
import com.example.crystalgame.library.communication.messages.Message;

public class MessageQueue extends Thread {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8069378267901501487L;

	private CommunicationManager manager;
	private static LinkedBlockingQueue<MessageQueueElement> messageQueue;
	private boolean sendMessages = true;
	
	public MessageQueue(CommunicationManager manager) {
		this.manager = manager;
		messageQueue = new LinkedBlockingQueue<MessageQueue.MessageQueueElement>();
	}
	
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
		while (sendMessages) {
			MessageQueueElement element = messageQueue.poll();
			if (element != null) {
				try {
					manager.sendData(element.communicationId, element.message);
					System.out.println("Sent message wooo!");
				} catch (CommunicationFailureException e) {
					System.err.println(e.getMessage());
					boolean success = false;
					do {
						success = put(element);
						try {
							sleep(2000);
						} catch (InterruptedException e1) {
							System.err.println(e1.getMessage());
						}
					} while (!success);
				}
			}
		}
	}
	
	public class MessageQueueElement {
		
		public final String communicationId;
		public final Message message;
		
		public MessageQueueElement(String communicationId, Message message) {
			this.communicationId = communicationId;
			this.message = message;
		}
		
	}
}
