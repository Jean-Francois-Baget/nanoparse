package fr.inria.boreal.jfbaget.nanoparse;

import org.json.JSONObject;

public interface IMatch {

    public boolean success();
    
    public IReader reader();
    
    public int start();
    
    public int end();
    
    public void setEnd(int end);
    
    public Object result();
    
    public JSONObject toJSON();
    
}
