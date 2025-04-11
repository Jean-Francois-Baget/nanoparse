package fr.inria.boreal.jfbaget.nanoparse.readers;

import fr.inria.boreal.jfbaget.nanoparse.IMatch;
import fr.inria.boreal.jfbaget.nanoparse.IParser;
import fr.inria.boreal.jfbaget.nanoparse.IReader;
import fr.inria.boreal.jfbaget.nanoparse.Parser;
import fr.inria.boreal.jfbaget.nanoparse.matches.Match;

abstract class AbstractReader implements IReader{

    private final String name;
    private final boolean skip;
    private IReader skipReader;

    public AbstractReader(String name, boolean skip) {
        this.name = name;
        this.skip = skip;
    }

    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public boolean requiresSkip() {
    	return this.skip;
    }

    @Override
    public IMatch read(CharSequence input, int start) {
    	int position = start;
    	if (this.skip) {
    		IMatch match = this.skipReader.read(input, start);
    		position = match.end();
    	}
        return this.simpleread(input, position);
    }
    

    protected abstract IMatch simpleread(CharSequence input, int start);
    
    @Override
    public void link(IParser parser) {
    	this.simpleLink(parser);
    	this.skipReader = parser.getReader("skip");
    	
    }
    
    protected abstract void simpleLink(IParser parser);
}