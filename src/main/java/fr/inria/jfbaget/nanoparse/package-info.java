/**
 * Core NanoParse API: parser, readers and matches.
 * <p>
 * This package defines the main interfaces and classes used to build and run
 * parsers:
 * <ul>
 *   <li>{@link fr.inria.jfbaget.nanoparse.IParser} and {@link fr.inria.jfbaget.nanoparse.Parser}</li>
 *   <li>{@link fr.inria.jfbaget.nanoparse.IReader}</li>
 *   <li>{@link fr.inria.jfbaget.nanoparse.IMatch}</li>
 * </ul>
 * Typical usage is:
 * <ol>
 *   <li>Define a list of {@code IReader} rules.</li>
 *   <li>Create a {@link fr.inria.jfbaget.nanoparse.Parser} with them.</li>
 *   <li>Call {@code parser.read(input, 0)} to obtain an {@code IMatch}.</li>
 * </ol>
 */
package fr.inria.jfbaget.nanoparse;