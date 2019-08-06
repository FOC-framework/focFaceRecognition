package com.foc.faceRecognition;

import com.foc.annotations.model.FocEntity;
import com.foc.annotations.model.fields.FocLong;
import com.foc.desc.FocConstructor;
import com.foc.desc.parsers.ParsedFocDesc;
import com.foc.desc.parsers.pojo.PojoFocObject;

@SuppressWarnings({ "serial" })
@FocEntity
public class TreatedImage extends PojoFocObject {
	
	public static final String DBNAME = "TreatedImage";
	
	@FocLong()
	public static final String FIELD_TreatedRef = "TreatedRef";

	public TreatedImage(FocConstructor constr) {
		super(constr);
	}
	
	public static ParsedFocDesc getFocDesc() {
		return ParsedFocDesc.getInstance(DBNAME);
	}

	public long getTreatedRef() {
		return getPropertyLong(FIELD_TreatedRef);
	}

	public void setTreatedRef(long value) {
		setPropertyLong(FIELD_TreatedRef, value);
	}
	
}
