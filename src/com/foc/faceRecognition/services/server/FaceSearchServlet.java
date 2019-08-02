package com.foc.faceRecognition.services.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.foc.Globals;
import com.foc.business.photoAlbum.PhotoAlbum;
import com.foc.business.photoAlbum.PhotoAlbumListWithFilter;
import com.foc.desc.FocConstructor;
import com.foc.faceRecognition.Face;
import com.foc.list.FocList;
import com.foc.util.Utils;
import com.foc.web.microservice.FocMicroServlet;

public class FaceSearchServlet extends FocMicroServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SessionAndApplication sessionAndApp = pushSession(request, response);

		PhotoAlbumListWithFilter list = new PhotoAlbumListWithFilter();
		list.loadIfNotLoadedFromDB();
		
		int nbrOfImagesSent = 0;
		
		for(int i=0; i<list.size() && nbrOfImagesSent < 1; i++) {
			PhotoAlbum photo = (PhotoAlbum) list.getFocObject(i);
			if(photo != null && !Utils.isStringEmpty(photo.getImageName()) && 
					(		photo.getImageName().toLowerCase().endsWith(".jpg") 
					 || photo.getImageName().toLowerCase().endsWith(".png")
					 )
					){
				InputStream inutStream = photo.getImageCloud();
				try{
					sendPOST(photo.getImageName(), inutStream);
				}catch (Exception e){
					Globals.logException(e);
				}
				nbrOfImagesSent++;
			}
		}
		
		list.dispose();
		list = null;

//		FxLogSubmitter submitter = FxLogSubmitter.getInstance();
//		submitter.startThread(sessionAndApp.getWebApplication(), FocWebServer.getInstance());

		//response.setContentType("application/json");
		response.setHeader("Content-Type", "application/json; charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().println("{}");//My JSON

//		if(sessionAndApp != null){
//			sessionAndApp.logout();
//		}
	}

	@Override
	protected String getUIClassName() {
		return "siren.isf.fenix.main.FenixUI";
	}

	private void sendPOST(String fileName, InputStream inutStream) throws Exception {
    HttpURLConnection conn = (HttpURLConnection) new URL("http://localhost:5000/upload").openConnection();
			
		// some arbitrary text for multitext boundary
		// only 7-bit US-ASCII digits max length 70
		String boundary_string = "some radom/arbitrary text";

		// we want to write out
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.addRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary_string);

		// now we write out the multipart to the body
		OutputStream conn_out = conn.getOutputStream();
		BufferedWriter conn_out_writer = new BufferedWriter(new OutputStreamWriter(conn_out));
		// write out multitext body based on w3 standard
		// https://www.w3.org/Protocols/rfc1341/7_2_Multipart.html
		conn_out_writer.write("\r\n--" + boundary_string + "\r\n");
		conn_out_writer.write("Content-Disposition: form-data; " +
		        "name=\"image\"; " +
		        "filename=\""+fileName+"\"" +
		        "\r\n\r\n");
		conn_out_writer.flush();

		// payload from the file
		
		// write direct to outputstream instance, because we write now bytes and not strings
		int read_bytes;
		byte[] buffer = new byte[1024];
		while((read_bytes = inutStream.read(buffer)) != -1) {
			conn_out.write(buffer, 0, read_bytes);
		}
		conn_out.flush();
		// close multipart body
		conn_out_writer.write("\r\n--" + boundary_string + "--\r\n");
		conn_out_writer.flush();
		
		// close all the streams
		conn_out_writer.close();
		conn_out.close();
		
//		file_stream.close();
		// execute and get response code
		conn.getResponseCode();
		InputStream is = conn.getInputStream();
		
		//Reading the response -> String
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));

		String line = null;

		StringBuffer response = new StringBuffer();
		while ((line = rd.readLine()) != null) {
			response.append(line);
			response.append('\r');
		}
		rd.close();
		String responseBody = response.toString();
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
