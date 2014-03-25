/**
 * 
 */
package com.example.crystalgame.library.instructions;

import static org.junit.Assert.assertNotNull;

import java.io.Serializable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Allen Thomas Varghese
 *
 */
public class InstructionTest {
	
	Serializable[] arguments;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		arguments = new Serializable[] {1,2,3};
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.example.crystalgame.library.instructions.Instruction#Instruction(com.example.crystalgame.library.instructions.InstructionType, java.io.Serializable[])}.
	 */
	@Test
	public void testInstruction() {
		assertNotNull(new Instruction(InstructionType.DATA_TRANSFER, arguments){

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;});
	}

}
