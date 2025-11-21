package fr.inria.jfbaget.nanoparse.readers;


import fr.inria.jfbaget.nanoparse.IParser;
import fr.inria.jfbaget.nanoparse.Parser;
import fr.inria.jfbaget.nanoparse.matches.Match;
import fr.inria.jfbaget.nanoparse.matches.StringMatch;

public class StringReader extends AbstractReader {

 
    public String pattern;
   

    public StringReader(String identifier, String pattern, boolean skip) {
        super(identifier, skip);
        this.pattern = pattern;
    }

    @Override
    protected StringMatch simpleread(CharSequence input, int start) {
    	int length = this.pattern.length();
        int end = start + length;
        if (input.length() < end) {
        	return this.makeMatch(start, end, false, null);
        			//new StringMatch(this, start, start, false, null);
        }
        for (int i = 0; i < length; i++) {
        	if(input.charAt(start + i) != this.pattern.charAt(i)) {
        		return this.makeMatch(start, end, false, null);
        				//new StringMatch(this, start, start, false, null);
        	}	
        }
        return this.makeMatch(start, end, true, this.pattern);
        		//new StringMatch(this, start, end, true, this.pattern);
    }
    
    
    @Override
    protected void simpleLink(IParser parser) {}
    
    protected StringMatch makeMatch(int start, int end, boolean success, String result) {
    	return new StringMatch(this, start, end, success, result);
    }
    
    
      
}

