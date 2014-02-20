package com.example.crystalgame.library.communication.messages;

import java.io.Serializable;

/**
 * A data type describing message types
 * @author Balazs Pete, Allen Thomas Varghese
 *
 */
public enum MessageType implements Serializable {

	TEST_MESSAGE,
	TEST_MULTICAST_MESSAGE,
	ID_MESSAGE,
	CONTROL_MESSAGE,
	GROUP_STATUS_MESSAGE,
	INSTRUCTION_MESSAGE,
	GENERAL_INSTRUCTION_MESSAGE,
	GAME_MESSAGE
	
}
