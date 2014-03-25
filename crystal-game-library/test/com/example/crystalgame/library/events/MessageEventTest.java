/**
 * 
 */
package com.example.crystalgame.library.events;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.example.crystalgame.library.communication.messages.Message;
import com.example.crystalgame.library.communication.messages.MessageType;

/**
 * @author Allen Thomas Varghese
 *
 */
public class MessageEventTest {

	private Message message = null;
	private String senderId = null, receiverId = null;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		message = new Message(MessageType.CONTROL_MESSAGE) {
			
			@Override
			public boolean isMulticastMessage() {
				// TODO Auto-generated method stub
				return false;
			}
		};
		senderId = "sender123_setUp()";
		receiverId = "receiver123_setUp()";
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.example.crystalgame.library.events.MessageEvent#MessageEvent(com.example.crystalgame.library.communication.messages.Message)}.
	 */
	@Test
	public void testMessageEvent() {
		assertNotNull(new MessageEvent(message));
	}

	/**
	 * Test method for {@link com.example.crystalgame.library.events.MessageEvent#getSenderId()}.
	 */
	@Test
	public void testGetSenderId() {
		assertNotNull(senderId);
	}

	/**
	 * Test method for {@link com.example.crystalgame.library.events.MessageEvent#setSenderId(java.lang.String)}.
	 */
	@Test
	public void testSetSenderId() {
		senderId = "sender123";
		assertNotNull(senderId);
	}

	/**
	 * Test method for {@link com.example.crystalgame.library.events.MessageEvent#getReceiverId()}.
	 */
	@Test
	public void testGetReceiverId() {
		assertNotNull(receiverId);
	}

	/**
	 * Test method for {@link com.example.crystalgame.library.events.MessageEvent#setReceiverId(java.lang.String)}.
	 */
	@Test
	public void testSetReceiverId() {
		receiverId = "receiver123";
		assertNotNull(receiverId);
	}

	/**
	 * Test method for {@link com.example.crystalgame.library.events.MessageEvent#getMessage()}.
	 */
	@Test
	public void testGetMessage() {
		assertNotNull(message);
	}

}
