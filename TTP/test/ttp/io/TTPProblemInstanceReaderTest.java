package ttp.io;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.File;

import org.junit.Test;

import ttp.model.TTPInstance;

public class TTPProblemInstanceReaderTest {

	@Test
	public void testReadProblemInstance() throws Exception {

		File file = new File("testinstances"
				+ File.separator + "data4.txt");

		TTPInstance instance = TTPProblemInstanceReader
				.readProblemInstance(file);
		
		assertThat(instance, notNullValue());
	}

}
