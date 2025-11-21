package fr.inria.jfbaget.nanoparse.readers;


import java.util.ArrayList;
import java.util.List;

import fr.inria.jfbaget.nanoparse.IMatch;
import fr.inria.jfbaget.nanoparse.IParser;
import fr.inria.jfbaget.nanoparse.IReader;
import fr.inria.jfbaget.nanoparse.matches.ListMatch;

public class RepetitionReader extends AbstractReader{
	
	public IReader reader;
	public String readerId;
	public String separatorId;
	public IReader separator;
	public final int min;
	public final int max;
	public final boolean subReaderWhenOne;
	public final boolean storeSep;
	   
    public RepetitionReader(String identifier, String readerId, String separatorId, int min, int max, boolean skip, boolean subReaderWhenOne, boolean storeSep) {
        super(identifier, skip);
        this.readerId = readerId;
        this.separatorId = separatorId;
        this.min = min;
        this.max = max;
		this.subReaderWhenOne = subReaderWhenOne;
		this.storeSep = storeSep;
    }

	public RepetitionReader(String identifier, String readerId, String separatorId, int min, int max, boolean skip) {
		this(identifier, readerId,  separatorId, min, max, skip, false, false);
    }

	protected IMatch readSepAndElem(CharSequence input, int start) {
		int positionsep;
		boolean sepresult;
		IMatch match;
		IMatch smatch = null;;
		if (this.separatorId == null) {
			positionsep = start;
			sepresult = true;
		} else {
			smatch = this.separator.read(input, start);
			positionsep = smatch.end();
			sepresult = smatch.success();
		}
		if (sepresult) {
			match = this.reader.read(input, positionsep);
			if (match.success()) {
				if (this.storeSep) return new ListMatch(this, start, match.end(), true, List.of(smatch, match));
				else return match;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

    @Override
    protected IMatch simpleread(CharSequence input, int start) {
		if (this.max == 0) return new ListMatch(this, start, start, true, null);
		int position = start;
    	IMatch firstMatch = this.reader.read(input, start);
    	boolean loop = firstMatch.success();
    	if (loop) position = firstMatch.end();
		else return new ListMatch(this, start, start, this.min == 0, null);
    
		if (this.max == 1 && this.subReaderWhenOne) return firstMatch;

		IMatch nextMatch = this.readSepAndElem(input, position);
		if (nextMatch == null) {
			if (this.min > 1) return new ListMatch(this, start, start, false, null);
			if (this.subReaderWhenOne) return firstMatch;
			List<IMatch> result = new ArrayList<>();
			result.add(firstMatch);
			return new ListMatch(this, start, position, true, result);
		}
		List<IMatch> result = new ArrayList<>();
		result.add(firstMatch);
		result.add(nextMatch);
		position = nextMatch.end();
		while (loop && result.size() < this.max) {
			nextMatch = this.readSepAndElem(input, position);
			if (nextMatch == null) loop = false;
			else {
				position = nextMatch.end();
				result.add(nextMatch);
			}
		}
		if (result.size() >= this.min) {
    		return new ListMatch(this, start, position, true, result);
    	} else {
    		return new ListMatch(this, start, start, false, null);
    	}


		/* 
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
		if (length >= this.min && length == 1 && this.subReaderWhenOne) {
			return result.getFirst();
		} else if (length >= this.min) {
    		return new ListMatch(this, start, position, true, result);
    	} else {
    		return new ListMatch(this, start, start, false, null);
    	}
			*/
    }
    
    @Override
    protected void simpleLink(IParser parser) {
    	this.reader = parser.getReader(this.readerId);	
    	if (this.separatorId != null) {
    		this.separator = parser.getReader(this.separatorId);
    	}
    }
}
