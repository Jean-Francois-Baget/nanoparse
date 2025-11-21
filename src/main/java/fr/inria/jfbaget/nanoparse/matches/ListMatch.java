package fr.inria.jfbaget.nanoparse.matches;


import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import fr.inria.jfbaget.nanoparse.IReader;
import fr.inria.jfbaget.nanoparse.IMatch;

public class ListMatch extends Match<List<IMatch>>{
	
	public ListMatch(IReader reader, int start, int end, boolean success, List<IMatch> result) {
		super(reader, start, end, success, result);
	}
	
	@Override
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		
		if (this.result == null) {
			json.put("result", new JSONArray());
		} else {
			List<Object> map = this.result.stream()
                .map(IMatch::toJSON)
                .collect(Collectors.toList());
			JSONArray jsonresult = new JSONArray(map);
			json.put("result", jsonresult);
		}
		return json;
		
	}

}
