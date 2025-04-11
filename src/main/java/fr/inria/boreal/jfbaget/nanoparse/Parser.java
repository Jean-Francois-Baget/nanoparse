package fr.inria.boreal.jfbaget.nanoparse;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import fr.inria.boreal.jfbaget.nanoparse.basetypes.BaseTypes;
import fr.inria.boreal.jfbaget.nanoparse.exceptions.InvalidIdentifierException;
import fr.inria.boreal.jfbaget.nanoparse.readers.RegexReader;


/*
 * TO DO: vérifier que toutes les règles sont là. En fait, le get de la Hash Map retourne null, 
 * ce qui initialise les readers à null.
 * 
 */

public class Parser implements IParser{
	
	private HashMap<String, IReader> readers = new HashMap<>();
	
	private List<String> readerNames = new ArrayList<>();
	
	private String defaultReaderName = "main";

	public Parser(List<IReader> readers) {
		
		this.addBaseTypes();
		
		for (IReader reader : readers) {
			if (BaseTypes.getNames().contains(reader.getName()))
				throw new InvalidIdentifierException(reader.getName());
			else
				this.addReader(reader);
		}
		// add default skipreader if not overriden in the provided readers
		if (!this.readers.containsKey("skip")) {
			this.addReader(new RegexReader("skip", "[\s\r\n]*", false));
		}
		for (IReader reader : this.readers.values()) {
			reader.link(this);
		}
	}
	
	
	@Override
	public IMatch read(CharSequence input, int start, String readerName) {
		return this.readers.get(readerName).read(input, start);
	}
	
	@Override
	public IMatch read(CharSequence input, int start) {
		return this.read(input, start, this.getDefaultReaderName());
	}
	
	@Override
	public List<String> getReaderNames() {
		return Collections.unmodifiableList(this.readerNames);
	}
	
	@Override
	public IReader getReader(String readerName) {
		IReader reader = this.readers.get(readerName);
		if (reader == null)
			throw new RuntimeException("No such reader " + readerName);
		return reader;
	}
	
	@Override
	public void setDefaultReaderName(String readerName) {
		this.defaultReaderName = readerName;
	}
	
	@Override
	public String getDefaultReaderName() {
		return this.defaultReaderName;
	}
	
	private void addReader(IReader reader) {
		String name = reader.getName();
		if (this.readers.containsKey(name)) {
			
		}
		this.readers.put(name, reader);
		this.readerNames.add(name);
	}
	
	private void addBaseTypes() {
		for (String name : BaseTypes.getNames()) {
			this.addReader(BaseTypes.create(name));
		}
	}
}
