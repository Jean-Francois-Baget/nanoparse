package fr.inria.jfbaget.nanoparse.readers;


import fr.inria.jfbaget.nanoparse.IReader;
import fr.inria.jfbaget.nanoparse.Parser;
import fr.inria.jfbaget.nanoparse.matches.ListMatch;
import fr.inria.jfbaget.nanoparse.matches.ObjectMatch;
import fr.inria.jfbaget.nanoparse.matches.StringMatch;
import fr.inria.jfbaget.nanoparse.IMatch;
import fr.inria.jfbaget.nanoparse.IParser;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONTokener;

public class SequenceReader extends AbstractReader{

    public List<IReader> readers;
    public List<String> readerIds;
    public int patternType = RETURN_ALL;
    public int singleReturn = -1;
    public List<Integer> manyReturns;
    public Map<Integer, String> objectReturns;
    
    private static final int EXTRACT_ONE = 0;
    private static final int RETURN_ALL = 1;
    private static final int EXTRACT_SOME = 2;
    private static final int MAKE_OBJECT = 3;
   
    public SequenceReader(String identifier, List<String> readerIds, boolean skip) {
        super(identifier, skip);
        this.readerIds = readerIds;
    }
    
    public SequenceReader(String identifier, List<String> readerIds, boolean skip, int unique) {
    	this(identifier, readerIds, skip);
    	this.patternType = EXTRACT_ONE;
		this.singleReturn = unique;
    }
    
    public SequenceReader(String identifier, List<String> readerIds, boolean skip, List<Integer> extracted) {
    	this(identifier, readerIds, skip);
    	this.patternType = EXTRACT_SOME;
		this.manyReturns = extracted;
    }
    
    public SequenceReader(String identifier, List<String> readerIds, boolean skip, Map<Integer, String> extracted) {
    	this(identifier, readerIds, skip);
    	this.patternType = MAKE_OBJECT;
		this.objectReturns = extracted;
    }
    
    
    @Override
    protected IMatch simpleread(CharSequence input, int start) {
    	//System.out.println(this.getName());
    	List<IMatch> result = new ArrayList<>();
    	int position = start;
    	for (IReader reader : this.readers) {
    		//System.out.println("reading " + reader.getName());
    		IMatch match = reader.read(input, position);
    		if (match.success()) {
    			result.add(match);
    			position = match.end();
    		}
    		else {
    			return new ListMatch(this, start, start, false, null);
    		}
    	}
        return this.makeMatch(start, position, true, result);
    }
    
    protected IMatch makeMatch(int start, int end, boolean success, List<IMatch> result) {
    	switch(this.patternType) {
    	case RETURN_ALL:
    		return new ListMatch(this, start, end, success, result);
    	case EXTRACT_ONE:
    		IMatch inner = result.get(this.singleReturn);
			return inner.copyWith(inner.start(), end, inner.success());
    	case EXTRACT_SOME: {
    		List<IMatch> newResult = new ArrayList<>();
    		for (int i = 0; i < this.manyReturns.size(); i++) {
    			newResult.add(i, result.get(this.manyReturns.get(i)));
    		}
    		return new ListMatch(this, start, end, success, newResult);
    	}
    	case MAKE_OBJECT: {
    		Map<String, IMatch> newResult = new HashMap<>();
    		for (Map.Entry<Integer, String> entry : this.objectReturns.entrySet()) {
    			newResult.put(entry.getValue(), result.get(entry.getKey()));
    		}
    		return new ObjectMatch(this, start, end, success, newResult);
    	}
    	default:
    		return new ListMatch(this, start, end, success, result);
    	}
   
    }
    
    @Override
    protected void simpleLink(IParser parser) {
    	this.readers = new ArrayList<>();
    	for (String readerId : this.readerIds) {
    		this.readers.add(parser.getReader(readerId));
    	}
    }
}
