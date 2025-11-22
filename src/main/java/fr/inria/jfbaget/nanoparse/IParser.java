package fr.inria.jfbaget.nanoparse;

import java.util.List;


/**
 * Entry point for parsing with NanoParse.
 * <p>
 * An {@code IParser} manages a set of named {@link IReader readers}
 * (grammar rules). Each call to {@link #read(CharSequence, int, String)}
 * chooses one of these rules as an entry point and returns an
 * {@link IMatch} that describes either a successful or failed match.
 * <p>
 * Success and failure are always encoded inside the {@code IMatch}:
 * <ul>
 *   <li>On success, the match covers a span of the input from
 *       {@code start} (inclusive) to {@code end} (exclusive), and
 *       may contain nested sub-matches for sub-rules.</li>
 *   <li>On failure, the match has {@code success() == false} and
 *       typically {@code start == end} at the position where the
 *       rule was attempted.</li>
 * </ul>
 */
public interface IParser {

	/**
	 * Parses {@code input} starting at the given index, using the rule
	 * identified by {@code readerName} as entry point.
	 * <p>
	 * The returned {@link IMatch} always reflects the outcome of the
	 * attempt:
	 * <ul>
	 *   <li>If the rule can be matched at {@code start}, the match is
	 *       marked successful and its {@code start}/{@code end}
	 *       delimit the consumed span.</li>
	 *   <li>If the rule cannot be matched at {@code start}, the match
	 *       is marked as a failure and usually has {@code start == end}
	 *       at the position where parsing failed.</li>
	 * </ul>
	 *
	 * @param input      the character sequence to parse
	 * @param start      zero-based index of the first character to read
	 * @param readerName the name of the rule (reader) to use as entry point
	 * @return an {@link IMatch} describing a successful or failed match
	 * @throws fr.inria.jfbaget.nanoparse.exceptions.InvalidIdentifierException
	 *         if no reader with the given name exists
	 */
	public IMatch read(CharSequence input, int start, String readerName);

	/**
	 * Parses {@code input} starting at {@code start}, using the current
	 * default reader as entry point.
	 * <p>
	 * This is equivalent to:
	 * <pre>{@code
	 * parser.read(input, start, parser.getDefaultReaderName());
	 * }</pre>
	 *
	 * @param input the character sequence to parse
	 * @param start zero-based index of the first character to read
	 * @return an {@link IMatch} describing a successful or failed match
	 *         with the default rule
	 * @throws fr.inria.jfbaget.nanoparse.exceptions.InvalidIdentifierException
	 *         if the default reader name does not refer to an existing rule
	 */
	public IMatch read(CharSequence input, int start);

	/**
	 * Returns the names of all readers (grammar rules) known to this parser.
	 *
	 * @return an unmodifiable list of all reader names; the order is
	 *         implementation-dependent
	 */
	public List<String> getReaderNames();

	/**
	 * Returns the reader (sub-parser) associated with the given name.
	 *
	 * @param readerName the name of the reader to look up
	 * @return the corresponding {@link IReader}
	 * @throws fr.inria.jfbaget.nanoparse.exceptions.InvalidIdentifierException
	 *         if no reader with the given name exists
	 */
	public IReader getReader(String readerName);

	/**
	 * Changes the default reader used by
	 * {@link #read(CharSequence, int)}.
	 *
	 * @param readerName the name of an existing reader to use as default
	 * @throws fr.inria.jfbaget.nanoparse.exceptions.InvalidIdentifierException
	 *         if no reader with the given name exists
	 */
	public void setDefaultReaderName(String readerName);

	/**
	 * Returns the name of the current default reader.
	 *
	 * @return the name of the default reader
	 */
	public String getDefaultReaderName();

}