/**
 * 
 */
package com.example.crystalgame.library.events;

import static org.junit.Assert.assertNotNull;

import java.io.Serializable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.example.crystalgame.library.instructions.Instruction;
import com.example.crystalgame.library.instructions.InstructionType;

/**
 * @author Allen Thomas Varghese
 *
 */
public class InstructionEventTest {

	private Instruction instruction;
	private Serializable[] arguments;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		arguments = new Serializable[] {3,1,2};
		instruction = new Instruction(InstructionType.DATA_TRANSFER, arguments) {};
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.example.crystalgame.library.events.InstructionEvent#InstructionEvent(com.example.crystalgame.library.instructions.Instruction)}.
	 */
	@Test
	public void testInstructionEvent() {
		assertNotNull(new InstructionEvent(instruction));
	}

	/**
	 * Test method for {@link com.example.crystalgame.library.events.InstructionEvent#getInstruction()}.
	 */
	@Test
	public void testGetInstruction() {
		assertNotNull(instruction);
	}

}
