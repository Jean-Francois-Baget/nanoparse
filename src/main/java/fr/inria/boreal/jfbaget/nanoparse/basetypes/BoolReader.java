package fr.inria.boreal.jfbaget.nanoparse.basetypes;


import fr.inria.boreal.jfbaget.nanoparse.matches.BoolMatch;
import fr.inria.boreal.jfbaget.nanoparse.readers.RegexReader;

public class BoolReader extends RegexReader {
	
	public BoolReader() {
		super("BOOL", "(true)|(false)", true);
	}
	
	@Override
	protected BoolMatch makeMatch(int start, int end, boolean success, String result) {
    	return new BoolMatch(this, start, end, success, result);
    }

}
