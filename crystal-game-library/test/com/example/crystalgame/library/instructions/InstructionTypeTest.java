/**
 * 
 */
package com.example.crystalgame.library.instructions;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Allen Thomas Varghese
 *
 */
public class InstructionTypeTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		assertNotNull(InstructionType.DATA_SYNCRONISATION);
		assertNotNull(InstructionType.DATA_TRANSFER);
		assertNotNull(InstructionType.GAME_INSTRUCTION);
		assertNotNull(InstructionType.GROUP_INSTRUCTION);
		assertNotNull(InstructionType.GROUP_STATUS_INSTRUCTION);
	}

}
