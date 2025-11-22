package fr.inria.jfbaget.nanoparse.readers;

import static org.junit.jupiter.api.Assertions.*;


import fr.inria.jfbaget.nanoparse.Parser;
import org.junit.jupiter.api.Test;

import fr.inria.jfbaget.nanoparse.matches.StringMatch;

import java.util.List;

class StringReaderTest {

	@Test
	void test() {
		StringReader reader = new StringReader("", "foo", false);
		StringMatch match = (StringMatch)reader.read("foobar", 0);
		assertTrue(match.success());
		assertEquals(match.result(), "foo");
		assertEquals(match.start(), 0);
		assertEquals(match.end(), 3);
	}
	
	@Test
	void test2() {
		StringReader reader = new StringReader("", "foo", false);
		StringMatch match = (StringMatch)reader.read("barfoo", 3);
		assertTrue(match.success());
		assertEquals(match.result(), "foo");
		assertEquals(match.start(), 3);
		assertEquals(match.end(), 6);
	}
	
	@Test
	void test3() {
		StringReader reader = new StringReader("", "foo", false);
		StringMatch match = (StringMatch)reader.read("fo", 0);
		assertFalse(match.success());
		assertEquals(match.start(), 0);
		assertEquals(match.end(), 0);
	}

	@Test
	void test4() {
		Parser parser = new Parser(List.of(
				new StringReader( "main", "foo", true)
		));
		StringMatch match = (StringMatch)parser.read("  foo", 0);
		assertTrue(match.success());
		assertEquals(match.start(), 2);
	}
	       
}
