package fr.inria.boreal.jfbaget.nanoparse.basetypes;

import fr.inria.boreal.jfbaget.nanoparse.matches.IntMatch;
import fr.inria.boreal.jfbaget.nanoparse.readers.RegexReader;

public class IntReader extends RegexReader {
	
	public IntReader() {
		super("INT", "[+-]?[0-9]+", true);
	}
	
	@Override
	protected IntMatch makeMatch(int start, int end, boolean success, String result) {
    	return new IntMatch(this, start, end, success, result);
    }

}
