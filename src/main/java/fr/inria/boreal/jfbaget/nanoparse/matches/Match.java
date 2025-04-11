package fr.inria.boreal.jfbaget.nanoparse.matches;

import org.json.JSONObject;

import fr.inria.boreal.jfbaget.nanoparse.IMatch;
import fr.inria.boreal.jfbaget.nanoparse.IReader;

public class Match<ResType> implements IMatch {

    private IReader reader;
    private int start;
    private int end;
    private boolean success;
    public ResType result;

    public Match(IReader reader, int start, int end, boolean success, ResType result) {
        this.reader = reader;
        this.start = start;
        this.end = end;
        this.success = success;
        this.result = result;
    }

    @Override
    public boolean success() {
        return this.success;
    }
    
    @Override
    public IReader reader() {
    	return this.reader;
    }
    
    @Override
    public int start() {
    	return this.start;
    }
    
    @Override
    public int end() {
    	return this.end;
    }
    
    @Override
    public void setEnd(int end) {
    	this.end = end;
    }
    
    @Override
    public ResType result() {
    	return this.result;
    }
    
    @Override
    public JSONObject toJSON()  {
    	JSONObject json = new JSONObject();
    	json.put("success", this.success);
    	json.put("rule", this.reader().getName());
    	json.put("start", this.start);
    	json.put("end", this.end);
    	return json;
    	
    }
    
}
