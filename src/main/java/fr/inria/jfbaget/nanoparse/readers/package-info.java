/**
 * Built-in reader implementations (grammar building blocks).
 * <p>
 * This package provides concrete {@link fr.inria.jfbaget.nanoparse.IReader}
 * implementations used to define grammars:
 * <ul>
 *   <li>Primitive readers, such as {@code StringReader} and {@code RegexReader},</li>
 *   <li>Combinators, such as {@code SequenceReader}, {@code ChoiceReader},
 *       {@code OptionalReader} and {@code RepetitionReader}.</li>
 * </ul>
 * These are assembled into grammars and registered in a
 * {@link fr.inria.jfbaget.nanoparse.Parser}.
 */
package fr.inria.jfbaget.nanoparse.readers;