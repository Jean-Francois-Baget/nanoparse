package fr.inria.jfbaget.nanoparse.matches;

import java.util.Map;

import org.json.JSONObject;

import fr.inria.jfbaget.nanoparse.IReader;
import fr.inria.jfbaget.nanoparse.IMatch;

public class ObjectMatch extends Match<Map<String, IMatch>>{
	
	public ObjectMatch(IReader reader, int start, int end, boolean success, Map<String, IMatch> result) {
		super(reader, start, end, success, result);
	}
	
	@Override
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		JSONObject result = new JSONObject();
		if (this.result != null) {
			for (Map.Entry<String, IMatch> entry : this.result.entrySet()) {
				result.put(entry.getKey(), entry.getValue().toJSON());
			}
		}
		json.put("result", result);
		return json;
		
	}

}