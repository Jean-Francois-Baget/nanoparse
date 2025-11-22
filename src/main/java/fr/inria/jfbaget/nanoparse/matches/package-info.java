/**
 * Result types produced by NanoParse readers.
 * <p>
 * This package defines concrete implementations of
 * {@link fr.inria.jfbaget.nanoparse.IMatch} used to represent parse results.
 * <p>
 * The central abstraction is {@link fr.inria.jfbaget.nanoparse.matches.Match},
 * a generic, immutable match which stores:
 * <ul>
 *   <li>the {@link fr.inria.jfbaget.nanoparse.IReader} that produced it,</li>
 *   <li>the start and end indices in the input,</li>
 *   <li>a success flag, and</li>
 *   <li>a typed semantic result value.</li>
 * </ul>
 * <p>
 * Specialized subclasses provide convenient typed access for common cases:
 * <ul>
 *   <li>{@link fr.inria.jfbaget.nanoparse.matches.StringMatch} – for string values,</li>
 *   <li>{@link fr.inria.jfbaget.nanoparse.matches.IntMatch} – for integer literals,</li>
 *   <li>{@link fr.inria.jfbaget.nanoparse.matches.FloatMatch} – for floating-point literals,</li>
 *   <li>{@link fr.inria.jfbaget.nanoparse.matches.BoolMatch} – for boolean literals,</li>
 *   <li>{@link fr.inria.jfbaget.nanoparse.matches.ListMatch} – for sequences or repetitions
 *       of sub-matches,</li>
 *   <li>{@link fr.inria.jfbaget.nanoparse.matches.ObjectMatch} – for structured objects
 *       built from named sub-matches.</li>
 * </ul>
 * <p>
 * All match implementations are immutable. Some readers may adjust spans or
 * aggregate sub-matches by creating new instances via the {@code copyWith}
 * pattern. For debugging and inspection, every match can be converted to a
 * JSON representation via {@link fr.inria.jfbaget.nanoparse.IMatch#toJSON()}.
 */
package fr.inria.jfbaget.nanoparse.matches;