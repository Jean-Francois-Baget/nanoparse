package fr.inria.boreal.jfbaget.nanoparse;

import java.util.List;


/**
 * 
 */
public interface IParser {
	
	/**
	 * Reads input beginning at start
	 * @param input
	 * @param start
	 * @param readerName
	 * @return 
	 */
	public IMatch read(CharSequence input, int start, String readerName);
	
	public IMatch read(CharSequence input, int start);
	
	public List<String> getReaderNames();
	
	public IReader getReader(String readerName);
	
	public void setDefaultReaderName(String readerName);
	
	public String getDefaultReaderName();

}