package fr.inria.jfbaget.nanoparse.readers;


import java.util.ArrayList;
import java.util.List;

import fr.inria.jfbaget.nanoparse.IMatch;
import fr.inria.jfbaget.nanoparse.IParser;
import fr.inria.jfbaget.nanoparse.IReader;
import fr.inria.jfbaget.nanoparse.matches.ListMatch;

/**
 * Reader that matches a repeated pattern, optionally separated by another rule.
 * <p>
 * A {@code RepetitionReader} attempts to read:
 * <pre>
 *   element (separator element)*
 * </pre>
 * between {@link #min} and {@link #max} times.
 * <ul>
 *   <li>If the repetition succeeds within the bounds, it returns either:
 *     <ul>
 *       <li>a {@link ListMatch} containing all matches (and optionally separators), or</li>
 *       <li>the single element match itself when {@link #subReaderWhenOne} is true
 *           and only one repetition is present.</li>
 *     </ul>
 *   </li>
 *   <li>If the repetition fails to reach {@code min} occurrences, it fails with an
 *       empty match (span {@code [start, start)} and {@code result == null}).</li>
 * </ul>
 * Skip behaviour is controlled by the {@code skip} flag passed to the
 * constructor. If {@code skip} is {@code true}, the global {@code "skip"}
 * rule is applied before the repetition is evaluated (via
 * {@link AbstractReader#read(CharSequence, int)}). The element and separator
 * readers then handle their own skip behaviour independently.
 */
public class RepetitionReader extends AbstractReader{

	/** Resolved element reader. */
	public IReader reader;
	/** Name of the element rule, resolved at link time. */
	public String readerId;
	/** Name of the separator rule, resolved at link time (may be null). */
	public String separatorId;
	/** Resolved separator reader (may be null if {@link #separatorId} is null). */
	public IReader separator;
	/** Minimum number of repetitions required for success. */
	public final int min;
	/** Maximum number of repetitions allowed (0 means "always empty"). */
	public final int max;
	/**
	 * If {@code true} and exactly one repetition occurs, the result is the
	 * element match itself instead of a {@link ListMatch}.
	 */
	public final boolean subReaderWhenOne;
	/**
	 * If {@code true}, separator matches are stored alongside element matches
	 * in the result (as {@link ListMatch} of [separator, element] pairs).
	 * Otherwise only the element matches are returned.
	 */
	public final boolean storeSep;

	/**
	 * Creates a {@code RepetitionReader} with full control over behaviour.
	 *
	 * @param identifier        the unique name of this rule in the grammar
	 * @param readerId          the name of the element reader
	 * @param separatorId       the name of the separator reader, or {@code null}
	 *                          if no separator is used
	 * @param min               minimum number of repetitions required
	 * @param max               maximum number of repetitions allowed
	 * @param skip              {@code true} if the global {@code "skip"} rule
	 *                          should be applied before matching
	 * @param subReaderWhenOne  if {@code true} and exactly one element is read,
	 *                          return its match directly instead of a list
	 * @param storeSep          if {@code true}, include separators in the result
	 */
    public RepetitionReader(String identifier, String readerId, String separatorId, int min, int max, boolean skip, boolean subReaderWhenOne, boolean storeSep) {
        super(identifier, skip);
        this.readerId = readerId;
        this.separatorId = separatorId;
        this.min = min;
        this.max = max;
		this.subReaderWhenOne = subReaderWhenOne;
		this.storeSep = storeSep;
    }

	/**
	 * Convenience constructor with default {@code subReaderWhenOne = false}
	 * and {@code storeSep = false}.
	 */
	public RepetitionReader(String identifier, String readerId, String separatorId, int min, int max, boolean skip) {
		this(identifier, readerId,  separatorId, min, max, skip, false, false);
    }

	/**
	 * Reads one optional {@code separator} + {@code element} pair starting at
	 * {@code start}.
	 * <ul>
	 *   <li>If there is no separator rule, it directly tries an element.</li>
	 *   <li>If separator and element both succeed:
	 *     <ul>
	 *       <li>returns a {@link ListMatch} of [separator, element] if
	 *           {@link #storeSep} is true;</li>
	 *       <li>otherwise returns the element match itself.</li>
	 *     </ul>
	 *   </li>
	 *   <li>If either separator or element fails, returns {@code null} to signal
	 *       that the repetition should stop.</li>
	 * </ul>
	 */
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

	/**
	 * Attempts to read the repeated pattern within the allowed bounds.
	 */
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
    }

	/**
	 * Resolves the element and separator readers by name.
	 */
    @Override
    protected void simpleLink(IParser parser) {
    	this.reader = parser.getReader(this.readerId);	
    	if (this.separatorId != null) {
    		this.separator = parser.getReader(this.separatorId);
    	}
    }
}
