package fr.inria.boreal.jfbaget.nanoparse.basetypes;

import fr.inria.boreal.jfbaget.nanoparse.matches.FloatMatch;
import fr.inria.boreal.jfbaget.nanoparse.readers.RegexReader;

public class FloatReader extends RegexReader {
	
	public FloatReader() {
		//super("FLOAT", "[-+]?(?:\\d+(?:\\.\\d*)?|\\.\\d+)(?:[eE][-+]?\\d+)?", true);
		super("FLOAT", "[-+]?(?:\\d+(?:\\.\\d*)?|\\.\\d+)(?:[eE][-+]?\\d+)?", true);
	}
	
	@Override
	protected FloatMatch makeMatch(int start, int end, boolean success, String result) {
    	return new FloatMatch(this, start, end, success, result);
    }

}