package com.foc.faceRecognition.gui;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;

import com.foc.Globals;
import com.foc.business.photoAlbum.PhotoAlbum;
import com.foc.business.photoAlbum.PhotoAlbumDesc;
import com.foc.desc.FocConstructor;
import com.foc.faceRecognition.services.server.SimilarFace;
import com.foc.faceRecognition.services.server.request.SearchPhotoRequest;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.util.Utils;
import com.foc.vaadin.gui.components.FVButton;
import com.foc.vaadin.gui.components.upload.FVImageReceiver;
import com.foc.vaadin.gui.components.upload.FVUpload_Image;
import com.foc.vaadin.gui.layouts.FVVerticalLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.ui.Component;
import com.vaadin.ui.Upload.SucceededEvent;

@SuppressWarnings("serial")
public class Face_Search_Standard_Form extends FocXMLLayout {
	
	@Override
	protected void afterLayoutConstruction() {
		super.afterLayoutConstruction();
		
		FVVerticalLayout vLay = getUploadButtonLayout();
		FaceUploderButton uploadButton = new FaceUploderButton();
		vLay.addComponent(uploadButton);
	}
	
	public FVVerticalLayout getUploadButtonLayout() {
		return (FVVerticalLayout) getComponentByName("_PhotoUpload");
	}
	
	public FVVerticalLayout getSimilarButtonLayout() {
		return (FVVerticalLayout) getComponentByName("_SimilarPhotos");
	}

	public class FaceUploderButton extends FVUpload_Image implements FVImageReceiver {
	
		public FaceUploderButton() {
			setImageReceiver(this);
		}
		
		public void dispose() {
			super.dispose();
		}

		@Override
		public void imageReceived(SucceededEvent event, InputStream image) {
			if(event != null && !Utils.isStringEmpty(event.getFilename())) {
				SearchPhotoRequest req = new SearchPhotoRequest();
				try{
					ArrayList<SimilarFace> similars = req.sendPhotoAndGetSimilars(event.getFilename(), image);
					
					similars.sort(new Comparator<SimilarFace>() {
						@Override
						public int compare(SimilarFace arg0, SimilarFace arg1) {
							int ret = 0;
							if(arg0.getDistance() - arg1.getDistance() > 0) {
								ret =  1;
							} else if(arg0.getDistance() - arg1.getDistance() < 0) {
								ret = -1;
							}
							return ret;
						}
					});
					
					disposeSimilars();
					FVVerticalLayout vLayout = getSimilarButtonLayout();

					for(int i=0; i<similars.size(); i++) {
						SimilarFace similarFace = similars.get(i);
						if (similarFace != null) {
							String buttonCaption = String.valueOf(similarFace.getDistance());
							if(buttonCaption.length() > 6) buttonCaption = buttonCaption.substring(0, 6);
							buttonCaption += " - " + similarFace.getRef();
							vLayout.addComponent(new SimilarButton(buttonCaption, similarFace.getRef()));
						}
					}
				}catch (Exception e){
					Globals.logException(e);
				}
			}
		}
	}
	
	private void disposeSimilars() {
		FVVerticalLayout vLayout = getSimilarButtonLayout();
		if(vLayout != null) {
			for(int i=0; i<vLayout.getComponentCount(); i++) {
				Component cmp = vLayout.getComponent(i);
				if(cmp instanceof SimilarButton) {
					vLayout.removeComponent(cmp);
					((SimilarButton) cmp).dispose();
				}
			}
			vLayout.removeAllComponents();
		}
	}
	
	public class SimilarButton extends FVButton {

		private long ref = 0;
		
		public SimilarButton(String content, long ref) {
			super(content);
			this.ref = ref;
			setWidth("200px");
			addClickListener(new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					FocConstructor constr = new FocConstructor(PhotoAlbumDesc.getInstance());
					PhotoAlbum photoAlbum = (PhotoAlbum) constr.newItem();
					photoAlbum.setReference(ref);
					photoAlbum.load();

					XMLViewKey key = new XMLViewKey(PhotoAlbumDesc.DB_TABLE_NAME, XMLViewKey.TYPE_FORM, "Reduced", XMLViewKey.VIEW_DEFAULT);
					FocXMLLayout centralPanel = (FocXMLLayout) XMLViewDictionary.getInstance().newCentralPanel(getMainWindow(), key, photoAlbum);
					centralPanel.popupInDialog();
					
//					String tableName = photoAlbum.getTableName();
//					long ref = photoAlbum.getObjectRef();
					
					photoAlbum.dispose();
				}
			});
		}
		
	}
}