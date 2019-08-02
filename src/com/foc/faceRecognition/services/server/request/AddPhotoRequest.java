package com.foc.faceRecognition.services.server.request;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.bcel.generic.GETSTATIC;
import org.json.JSONArray;
import org.json.JSONObject;

import com.foc.Globals;
import com.foc.desc.FocConstructor;
import com.foc.faceRecognition.Face;

public class AddPhotoRequest extends AbstractFaceRecognitionRequest {
	
	public AddPhotoRequest() {
	}
	
	public String getURLSuffix() {
		return "upload";
	}
	
	public void sendPhotoAndSaveFaces(String fileName, InputStream inutStream) throws Exception {
		InputStream is = super.sendPhoto(fileName, inutStream);
		String responseBody = getStringFromInputStream(is);		
		Globals.logString(responseBody);
		
		JSONObject jsonObj = new JSONObject(responseBody);
		
		JSONArray docsArray = jsonObj.getJSONArray("faces");
		for(int i=0; i<docsArray.length(); i++) {
			JSONObject faceJSON = (JSONObject) docsArray.get(i);

			FocConstructor constr = new FocConstructor(Face.getFocDesc());
			Face           face   = (Face) constr.newItem();
			face.setCreated(true);
			face.parseJson(faceJSON);
			face.validate(true);
			face.dispose();
		}
	}
}
