package com.example.crystalgame.server.game;

import com.example.crystalgame.library.communication.messages.InstructionRelayMessage;
import com.example.crystalgame.library.instructions.Instruction;
import com.example.crystalgame.server.sequencer.Sequencer;

public class GameController extends Thread {

	private Sequencer sequencer;
	
	public GameController(Sequencer sequencer) {
		this.sequencer = sequencer;
	}

	public void sendInstructionToClient(String clientID, Instruction instruction) {
		InstructionRelayMessage message = new InstructionRelayMessage(clientID);
		message.setData(instruction);
		sequencer.sendMessageToOne(message);
	}
	
}
