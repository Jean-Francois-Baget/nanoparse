package fr.inria.jfbaget.nanoparse.basetypes;

import fr.inria.jfbaget.nanoparse.readers.RegexReader;

public class QuotedStringReader  extends RegexReader {
	
	public QuotedStringReader() {
		super("STRING", "\"([^\"\\\\]|\\\\.)*\"", true);
	}

}
