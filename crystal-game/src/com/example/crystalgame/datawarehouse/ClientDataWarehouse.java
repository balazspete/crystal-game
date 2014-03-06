package com.example.crystalgame.datawarehouse;

import java.util.List;

import android.util.Log;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;

import com.example.crystalgame.library.data.HasID;
import com.example.crystalgame.library.datawarehouse.DataWarehouse;
import com.example.crystalgame.library.datawarehouse.DataWarehouseException;
import com.example.crystalgame.library.datawarehouse.Synchronizer;
import com.example.crystalgame.library.events.InstructionEventListener;
import com.example.crystalgame.library.instructions.DataSynchronisationInstruction;
import com.example.crystalgame.library.util.RandomID;

/**
 * The client version of the DB4OInterface
 * @author Balazs Pete, Allen Thomas Varghese
 *
 */
public class ClientDataWarehouse extends DataWarehouse {

	public static String DB_PATH;
	public static String myID;
	
	protected ClientDataWarehouse(ObjectContainer store, Synchronizer synchroniser) {
		super(store, synchroniser);
	}

	private static ClientDataWarehouse instance;
	
	/**
	 * Get the instance of the Data warehouse
	 * <i>You must set the myID variable prior to accessing the instance</i>
	 * @return The data warehouse
	 */
	public static ClientDataWarehouse getInstance() {
		if(instance == null) {
			if (DB_PATH == null) {
				Log.e("ClientDataWarehouse", "You must set the `DB_PATH` variable prior to using the DW");
				return new NullDataWarehouse();
			}
			
			if (myID == null) {
				Log.e("ClientDataWarehouse", "You must set the `myID` variable prior to using the DW");
				return new NullDataWarehouse();
			}
			
			ObjectContainer c = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), DB_PATH + "/" + RandomID.getRandomId());
			instance = new ClientDataWarehouse(c, new ClientSynchronizer(c, myID));
		}
		
		return instance;
	}

	/**
	 * An empty CLientDataWarehouse
	 * @author Balazs Pete, Allen Thomas Warghese
	 *
	 */
	protected static class NullDataWarehouse extends ClientDataWarehouse {

		protected NullDataWarehouse() {
			super(null, null);
		}
		
		@Override
		public HasID put(@SuppressWarnings("rawtypes") Class type, HasID value) throws DataWarehouseException {
			throw DataWarehouseException.NULL_WAREHOUSE;
		}
		
		@Override
		public List<HasID> putList(@SuppressWarnings("rawtypes") Class type, List<HasID> value) throws DataWarehouseException {
			throw DataWarehouseException.NULL_WAREHOUSE;
		}

		@Override
		public HasID get(@SuppressWarnings("rawtypes") Class type, String id) throws DataWarehouseException {
			throw DataWarehouseException.NULL_WAREHOUSE;
		}

		@Override
		public boolean delete(@SuppressWarnings("rawtypes") Class type, String id) throws DataWarehouseException {
			throw DataWarehouseException.NULL_WAREHOUSE;
		}
		
		@Override
		public boolean deleteList(@SuppressWarnings("rawtypes") Class type, List<String> ids) throws DataWarehouseException {
			throw DataWarehouseException.NULL_WAREHOUSE;
		}

		@Override
		public List<HasID> getList(@SuppressWarnings("rawtypes") Class type) throws DataWarehouseException {
			throw DataWarehouseException.NULL_WAREHOUSE;
		}
		
		@Override
		public void passInstruction(DataSynchronisationInstruction instruction) {}
		
		@Override
		public void addInstructionEventListener(InstructionEventListener listener) {}
		
		@Override
		public void removeInstructionEventListener(InstructionEventListener listener) {}
		
	}
	
}
