package fr.inria.boreal.jfbaget.nanoparse.readers;


import fr.inria.boreal.jfbaget.nanoparse.IReader;
import fr.inria.boreal.jfbaget.nanoparse.Parser;
import fr.inria.boreal.jfbaget.nanoparse.matches.Match;
import fr.inria.boreal.jfbaget.nanoparse.IMatch;
import fr.inria.boreal.jfbaget.nanoparse.IParser;

import java.util.ArrayList;
import java.util.List;

public class OptionalReader extends AbstractReader{

    public IReader reader;
    public String readerId;
   
    public OptionalReader(String identifier, String readerId, boolean skip) {
        super(identifier, skip);
        this.readerId = readerId;
    }

    @Override
    protected IMatch simpleread(CharSequence input, int start) {
    	IMatch match = this.reader.read(input, start);
    	if (match.success()) {
    		return match;
    	} else {
    		return new Match<>(this, start, start, true, null);
    	}
    }
    
   
    
    @Override
    public void simpleLink(IParser parser) {
    	this.reader = parser.getReader(this.readerId);
    }
    
}
