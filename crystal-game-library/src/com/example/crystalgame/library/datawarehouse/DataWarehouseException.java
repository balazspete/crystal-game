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
		MISMATCHING_TYPE_EXCEPTION = new DataWarehouseException("Not identical object types (classes).");
	
	
	private DataWarehouseException(String message) {
		super(message);
	}
	
}
