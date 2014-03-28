package com.example.crystalgame.game;

import java.util.ArrayList;
import java.util.List;

import com.example.crystalgame.CrystalGame;
import com.example.crystalgame.communication.ClientCommunication;
import com.example.crystalgame.communication.ClientOutgoingMessages;
import com.example.crystalgame.library.data.Character;
import com.example.crystalgame.library.data.Crystal;
import com.example.crystalgame.datawarehouse.ClientDataWarehouse;
import com.example.crystalgame.library.data.HasID;
import com.example.crystalgame.library.datawarehouse.DataWarehouseException;
import com.example.crystalgame.library.instructions.CharacterInteractionInstruction;
import com.example.crystalgame.library.instructions.InstructionFormatException;
import com.example.crystalgame.library.instructions.CharacterInteractionInstruction.RPSSelection;

public class InteractionManager {

	private static InteractionManager instance;
	
	private long timestamp;
	private boolean inDuel;
	
	private String currentDuel,
		otherClientID, 
		otherCharacterID;
	
	private RPSSelection mySelection, otherSelection;
	
	private boolean master;
	
	public InteractionManager() {
		reset();
	}
	
	public void initiateInteraction(ClientOutgoingMessages out, String otherClientID) {
		inDuel = true;
	}
	
	public void onInteractionRequest(ClientOutgoingMessages out, CharacterInteractionInstruction instruction) {
		CharacterInteractionInstruction replyInstruction;
		try {
			timestamp = timestamp != Long.MAX_VALUE ? timestamp : System.currentTimeMillis();
			replyInstruction = CharacterInteractionInstruction.
				createInteractionRequestAcknowledgmentInstruction(instruction, CrystalGame.getClientID(), 
						InventoryManager.getInstance().getCharacter().getID(),
						!inDuel, timestamp);
		} catch (InstructionFormatException e) {
			return;
		}
		
		if (!inDuel) {
			inDuel = true;
			otherCharacterID = (String) instruction.arguments[2];
			otherClientID = (String) instruction.arguments[1];
		}
		
		out.relayInstruction(replyInstruction, otherCharacterID);
	}
	
	public boolean onInteractionRequestAcknowledgment(CharacterInteractionInstruction instruction) {
		boolean duel = (Boolean) instruction.arguments[3];
		if (duel) {
			master = timestamp < ((Long) instruction.arguments[4]);
		} else {
			inDuel = false;
		}
		
		return duel;
	}
	
	public boolean isMaster() {
		return master;
	}
	
	public void onRPSSelectionRequest(ClientOutgoingMessages out, CharacterInteractionInstruction instruction) {
		
	}
	
	public int onRPSSelectionReply(CharacterInteractionInstruction instruction) {
		RPSSelection selection = (RPSSelection) instruction.arguments[0];
		if (selection != null) {
			otherSelection = selection;
			return game();
		}
		
		return 0;
	}
	
	public boolean onRPSSelection(ClientOutgoingMessages out, CharacterInteractionInstruction.RPSSelection selection) {
		if (selection == null || otherClientID == null || out == null) {
			return false;
		}
		
		CharacterInteractionInstruction instruction;
		if (master) {
			mySelection = selection;
			instruction = CharacterInteractionInstruction.createRPSSelectionRequest();
		} else {
			try {
				instruction = CharacterInteractionInstruction.createRPSSelectionReply(selection);
			} catch (InstructionFormatException e) {
				return false;
			}
		}
		
		out.relayInstruction(instruction, otherClientID);
		
		return true;
	}
	
	public void localWin(ClientOutgoingMessages out) {
		relayResult(out, false);
		giveCrystal(otherCharacterID, myCharacterID());
		reset();
	}
	
	public void remoteWin(ClientOutgoingMessages out) {
		relayResult(out, true);
		giveCrystal(myCharacterID(), otherCharacterID);
		reset();
	}
	
	private String myCharacterID() {
		Character c = InventoryManager.getInstance().getCharacter();
		if (c != null) {
			return c.getID();
		}
	
		return null;
	}
	
	private void relayResult(ClientOutgoingMessages out, boolean didWin) {
		out.relayInstruction(CharacterInteractionInstruction.createRPSResultRelay(didWin), otherClientID);
	}
	
	private void giveCrystal(String from, String to) {
		try {
			HasID f = ClientDataWarehouse.getInstance().get(Character.class, from);
			HasID t = ClientDataWarehouse.getInstance().get(Character.class, to);
			
			if (f == null && t == null) {
				return;
			}
			
			Character cFrom = (Character) f;
			boolean result = cFrom.removeOneCrystal();
			
			if (result) {
				Character cTo = (Character) t;
				cTo.addCrystal(new Crystal(cTo.getLongitude(), cTo.getLatitude()));
				
				List<HasID> list = new ArrayList<HasID>();
				list.add(cFrom);
				list.add(cTo);
				
				ClientDataWarehouse.getInstance().putList(Character.class, list);
			}
		} catch (DataWarehouseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void reset() {
		timestamp = Long.MAX_VALUE;
		inDuel = false;
		currentDuel = null;
		otherCharacterID = null;
		otherClientID = null;
		master = false;
		mySelection = null;
		otherSelection = null;
	}
	
	// 1 if I win
	private int game() {
		int score = 0;
		if (mySelection == otherSelection) {
			score = 0;
		} else {
			switch (mySelection) {
				case PAPER:
					score = otherSelection == RPSSelection.ROCK ? 1 : -1;
					break;
				case ROCK:
					score = otherSelection == RPSSelection.SCISSORS ? 1 : -1;
					break;
				case SCISSORS:
					score = otherSelection == RPSSelection.PAPER ? 1 : -1;
					break;
			}
		}
		return score;
	}
	
	public static InteractionManager getInstance() {
		if (instance == null) {
			instance = new InteractionManager();
		}
		
		return instance;
	}
	
}
