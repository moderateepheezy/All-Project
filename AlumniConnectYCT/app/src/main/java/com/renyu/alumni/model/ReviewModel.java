package com.renyu.alumni.model;

import java.io.Serializable;

public class ReviewModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	ClassInfoModel cmodel=null;
	UserModel umodel=null;
	public ClassInfoModel getCmodel() {
		return cmodel;
	}
	public void setCmodel(ClassInfoModel cmodel) {
		this.cmodel = cmodel;
	}
	public UserModel getUmodel() {
		return umodel;
	}
	public void setUmodel(UserModel umodel) {
		this.umodel = umodel;
	}

}
