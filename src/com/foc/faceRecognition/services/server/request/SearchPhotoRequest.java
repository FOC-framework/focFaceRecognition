package com.foc.faceRecognition.services.server.request;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.foc.Globals;
import com.foc.faceRecognition.services.server.SimilarFace;

public class SearchPhotoRequest extends AbstractFaceRecognitionRequest {
	
	public SearchPhotoRequest() {
	}
	
	public String getURLSuffix() {
		return "search";
	}
	
	public ArrayList<SimilarFace> sendPhotoAndGetSimilars(String fileName, InputStream inutStream) throws Exception {
		ArrayList<SimilarFace> similars = new ArrayList<>(); 
		
		InputStream is = super.sendPhoto(fileName, inutStream);
		String responseBody = getStringFromInputStream(is);
		Globals.logString(responseBody);

		JSONObject jsonObj = new JSONObject(responseBody);
		
		JSONArray docsArray = jsonObj.getJSONArray("similar_faces");
		for(int i=0; i<docsArray.length(); i++) {
			JSONObject faceJSON = (JSONObject) docsArray.get(i);

			String refStr = faceJSON.getString("ref");
			long ref = Long.valueOf(refStr);
			double distance = faceJSON.getDouble("distance");
			SimilarFace face = new SimilarFace(ref, distance);
			similars.add(face);
		}
		
		return similars;
	}
}
