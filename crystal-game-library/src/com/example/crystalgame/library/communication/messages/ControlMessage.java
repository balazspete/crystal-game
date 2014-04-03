package com.example.crystalgame.library.communication.messages;

/**
 * A message containing an instruction used for controlling features
 * @author Balazs Pete, Rajan Verma
 *
 */
public class ControlMessage extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8195375550502449610L;

	public ControlMessage() {
		super(MessageType.CONTROL_MESSAGE);
	}

	@Override
	public boolean isMulticastMessage() {
		return false;
	}

}
