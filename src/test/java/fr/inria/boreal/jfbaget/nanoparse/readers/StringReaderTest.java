package fr.inria.boreal.jfbaget.nanoparse.readers;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;

import fr.inria.boreal.jfbaget.nanoparse.matches.StringMatch;

class StringReaderTest {

	@Test
	void test() {
		StringReader reader = new StringReader("", "foo", false);
		StringMatch match = (StringMatch)reader.read("foobar", 0);
		assertEquals(match.success(), true);
		assertEquals(match.result(), "foo");
		assertEquals(match.start(), 0);
		assertEquals(match.end(), 3);
	}
	
	@Test
	void test2() {
		StringReader reader = new StringReader("", "foo", false);
		StringMatch match = (StringMatch)reader.read("barfoo", 3);
		assertEquals(match.success(), true);
		assertEquals(match.result(), "foo");
		assertEquals(match.start(), 3);
		assertEquals(match.end(), 6);
	}
	
	@Test
	void test3() {
		StringReader reader = new StringReader("", "foo", false);
		StringMatch match = (StringMatch)reader.read("fo", 0);
		assertEquals(match.success(), false);
		//assertEquals(match.result(), "foo");
		assertEquals(match.start(), 0);
		//assertEquals(match.end(), 0);
	}

}
