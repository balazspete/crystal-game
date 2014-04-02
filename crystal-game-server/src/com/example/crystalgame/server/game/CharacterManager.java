package com.example.crystalgame.server.game;

/**
 * Character Manager
 * @author Chen Shen, Rajan verma
 *
 */
import java.util.ArrayList;
import com.example.crystalgame.server.sequencer.Sequencer;
import com.example.crystalgame.library.communication.messages.InstructionRelayMessage;
import com.example.crystalgame.library.data.Character;
import com.example.crystalgame.library.data.Character.PlayerType;
import com.example.crystalgame.library.data.GameLocation;
import com.example.crystalgame.library.data.Location;
import com.example.crystalgame.library.data.Zone;
import com.example.crystalgame.library.datawarehouse.DataWarehouse;
import com.example.crystalgame.library.datawarehouse.DataWarehouseException;
import com.example.crystalgame.library.instructions.GameInstruction;
import com.example.crystalgame.library.instructions.Instruction;


public class CharacterManager 
{
	GameLocation gameLocation;
	private static boolean result;
	private DataWarehouse dataWarehouse;
	private Sequencer sequencer;
	private GameManager manager;
	
	/**
	 * 
	 * @param gameLocation
	 * @param sequencer
	 */
	public CharacterManager(GameManager manager, GameLocation gameLocation, Sequencer sequencer)
	{
		this.gameLocation = gameLocation;
		this.sequencer = sequencer;
		this.manager= manager;
	}

	
	public void inGameLocation()
	{
		ArrayList<Location> locationPoints = new ArrayList<Location>();
		locationPoints =this.gameLocation.getLocationList();
		Character[] characters = getCharacters();
		for(Character character:characters)
		{
			result = Zone.checkIfWithin( locationPoints, character.getLocation());
			if(!(result))
			{
				if(character.getPlayerType().equals(PlayerType.PLAYER))
				{
					sendInstructionToClient(character.getClientId(),GameInstruction.createGameBoundaryDisqualifyInstruction());
					manager.removeClientFromGame(character.getClientId());
				}
			}
			else
			{
				
			}
		}
	}
	
	/**
	 * 
	 */
	public void disqualifyEnergy()
	{
		Character[] characters = getCharacters();
		for(Character character:characters)
		{
			if(character.getEnergy()<=0)
			{
				if(character.getPlayerType().equals(PlayerType.PLAYER))
				{
					sendInstructionToClient(character.getClientId(),GameInstruction.createEnergyDisqualifyInstruction());
					manager.removeClientFromGame(character.getClientId());
				}
			}
			else
			{
				
			}
		}
		
	}
	/**
	 * 
	 * @return
	 */
	public Character[] getCharacters()
	{
		/*Character character = new Sage(4, 2, PlayerType.PLAYER, "1222");
		character.setEnergy(1);
		Character[] sage = {character};
		*/
		try 
		{
			return dataWarehouse.getList(Character.class).toArray(new Character[0]);
		} 
		catch (DataWarehouseException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 
	 * @param clientID
	 * @param instruction
	 */
	public void sendInstructionToClient(String clientID, Instruction instruction) 
	{
		InstructionRelayMessage message = new InstructionRelayMessage(clientID);
		message.setData(instruction);
		this.sequencer.sendMessageToOne(message);
	}
}
