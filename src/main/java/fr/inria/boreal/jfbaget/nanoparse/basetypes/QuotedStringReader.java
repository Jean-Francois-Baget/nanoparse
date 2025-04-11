package fr.inria.boreal.jfbaget.nanoparse.basetypes;

import fr.inria.boreal.jfbaget.nanoparse.readers.RegexReader;

public class QuotedStringReader  extends RegexReader {
	
	public QuotedStringReader() {
		super("STRING", "\"([^\"\\\\]|\\\\.)*\"", true);
	}

}
