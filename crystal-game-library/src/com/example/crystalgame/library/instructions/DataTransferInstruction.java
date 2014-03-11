package com.example.crystalgame.library.instructions;

import java.io.Serializable;

public class DataTransferInstruction extends Instruction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4618358027991585095L;

	public enum DataTransferInstructionType implements Serializable {
		DW_DOWNLOAD_REQUEST, DW_DOWNLOAD_REPLY
	}
	
	public final DataTransferInstructionType transferInstructionType;
	
	private DataTransferInstruction(DataTransferInstructionType type, Serializable... arguments) {
		super(InstructionType.DATA_TRANSFER, arguments);
		transferInstructionType = type;
	}

	public static DataTransferInstruction createDataWarehouseDownloadRequestInstruction() {
		return new DataTransferInstruction(DataTransferInstructionType.DW_DOWNLOAD_REQUEST);
	}
	
	public static DataTransferInstruction createDataWarehouseDownloadReplyInstruction(Serializable[] data) {
		return new DataTransferInstruction(DataTransferInstructionType.DW_DOWNLOAD_REPLY, data);
	}
	
}
