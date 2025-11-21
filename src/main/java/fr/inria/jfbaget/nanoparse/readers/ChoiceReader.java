package fr.inria.jfbaget.nanoparse.readers;

import java.util.ArrayList;
import java.util.List;


import fr.inria.jfbaget.nanoparse.IReader;
import fr.inria.jfbaget.nanoparse.Parser;
import fr.inria.jfbaget.nanoparse.matches.Match;
import fr.inria.jfbaget.nanoparse.IMatch;
import fr.inria.jfbaget.nanoparse.IParser;



public class ChoiceReader extends AbstractReader{

    public List<IReader> readers;
    public List<String> readerIds;
   
    public ChoiceReader(String identifier, List<String> readerIds, boolean skip) {
        super(identifier, skip);
        this.readerIds = readerIds;
    }

    @Override
    protected IMatch simpleread(CharSequence input, int start) {
    	for (IReader reader : this.readers) {
    		IMatch match = reader.read(input, start);
    		if (match.success()) {
    			return match;
    			//return new Match<>(this, start, match.end(), true, match);
    		}
    	}
        return new Match<>(this, start, start, false, null);
    }
    
    
    
    @Override
    public void simpleLink(IParser parser) {
    	this.readers = new ArrayList<>();
    	for (String readerId : this.readerIds) {
    		this.readers.add(parser.getReader(readerId));
    		
    	}
    }
    
}
