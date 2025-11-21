package fr.inria.jfbaget.nanoparse.basetypes;


import fr.inria.jfbaget.nanoparse.matches.BoolMatch;
import fr.inria.jfbaget.nanoparse.readers.RegexReader;

public class BoolReader extends RegexReader {
	
	public BoolReader() {
		super("BOOL", "(true)|(false)", true);
	}
	
	@Override
	protected BoolMatch makeMatch(int start, int end, boolean success, String result) {
    	return new BoolMatch(this, start, end, success, result);
    }

}
