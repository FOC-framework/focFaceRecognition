package com.foc.faceRecognition.services.server.request;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.foc.Globals;
import com.foc.desc.FocConstructor;
import com.foc.faceRecognition.Face;
import com.foc.faceRecognition.TreatedImage;

public class AddPhotoRequest extends AbstractFaceRecognitionRequest {
	
	public AddPhotoRequest() {
	}
	
	public String getURLSuffix() {
		return "upload";
	}
	
	public void sendPhotoAndSaveFaces(long photoAlbumRef, String fileName, InputStream inutStream) throws Exception {
		InputStream is = super.sendPhoto(fileName, inutStream);
		String responseBody = getStringFromInputStream(is);		
		Globals.logString(responseBody);
		
		addRefToTreatedTable(photoAlbumRef);
		
		JSONObject jsonObj = new JSONObject(responseBody);
		
		JSONArray docsArray = jsonObj.getJSONArray("faces");
		for(int i=0; i<docsArray.length(); i++) {
			JSONObject faceJSON = (JSONObject) docsArray.get(i);

			FocConstructor constr = new FocConstructor(Face.getFocDesc());
			Face           face   = (Face) constr.newItem();
			face.setCreated(true);
			face.parseJson(faceJSON);
			face.setPhotoAlbumRef(photoAlbumRef);
			face.validate(true);
			face.dispose();
		}
	}
	
	private void addRefToTreatedTable(long photoAlbumRef){
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT \"TreatedRef\" FROM \"TreatedImage\" WHERE \"TreatedRef\" = "+photoAlbumRef);
		ArrayList<String> returns = Globals.getApp().getDataSource().command_SelectRequest(buffer);
		if(returns.size() == 0) {
			//Save in Treated Image
			FocConstructor constr  = new FocConstructor(TreatedImage.getFocDesc());
			TreatedImage   treated = (TreatedImage) constr.newItem();
			treated.setCreated(true);
			treated.setTreatedRef(photoAlbumRef);
			treated.validate(true);
			treated.dispose();
			//---------------------
		}
	}
}
