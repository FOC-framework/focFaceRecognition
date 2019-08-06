package com.foc.faceRecognition;

import com.foc.Globals;
import com.foc.faceRecognition.services.server.FaceStartServlet;
import com.foc.faceRecognition.services.server.request.SendFacesInitRequest;
import com.foc.menuStructure.FocMenuItem;
import com.foc.menuStructure.IFocMenuItemAction;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.FocWebModule;
import com.foc.vaadin.gui.menuTree.FVMenuTree;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.gui.INavigationWindow;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;

public class FaceRecognitionModule extends FocWebModule {

	public static final int VERSION_ID = 1000;

	public static final String MODULE_NAME = "Face Recognition";
	public static final String MNU_FACE = "MNU_FACE";

	public FaceRecognitionModule() {
		super(MODULE_NAME, "Face Recognition", "com.foc.faceRecognition", "com.foc.faceRecognition.gui", "Face recognition 1.0", VERSION_ID);
//		addPackages("", null);
	}

	@Override
	public void declareFocObjectsOnce() {
		super.declareFocObjectsOnce();
	}

	@Override
	public void menu_FillMenuTree(FVMenuTree menuTree, FocMenuItem fatherMenuItem) {
		FocMenuItem mainMenu = menuTree.pushRootMenu(MNU_FACE, "Face Recognition");
		FocMenuItem menuItem = null;

		menuItem = mainMenu.pushMenu("MNU_FACE_CONFIG", "Face recognition configuration");
		menuItem.setMenuAction(new IFocMenuItemAction() {
			@Override
			public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
				INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
				
				FaceRecognitionConfig faceRecognitionConfig = FaceRecognitionConfig.getInstance();
				
				if(faceRecognitionConfig != null) {
					XMLViewKey key = new XMLViewKey(FaceRecognitionConfig.getFocDesc().getStorageName(), XMLViewKey.TYPE_FORM);
					FocXMLLayout layout = (FocXMLLayout) XMLViewDictionary.getInstance().newCentralPanel(mainWindow, key, faceRecognitionConfig);
					mainWindow.changeCentralPanelContent(layout, FocCentralPanel.PREVIOUS_KEEP);
				}
			}
		});
		
		menuItem = mainMenu.pushMenu("MNU_FACE_SEARCH", "Search new Faces");
		menuItem.setMenuAction(new IFocMenuItemAction() {
			@Override
			public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
				INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
				
				XMLViewKey key = new XMLViewKey(Face.getFocDesc().getStorageName(), XMLViewKey.TYPE_FORM, "Search", XMLViewKey.VIEW_DEFAULT);
				FocXMLLayout layout = (FocXMLLayout) XMLViewDictionary.getInstance().newCentralPanel(mainWindow, key, null);
				layout.setFocDataOwner(true);
				mainWindow.changeCentralPanelContent(layout, FocCentralPanel.PREVIOUS_KEEP);
			}
		});

		menuItem = mainMenu.pushMenu("MNU_FACES_RELOAD", "Load new photos");
		menuItem.setMenuAction(new IFocMenuItemAction() {
			@Override
			public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
				INavigationWindow mainWindow = (INavigationWindow) navigationWindow;
				FaceStartServlet.pushFacesFromPhotoAlbum();
			}
		});

		menuItem = mainMenu.pushMenu("MNU_FACES_PUSH_TO_SERVER", "Init service with scanned faces");
		menuItem.setMenuAction(new IFocMenuItemAction() {
			@Override
			public void actionPerformed(Object navigationWindow, FocMenuItem menuItem, int extraActionIndex) {
				try{
					SendFacesInitRequest request = new SendFacesInitRequest();
					request.sendFaces();
				}catch (Exception e){
					Globals.logException(e);
				}
			}
		});

		/*
		if (getFocGroup().getWebModuleRights(MODULE_NAME) == GrpWebModuleRightsDesc.ACCESS_FULL_WITH_CONFIGURTION) {
			FocMenuItem lookupsMenu = mainMenu.pushMenu(MNU_STATELESS_LOOKUPS, "النماذج الفرعية لغرفة العمليات");

			menuItem = lookupsMenu.pushMenu("MNU_STLS_DOC_TYPE", "انواع المستندات المرفقة مع استدعاء مكتوم القيد");
			menuItem.setMenuAction(new FocMenuItemAbstractAction_WithAddCommand(menuItem, null) {
				@Override
				public FocList getFocList() {
					FocDesc focDesc = StlsDocType.getFocDesc();
					return focDesc.getFocList(FocList.LOAD_IF_NEEDED);
				}
			});
		}
		 */
	}

}