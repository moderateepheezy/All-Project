package com.renyu.alumni.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ImageUpdateModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//�����ϴ�����
	ArrayList<ImageChoiceModel> imageList=null;
	//����ϴ�����
	int finishCount=0;
	//ͼƬ�ϴ�����
	ArrayList<ImageChoiceModel> uploadImageLists=null;
	
	private int activity_id;//���ʶ
	private String activity_name;//�����
	private long begin_time;//���ʼʱ�� yyyy-MM-dd HH:mm:ss
	private long end_time;//�����ʱ�� yyyy-MM-dd HH:mm:ss
	private String address;//���ַ
	private long apply_time;//������ֹʱ�� yyyy-MM-dd HH:mm:ss
	private String create_org;//���֯��
	private String activity_pic;//���ҳ����ͼurl
	private String activity_desc;//�����
	private int activity_state=0;
	private String his_desc;//��������
	
	public String getHis_desc() {
		return his_desc;
	}
	public void setHis_desc(String his_desc) {
		this.his_desc = his_desc;
	}
	public int getActivity_state() {
		return activity_state;
	}
	public void setActivity_state(int activity_state) {
		this.activity_state = activity_state;
	}
	public int getActivity_id() {
		return activity_id;
	}
	public void setActivity_id(int activity_id) {
		this.activity_id = activity_id;
	}
	public String getActivity_name() {
		return activity_name;
	}
	public void setActivity_name(String activity_name) {
		this.activity_name = activity_name;
	}
	public long getBegin_time() {
		return begin_time;
	}
	public void setBegin_time(long begin_time) {
		this.begin_time = begin_time;
	}
	public long getEnd_time() {
		return end_time;
	}
	public void setEnd_time(long end_time) {
		this.end_time = end_time;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public long getApply_time() {
		return apply_time;
	}
	public void setApply_time(long apply_time) {
		this.apply_time = apply_time;
	}
	public String getCreate_org() {
		return create_org;
	}
	public void setCreate_org(String create_org) {
		this.create_org = create_org;
	}
	public String getActivity_pic() {
		return activity_pic;
	}
	public void setActivity_pic(String activity_pic) {
		this.activity_pic = activity_pic;
	}
	public String getActivity_desc() {
		return activity_desc;
	}
	public void setActivity_desc(String activity_desc) {
		this.activity_desc = activity_desc;
	}
	public ArrayList<ImageChoiceModel> getImageList() {
		return imageList;
	}
	public void setImageList(ArrayList<ImageChoiceModel> imageList) {
		this.imageList = imageList;
	}
	public int getFinishCount() {
		return finishCount;
	}
	public void setFinishCount(int finishCount) {
		this.finishCount = finishCount;
	}
	public ArrayList<ImageChoiceModel> getUploadImageLists() {
		return uploadImageLists;
	}
	public void setUploadImageLists(ArrayList<ImageChoiceModel> uploadImageLists) {
		this.uploadImageLists = uploadImageLists;
	}
}
