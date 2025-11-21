package fr.inria.jfbaget.nanoparse;


public interface IReader {

    public IMatch read(CharSequence input, int start);

    public String getName();
    
    public boolean requiresSkip();
    
    public void link(IParser parser);
    
}
