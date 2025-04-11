package fr.inria.boreal.jfbaget.nanoparse.exceptions;

public class InvalidIdentifierException extends RuntimeException {
	
	
	private static final long serialVersionUID = 1L;

	public InvalidIdentifierException(String identifier) {
        super("Cannot add a reader with identifier " + identifier  + ", reserved for basetypes.");
    }

}
