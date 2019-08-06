package com.foc.faceRecognition.services.server.request;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import com.foc.Globals;
import com.foc.faceRecognition.Face;
import com.foc.list.FocList;

public class SendFacesInitRequest extends AbstractFaceRecognitionRequest {
	
	public SendFacesInitRequest() {
	}
	
	public String getURLSuffix() {
		return "init";
	}
	
	public void sendFaces() throws Exception {
		
		JSONObject jsonMain = new JSONObject();
		JSONArray arr1 = new JSONArray();
		
		FocList list = Face.getFocDesc().newFocList();
		if(list != null) {
			list.loadIfNotLoadedFromDB();
			for(int i=0; i<list.size(); i++) {
				Face face = (Face) list.getFocObject(i);
				if(face != null) {
					JSONObject jsonFace = new JSONObject();
					
					jsonFace.put("ref", face.getPhotoAlbumRef());
					jsonFace.put("top", face.getTop());
					jsonFace.put("bottom", face.getBottom());
					jsonFace.put("left", face.getLeft());
					jsonFace.put("right", face.getRight());
					jsonFace.put("encoding", face.getEncoding());
					
					arr1.put(jsonFace);
				}
			}
			list.dispose();
		}
		jsonMain.put("faces", arr1);
		
//		String sentJson = request.toString();
//		JSONObject json1 = new JSONObject();
//		json1.put("name", "antoine");
//		json1.put("family", "samaha");
//		JSONArray arr1 = new JSONArray();
//		arr1.put(json1);
//		
//		
//		jsonMain.put("test", "tsting1");
//		jsonMain.put("firstArray", arr1);
////		String sentJson = "{\"test\":\"testing\",\"test2\":[{\"v1\"=\"antoine\",\"v2\":\"samaha\"}]}";
		String sentJson = jsonMain.toString();
		Globals.logString(sentJson);
		
		URL               url        = new URL(getURL());
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		connection.setRequestMethod("POST");
		
		connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
		connection.setRequestProperty("Content-Length", "" + Integer.toString(sentJson.getBytes().length));
		connection.setRequestProperty("Content-Language", "en-US");
		connection.setRequestProperty("Accept", "application/json");
		
		connection.setUseCaches(false);
		connection.setDoInput(true);
		connection.setDoOutput(true);
		
		// Send request
		DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
		
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
		writer.write(sentJson);
		writer.close();
		
		wr.flush();
		wr.close();

		// Get Response
		InputStream is = connection.getInputStream();
		String response = getStringFromInputStream(is);
		Globals.logString("Response="+response);
	}
	
}
