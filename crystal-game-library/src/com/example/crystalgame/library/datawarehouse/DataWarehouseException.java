package com.example.crystalgame.library.datawarehouse;

/**
 * The exception describing a data warehouse fault
 * @author Balazs Pete, Allen Thomas Varghese
 *
 */
public class DataWarehouseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1151260724445469202L;

	public static DataWarehouseException
		MISMATCHING_ID_EXCEPTION = new DataWarehouseException("Not identical object IDs."),
		MISMATCHING_TYPE_EXCEPTION = new DataWarehouseException("Not identical object types (classes)."),
		FAILED_TO_UPDATE = new DataWarehouseException("Failed to update input data"),
		NULL_WAREHOUSE = new DataWarehouseException("Warehouse not initialised");
	
	
	private DataWarehouseException(String message) {
		super(message);
	}
	
	public static DataWarehouseException initialisationException(String message) {
		return new DataWarehouseException("Failed to initialise: " + message);
	}
	
}
