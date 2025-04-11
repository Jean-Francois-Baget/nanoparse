package fr.inria.boreal.jfbaget.nanoparse.basetypes;

import fr.inria.boreal.jfbaget.nanoparse.matches.FloatMatch;
import fr.inria.boreal.jfbaget.nanoparse.readers.RegexReader;

public class StrictFloatReader extends RegexReader {
	
	public StrictFloatReader() {
		//super("STRICTFLOAT", "[-+]?(?:[0-9]+\\.[0-9]*|\\.[0-9]+|[0-9]+(?:\\.[0-9]*)?[eE][-+]?[0-9]+)", true);
		//super("STRICTFLOAT", "([-+]?\\d+[eE][-+]?\\d+)  |(((\\d+\\.\\d*)|(\\.\\d+))([eE][-+]?\\d+)?)", true);
		super("STRICTFLOAT", "[-+]?\\d+[eE][-+]?\\d+|\\.\\d+([eE][+-]?\\d+)?|\\d+\\.\\d*([eE][+-]?\\d+)?", true);
	}
	
	@Override
	protected FloatMatch makeMatch(int start, int end, boolean success, String result) {
    	return new FloatMatch(this, start, end, success, result);
    }

}