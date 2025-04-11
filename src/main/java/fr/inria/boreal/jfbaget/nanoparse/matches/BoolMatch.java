package fr.inria.boreal.jfbaget.nanoparse.matches;

import org.json.JSONObject;

import fr.inria.boreal.jfbaget.nanoparse.IReader;

public class BoolMatch extends Match<Boolean> {
	
	public BoolMatch(IReader reader, int start, int end, boolean success, String result) {
		super(reader, start, end, success, Boolean.parseBoolean(result));
	}
	
	@Override
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("result", this.result);
		return json;
		
	}
}
