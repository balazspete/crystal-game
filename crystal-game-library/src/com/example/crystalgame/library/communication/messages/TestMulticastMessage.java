package com.example.crystalgame.library.communication.messages;

/**
 * An object used to test multicast functionality
 * @author Balazs Pete, Shen Chen
 *
 */
public class TestMulticastMessage extends MulticastMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4122713149068539788L;

	public TestMulticastMessage() {
		super(MessageType.TEST_MULTICAST_MESSAGE);
	}

}
