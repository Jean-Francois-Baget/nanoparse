package fr.inria.jfbaget.nanoparse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import fr.inria.jfbaget.nanoparse.basetypes.BaseTypes;
import fr.inria.jfbaget.nanoparse.exceptions.InvalidIdentifierException;
import fr.inria.jfbaget.nanoparse.readers.RegexReader;


/**
 * Default implementation of {@link IParser} for NanoParse grammars.
 * <p>
 * A {@code Parser} is constructed from a list of {@link IReader readers}
 * representing the user-defined grammar rules. It also automatically
 * registers the built-in base types from {@link BaseTypes}, and ensures
 * that all rule names are unique:
 * <ul>
 *   <li>User-defined rule names must not clash with base type names,</li>
 *   <li>User-defined rule names must not clash with each other.</li>
 * </ul>
 * <p>
 * The parser also ensures that a {@code "skip"} rule exists:
 * <ul>
 *   <li>If the user provides a reader named {@code "skip"}, it is used.</li>
 *   <li>Otherwise, a default {@link RegexReader} is added that matches
 *       whitespace and line breaks ({@code "[\\s\\r\\n]*"}).</li>
 * </ul>
 * Individual readers decide, via their own configuration, whether they
 * call the {@code "skip"} rule before attempting their own match; a grammar
 * can therefore define {@code "skip"} and still choose not to use it.
 * <p>
 * The default entry rule is chosen as follows:
 * <ul>
 *   <li>If a rule named {@code "main"} exists, it becomes the default.</li>
 *   <li>Otherwise, the first user-defined rule in the constructor list
 *       becomes the default.</li>
 * </ul>
 * The default reader can later be changed via
 * {@link #setDefaultReaderName(String)}.
 * <p>
 * A {@code Parser} instance can be reused for multiple {@link #read}
 * calls in sequence, but it is not guaranteed to be thread-safe and
 * should not be shared between threads without external synchronization.
 */
public class Parser implements IParser{
	
	private HashMap<String, IReader> readers = new HashMap<>();
	
	private List<String> readerNames = new ArrayList<>();
	
	private String defaultReaderName = "main";

	/**
	 * Creates a new parser from the given list of user-defined readers.
	 * <p>
	 * This constructor:
	 * <ol>
	 *   <li>Registers all base types from {@link BaseTypes}.</li>
	 *   <li>Registers each reader from the {@code readers} list, enforcing:
	 *       <ul>
	 *         <li>No name may clash with a base type name.</li>
	 *         <li>No name may be used by more than one user-defined reader.</li>
	 *       </ul>
	 *   </li>
	 *   <li>Determines the default reader:
	 *       <ul>
	 *         <li>If a rule named {@code "main"} exists, it becomes the default.</li>
	 *         <li>Otherwise, the first user-defined rule becomes the default.</li>
	 *       </ul>
	 *   </li>
	 *   <li>Ensures that a {@code "skip"} rule exists, adding a default
	 *       whitespace-matching {@link RegexReader} if needed.</li>
	 *   <li>Calls {@link IReader#link(IParser)} on all readers so they can
	 *       resolve references to other rules in the grammar.</li>
	 * </ol>
	 *
	 * @param readers the user-defined readers forming the grammar
	 * @throws IllegalArgumentException if {@code readers} is {@code null}
	 *         or empty
	 * @throws InvalidIdentifierException if a reader name clashes with a
	 *         base type name or with another reader name
	 */
	public Parser(List<IReader> readers) {

		if (readers == null || readers.isEmpty()) {
			throw new IllegalArgumentException("Parser requires at least one reader");
		}

		this.addBaseTypes();

		String firstUserRuleName = null;
		for (IReader reader : readers) {
			String name = reader.getName();
			if (BaseTypes.getNames().contains(name) || this.readers.containsKey(name))
				throw new InvalidIdentifierException(reader.getName());
			if (firstUserRuleName == null)
				firstUserRuleName = name;
			this.addReader(reader);
		}
		if (! this.readers.containsKey("main")) {
			this.defaultReaderName = firstUserRuleName;
		}
		// add default skipreader if not overriden in the provided readers
		if (!this.readers.containsKey("skip")) {
			this.addReader(new RegexReader("skip", "[\s\r\n]*", false));
		}
		for (IReader reader : this.readers.values()) {
			reader.link(this);
		}
	}

	/**
	 * Parses the input starting at {@code start} using the reader
	 * identified by {@code readerName} as entry point.
	 *
	 * @param input      the character sequence to parse
	 * @param start      zero-based index of the first character to read
	 * @param readerName the name of the rule to use as entry point
	 * @return an {@link IMatch} describing a successful or failed match
	 * @throws InvalidIdentifierException if no reader with the given name exists
	 */
	@Override
	public IMatch read(CharSequence input, int start, String readerName) {
		return this.readers.get(readerName).read(input, start);
	}

	/**
	 * Parses the input starting at {@code start} using the default reader
	 * as entry point.
	 *
	 * @param input the character sequence to parse
	 * @param start zero-based index of the first character to read
	 * @return an {@link IMatch} describing a successful or failed match
	 * @throws InvalidIdentifierException if the default reader name does not
	 *         refer to an existing rule
	 */
	@Override
	public IMatch read(CharSequence input, int start) {
		return this.read(input, start, this.getDefaultReaderName());
	}

	/**
	 * Returns the names of all readers (base types and user-defined rules)
	 * known to this parser.
	 *
	 * @return an unmodifiable list of all reader names
	 */
	@Override
	public List<String> getReaderNames() {
		return Collections.unmodifiableList(this.readerNames);
	}

	/**
	 * Returns the reader associated with the given name.
	 *
	 * @param readerName the name of the reader to look up
	 * @return the corresponding {@link IReader}
	 * @throws InvalidIdentifierException if no reader with the given name exists
	 */
	@Override
	public IReader getReader(String readerName) {
		IReader reader = this.readers.get(readerName);
		if (reader == null)
			throw new InvalidIdentifierException(readerName);
		return reader;
	}


	/**
	 * Changes the default reader used when calling
	 * {@link #read(CharSequence, int)}.
	 *
	 * @param readerName the name of an existing reader
	 * @throws InvalidIdentifierException if no reader with the given name exists
	 */
	@Override
	public void setDefaultReaderName(String readerName) {
		if (!this.readers.containsKey(readerName)) {
			throw new InvalidIdentifierException(readerName);
		}
		this.defaultReaderName = readerName;
	}

	/**
	 * Returns the name of the current default reader.
	 *
	 * @return the default reader name
	 */
	@Override
	public String getDefaultReaderName() {
		return this.defaultReaderName;
	}

	/** Registers a reader under its name. Assumes name uniqueness was checked. */
	private void addReader(IReader reader) {
		String name = reader.getName();

		this.readers.put(name, reader);
		this.readerNames.add(name);
	}

	/** Registers all base type readers from {@link BaseTypes}. */
	private void addBaseTypes() {
		for (String name : BaseTypes.getNames()) {
			this.addReader(BaseTypes.create(name));
		}
	}
}
