package com.foc.faceRecognition;

import org.json.JSONObject;

import com.foc.annotations.model.FocEntity;
import com.foc.annotations.model.fields.FocForeignEntity;
import com.foc.annotations.model.fields.FocInteger;
import com.foc.annotations.model.fields.FocString;
import com.foc.business.photoAlbum.PhotoAlbum;
import com.foc.desc.FocConstructor;
import com.foc.desc.parsers.ParsedFocDesc;
import com.foc.desc.parsers.pojo.PojoFocObject;

@SuppressWarnings({ "serial" })
@FocEntity
public class Face extends PojoFocObject{
	
	public static final String DBNAME = "Face";
	
	@FocForeignEntity(table = "PHOTO_ALBUM", cachedList = false)
	public static final String FIELD_PhotoAlbum = "PhotoAlbum";

	@FocInteger(size = 10)
	public static final String FIELD_Top = "Top";

	@FocInteger(size = 10)
	public static final String FIELD_Bottom = "Bottom";

	@FocInteger(size = 10)
	public static final String FIELD_Left = "Left";

	@FocInteger(size = 10)
	public static final String FIELD_Right = "Right";

	@FocString(size = 3500)
	public static final String FIELD_Encoding = "Encoding";

	public Face(FocConstructor constr) {
		super(constr);
	}
	
	public static ParsedFocDesc getFocDesc() {
		return ParsedFocDesc.getInstance(DBNAME);
	}

	public int getTop() {
		return getPropertyInteger(FIELD_Top);
	}

	public void setTop(int value) {
		setPropertyInteger(FIELD_Top, value);
	}

	public int getBottom() {
		return getPropertyInteger(FIELD_Bottom);
	}

	public void setBottom(int value) {
		setPropertyInteger(FIELD_Bottom, value);
	}

	public int getLeft() {
		return getPropertyInteger(FIELD_Left);
	}

	public void setLeft(int value) {
		setPropertyInteger(FIELD_Left, value);
	}

	public int getRight() {
		return getPropertyInteger(FIELD_Right);
	}

	public void setRight(int value) {
		setPropertyInteger(FIELD_Right, value);
	}

	public String getEncoding() {
		return getPropertyString(FIELD_Encoding);
	}

	public void setEncoding(String value) {
		setPropertyString(FIELD_Encoding, value);
	}

	public PhotoAlbum getPhotoAlbum() {
		return (PhotoAlbum) getPropertyObject(FIELD_PhotoAlbum);
	}
	
	public long getPhotoAlbumRef() {
		return getPropertyObjectLocalReference(FIELD_PhotoAlbum);
	}

	public void setPhotoAlbum(PhotoAlbum value) {
		setPropertyObject(FIELD_PhotoAlbum, value);
	}
	
	public void setPhotoAlbumRef(long value) {
		setPropertyObjectLocalReference(FIELD_PhotoAlbum, value);
	}
	
	public void parseJson(JSONObject json) throws Exception {
		setTop(json.getInt("top"));
		setBottom(json.getInt("bottom"));
		setLeft(json.getInt("left"));
		setRight(json.getInt("right"));
		setEncoding(json.getString("encoding"));
	}
}
