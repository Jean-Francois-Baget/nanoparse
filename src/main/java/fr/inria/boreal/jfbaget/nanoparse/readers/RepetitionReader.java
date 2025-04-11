package fr.inria.boreal.jfbaget.nanoparse.readers;


import java.util.ArrayList;
import java.util.List;

import fr.inria.boreal.jfbaget.nanoparse.IMatch;
import fr.inria.boreal.jfbaget.nanoparse.IParser;
import fr.inria.boreal.jfbaget.nanoparse.IReader;
import fr.inria.boreal.jfbaget.nanoparse.Parser;
import fr.inria.boreal.jfbaget.nanoparse.matches.ListMatch;

public class RepetitionReader extends AbstractReader{
	
	public IReader reader;
	public String readerId;
	public String separatorId;
	public IReader separator;
	public int min = 0;
	public int max = Integer.MAX_VALUE;
	   
    public RepetitionReader(String identifier, String readerId, String separatorId, int min, int max, boolean skip) {
        super(identifier, skip);
        this.readerId = readerId;
        this.separatorId = separatorId;
        this.min = min;
        this.max = max;
    }

    @Override
    protected ListMatch simpleread(CharSequence input, int start) {
    	List<IMatch> result = new ArrayList<>();
    	int position = start;
    	int positionsep;
    	int length = 0;
    	IMatch match = this.reader.read(input, start);
    	boolean loop = match.success();
    	boolean sepresult;
    	if (loop) {
    		result.add(match);
    		position = match.end();
    		length += 1;
    	}
    	while(loop && length < this.max) {
    		if (this.separatorId == null) {
    			positionsep = position;
    			sepresult = true;
    		} else {
    			match = this.separator.read(input, position);
    			positionsep = match.end();
    			sepresult = match.success();
    		}
    		if (sepresult) {
    			match = this.reader.read(input, positionsep);
    			if (match.success()) {
    				result.add(match);
    				length += 1;
    				position = match.end();
    			} else {
    				loop = false;
    			}
    		} else {
    			loop = false;
    		}
    	}
    	if (length >= this.min) {
    		return new ListMatch(this, start, position, true, result);
    	} else {
    		return new ListMatch(this, start, start, false, null);
    	}
    }
    
    @Override
    protected void simpleLink(IParser parser) {
    	this.reader = parser.getReader(this.readerId);	
    	if (this.separatorId != null) {
    		this.separator = parser.getReader(this.separatorId);
    	}
    }
}
