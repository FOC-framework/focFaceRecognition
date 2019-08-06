package com.foc.faceRecognition.services.server.request;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.foc.faceRecognition.FaceRecognitionConfig;

public abstract class AbstractFaceRecognitionRequest {
	
	public AbstractFaceRecognitionRequest() {
	}

	public String getURL() {
		String url = FaceRecognitionConfig.getInstance().getWebServiceURL();
		if(url != null) {
			if(!url.endsWith("/")) {
				url += "/";
			}
			url = url + getURLSuffix();
		}
		return url;
	}
	
	public String getURLSuffix() {
		return "";
	}
	
	public InputStream sendPhoto(String fileName, InputStream inutStream) throws Exception {
    HttpURLConnection conn = (HttpURLConnection) new URL(getURL()).openConnection();
			
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
		
		return is;
	}
	
	public String getStringFromInputStream(InputStream is) throws IOException {
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	
		String line = null;
	
		StringBuffer response = new StringBuffer();
		while ((line = rd.readLine()) != null) {
			response.append(line);
			response.append('\r');
		}
		rd.close();
		String responseBody = response.toString();
		return responseBody;
	}
}
