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

/**
 * Reader that matches a sequence of rules.
 * <p>
 * A {@code SequenceReader} applies each of its inner readers in order, starting
 * at the current position, threading the end position of each successful match
 * into the next. If any inner reader fails, the whole sequence fails with an
 * empty match (span {@code [start, start)} and {@code result == null}).
 * <p>
 * The {@link #patternType} controls how the list of sub-matches is turned into
 * the final {@link IMatch}:
 * <ul>
 *   <li>{@link #RETURN_ALL}: return all sub-matches as a {@link ListMatch}.</li>
 *   <li>{@link #EXTRACT_ONE}: return a single selected sub-match (by index),
 *       with its span adjusted to the full sequence.</li>
 *   <li>{@link #EXTRACT_SOME}: return a {@link ListMatch} over a subset of
 *       sub-matches (selected by indices).</li>
 *   <li>{@link #MAKE_OBJECT}: return an {@link ObjectMatch} whose fields are
 *       selected sub-matches mapped to names.</li>
 * </ul>
 * Skip behaviour is controlled by the {@code skip} flag passed to the
 * constructor. If {@code skip} is {@code true}, the global {@code "skip"}
 * rule is applied before this sequence is evaluated (via
 * {@link AbstractReader#read(CharSequence, int)}). Each inner reader then
 * handles its own skip behaviour independently.
 */
public class SequenceReader extends AbstractReader{

    public List<IReader> readers;
    public List<String> readerIds;

	/** How to build the result from the list of sub-matches. */
    public int patternType = RETURN_ALL;

	/** Index of the single sub-match to return when {@link #patternType} is EXTRACT_ONE. */
    public int singleReturn = -1;

	/** Indices of the sub-matches to keep when {@link #patternType} is EXTRACT_SOME. */
    public List<Integer> manyReturns;

	/**
	 * Map from sub-match index to field name when {@link #patternType} is MAKE_OBJECT.
	 * Keys are indices into {@link #readers}, values are field names.
	 */
    public Map<Integer, String> objectReturns;
    
    private static final int EXTRACT_ONE = 0;
    private static final int RETURN_ALL = 1;
    private static final int EXTRACT_SOME = 2;
    private static final int MAKE_OBJECT = 3;


	/**
	 * Creates a sequence that returns all its sub-matches.
	 *
	 * @param identifier the unique name of this rule in the grammar
	 * @param readerIds  the names of the readers to apply in order
	 * @param skip       {@code true} if the global {@code "skip"} rule should
	 *                   be applied before this sequence
	 */
    public SequenceReader(String identifier, List<String> readerIds, boolean skip) {
        super(identifier, skip);
        this.readerIds = readerIds;
    }

	/**
	 * Creates a sequence that returns only a single sub-match.
	 *
	 * @param identifier the unique name of this rule in the grammar
	 * @param readerIds  the names of the readers to apply in order
	 * @param skip       whether to apply {@code skip} before the sequence
	 * @param unique     index of the sub-match to return
	 */
    public SequenceReader(String identifier, List<String> readerIds, boolean skip, int unique) {
    	this(identifier, readerIds, skip);
    	this.patternType = EXTRACT_ONE;
		this.singleReturn = unique;
    }

	/**
	 * Creates a sequence that returns a subset of its sub-matches as a list.
	 *
	 * @param identifier the unique name of this rule in the grammar
	 * @param readerIds  the names of the readers to apply in order
	 * @param skip       whether to apply {@code skip} before the sequence
	 * @param extracted  indices of sub-matches to keep, in the order they
	 *                   should appear in the resulting list
	 */
    public SequenceReader(String identifier, List<String> readerIds, boolean skip, List<Integer> extracted) {
    	this(identifier, readerIds, skip);
    	this.patternType = EXTRACT_SOME;
		this.manyReturns = extracted;
    }

	/**
	 * Creates a sequence that returns its sub-matches as a named object.
	 *
	 * @param identifier the unique name of this rule in the grammar
	 * @param readerIds  the names of the readers to apply in order
	 * @param skip       whether to apply {@code skip} before the sequence
	 * @param extracted  map from sub-match indices to field names
	 */
    public SequenceReader(String identifier, List<String> readerIds, boolean skip, Map<Integer, String> extracted) {
    	this(identifier, readerIds, skip);
    	this.patternType = MAKE_OBJECT;
		this.objectReturns = extracted;
    }

	/**
	 * Applies each inner reader in order, threading the position.
	 * <ul>
	 *   <li>On the first failure, returns a failed {@link ListMatch} with
	 *       {@code start == end == start}.</li>
	 *   <li>If all readers succeed, delegates to {@link #makeMatch(int, int, boolean, List)}
	 *       to build the final result according to {@link #patternType}.</li>
	 * </ul>
	 */
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

	/**
	 * Builds the final match for this sequence, based on {@link #patternType}.
	 * <ul>
	 *   <li>{@link #RETURN_ALL}: return a {@link ListMatch} over all sub-matches.</li>
	 *   <li>{@link #EXTRACT_ONE}: return a copy of the selected sub-match,
	 *       with its span extended to the whole sequence.</li>
	 *   <li>{@link #EXTRACT_SOME}: return a {@link ListMatch} over selected
	 *       sub-matches.</li>
	 *   <li>{@link #MAKE_OBJECT}: return an {@link ObjectMatch} mapping field
	 *       names to selected sub-matches.</li>
	 * </ul>
	 */
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

	/**
	 * Resolves all inner readers by name.
	 */
    @Override
    protected void simpleLink(IParser parser) {
    	this.readers = new ArrayList<>();
    	for (String readerId : this.readerIds) {
    		this.readers.add(parser.getReader(readerId));
    	}
    }
}
