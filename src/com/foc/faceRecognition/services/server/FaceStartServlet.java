package com.foc.faceRecognition.services.server;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.foc.Globals;
import com.foc.business.photoAlbum.PhotoAlbum;
import com.foc.business.photoAlbum.PhotoAlbumListWithFilter;
import com.foc.faceRecognition.services.server.request.AddPhotoRequest;
import com.foc.util.Utils;
import com.foc.web.microservice.FocMicroServlet;

public class FaceStartServlet extends FocMicroServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SessionAndApplication sessionAndApp = pushSession(request, response);

		pushFacesFromPhotoAlbum();

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
	
	public static void pushFacesFromPhotoAlbum() {
		PhotoAlbumListWithFilter list = new PhotoAlbumListWithFilter();
		list.loadIfNotLoadedFromDB();
		
		for(int i=0; i<list.size(); i++) {
			PhotoAlbum photo = (PhotoAlbum) list.getFocObject(i);
			if(photo != null && !Utils.isStringEmpty(photo.getImageName()) && 
					(		photo.getImageName().toLowerCase().endsWith(".jpg") 
					 || photo.getImageName().toLowerCase().endsWith(".png")
					 || photo.getImageName().toLowerCase().endsWith(".jpeg")
					 || photo.getImageName().toLowerCase().endsWith(".gif")
					 )
					){
				InputStream inutStream = photo.getImageCloud();
				try{
					AddPhotoRequest requ = new AddPhotoRequest();
					int lastDot = photo.getImageName().lastIndexOf(".");
					if(lastDot > 0) {
						String extension = photo.getImageName().substring(lastDot);
						String imageName = photo.getReferenceInt() + extension;
						requ.sendPhotoAndSaveFaces(imageName, inutStream);
					}
				}catch (Exception e){
					Globals.logException(e);
				}
			}
		}
		
		list.dispose();
		list = null;
	}

	@Override
	protected String getUIClassName() {
		return "siren.isf.fenix.main.FenixUI";
	}
}
