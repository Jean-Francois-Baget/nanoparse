/**
 * Built-in readers for common lexical base types.
 * <p>
 * This package provides ready-made
 * {@link fr.inria.jfbaget.nanoparse.IReader} implementations for frequently
 * used token categories such as identifiers, numbers and strings. They are
 * registered automatically in every
 * {@link fr.inria.jfbaget.nanoparse.Parser} via
 * {@link fr.inria.jfbaget.nanoparse.basetypes.BaseTypes}, which also
 * reserves their names so user-defined rules cannot clash with them.
 * <p>
 * The main entry point is {@link fr.inria.jfbaget.nanoparse.basetypes.BaseTypes},
 * which exposes:
 * <ul>
 *   <li>the list of reserved base type names, and</li>
 *   <li>a factory method to create the corresponding readers.</li>
 * </ul>
 * <p>
 * Concrete base type readers include:
 * <ul>
 *   <li>{@link fr.inria.jfbaget.nanoparse.basetypes.BoolReader} –
 *       boolean literals ({@code true}, {@code false}),</li>
 *   <li>{@link fr.inria.jfbaget.nanoparse.basetypes.IntReader} –
 *       signed decimal integers,</li>
 *   <li>{@link fr.inria.jfbaget.nanoparse.basetypes.FloatReader} –
 *       permissive numeric literals (integers or floats),</li>
 *   <li>{@link fr.inria.jfbaget.nanoparse.basetypes.StrictFloatReader} –
 *       stricter floating-point literals (excluding bare integers),</li>
 *   <li>{@link fr.inria.jfbaget.nanoparse.basetypes.NumberReader} –
 *       choice of {@code STRICTFLOAT} or {@code INT},</li>
 *   <li>{@link fr.inria.jfbaget.nanoparse.basetypes.IDReader} –
 *       identifier tokens,</li>
 *   <li>{@link fr.inria.jfbaget.nanoparse.basetypes.QuotedStringReader} –
 *       double-quoted string literals,</li>
 *   <li>{@link fr.inria.jfbaget.nanoparse.basetypes.BaseTypeReader} –
 *       generic base type rule combining several of the above.</li>
 * </ul>
 * <p>
 * These base readers are intended to cover the most common lexical needs.
 * Users can still define their own {@link fr.inria.jfbaget.nanoparse.readers.RegexReader}
 * or other custom readers for specialised token types.
 */
package fr.inria.jfbaget.nanoparse.basetypes;