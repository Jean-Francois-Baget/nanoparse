package fr.inria.boreal.jfbaget.nanoparse.basetypes;

import fr.inria.boreal.jfbaget.nanoparse.readers.RegexReader;

public class IDReader extends RegexReader {
	
	public IDReader() {
		super("ID", "[a-zA-Z_$][a-zA-Z0-9_$]*", true);
	}

}
