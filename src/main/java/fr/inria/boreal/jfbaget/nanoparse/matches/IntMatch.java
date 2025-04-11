package fr.inria.boreal.jfbaget.nanoparse.matches;


import org.json.JSONObject;

import fr.inria.boreal.jfbaget.nanoparse.IReader;

public class IntMatch extends Match<Integer> {
	
	public IntMatch(IReader reader, int start, int end, boolean success, String result) {
		super(reader, start, end, success, Integer.parseInt(result));
	}
	
	@Override
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("result", this.result);
		return json;
		
	}
}
