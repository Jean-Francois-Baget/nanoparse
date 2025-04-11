package fr.inria.boreal.jfbaget.nanoparse.basetypes;

import java.util.List;

import fr.inria.boreal.jfbaget.nanoparse.readers.ChoiceReader;

public class NumberReader extends ChoiceReader {
	
	public NumberReader() {
		super("NUMBER", List.of("STRICTFLOAT", "INT"), false);
	}
}
