package com.example.crystalgame.server.game;
/**
 * Game Controller
 * @author Chen Shen, Rajan verma
 *
 */
import com.example.crystalgame.server.sequencer.Sequencer;

public class GameController extends Thread {

	private Sequencer sequencer;
	
	public GameController(Sequencer sequencer) {
		this.sequencer = sequencer;
	}


	
}
