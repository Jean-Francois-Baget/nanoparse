package fr.inria.boreal.jfbaget.nanoparse.basetypes;

import java.util.List;

import fr.inria.boreal.jfbaget.nanoparse.readers.ChoiceReader;

public class BaseTypeReader extends ChoiceReader {
	
	public BaseTypeReader() {
		super("BASETYPE", List.of("BOOL", "ID", "STRING", "NUMBER"), false);
	}
}
