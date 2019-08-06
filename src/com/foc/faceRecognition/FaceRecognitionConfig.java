package com.foc.faceRecognition;

import com.foc.annotations.model.FocEntity;
import com.foc.annotations.model.fields.FocString;
import com.foc.desc.FocConstructor;
import com.foc.desc.parsers.ParsedFocDesc;
import com.foc.desc.parsers.pojo.PojoFocObject;
import com.foc.list.FocList;

@SuppressWarnings({ "serial" })
@FocEntity
public class FaceRecognitionConfig extends PojoFocObject{
	
	public static final String DBNAME = "FaceRecognitionConfig";
	
	@FocString(size = 200)
	public static final String FIELD_WebServiceURL = "WebServiceURL";
	
	public FaceRecognitionConfig(FocConstructor constr) {
		super(constr);
	}
	
	public static ParsedFocDesc getFocDesc() {
		return ParsedFocDesc.getInstance(DBNAME);
	}

	public String getWebServiceURL() {
		return getPropertyString(FIELD_WebServiceURL);
	}

	public void setWebServiceURL(String value) {
		setPropertyString(FIELD_WebServiceURL, value);
	}

	private static FaceRecognitionConfig instance = null;
	
	public static FaceRecognitionConfig getInstance() {
		if(instance == null) {
			FocList list = getFocDesc().getFocList(FocList.LOAD_IF_NEEDED);
			if(list.size() > 0) {
				instance = (FaceRecognitionConfig) list.getFocObject(0);
			} else {
				instance = (FaceRecognitionConfig) list.newEmptyItem();
				list.add(instance);
				instance.validate(false);
				list.validate(false);
			}
		}
		return instance;
	}
	
}
