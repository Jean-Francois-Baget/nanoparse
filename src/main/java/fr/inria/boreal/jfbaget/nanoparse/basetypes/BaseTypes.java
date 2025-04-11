package fr.inria.boreal.jfbaget.nanoparse.basetypes;


import fr.inria.boreal.jfbaget.nanoparse.IReader;

import java.util.List;

public class BaseTypes {
	
	
	private static List<String> readerIds = List.of("BASETYPE", "BOOL", "FLOAT", "STRICTFLOAT", 
			                                        "ID", "INT", "NUMBER", "STRING");
			
	public static List<String> getNames() {
		return readerIds;
	}
	
	public static IReader create(String readerId) {
		switch (readerId) {
			case "BASETYPE":
				return new BaseTypeReader();
			case "BOOL":
				return new BoolReader();
			case "FLOAT":
				return new FloatReader();
			case "STRICTFLOAT":
				return new StrictFloatReader();
			case "ID":
				return new IDReader();
			case "INT":
				return new IntReader();
			case "NUMBER":
				return new NumberReader();
			case "STRING":
				return new QuotedStringReader();
			default: // should add an exception here
				return null;
		}
	}

}
