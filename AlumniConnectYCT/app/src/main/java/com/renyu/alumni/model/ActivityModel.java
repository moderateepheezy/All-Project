package com.renyu.alumni.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ActivityModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int activity_id;//���ʶ
	private String activity_name;//�����
	private long begin_time;//���ʼʱ�� yyyy-MM-dd HH:mm:ss
	private long end_time;//�����ʱ�� yyyy-MM-dd HH:mm:ss
	private String address;//���ַ
	private long apply_time;//������ֹʱ�� yyyy-MM-dd HH:mm:ss
	private String create_org;//���֯��
	private int view_times;//�������
	private int apply_number;//�μ�����
	private String offdate;//���뱨����ʣʱ��  ����1�����ʾ����  ��һ�����ڵ���ʾСʱ   ��1Сʱ���ڵ���ʾ����(3��    3Сʱ     3����)
	private String activity_pic;//���ҳ����ͼurl
	private int activity_state;//0:�����(�������ֹ���ڻ�û��) 1���ɱ���(���ͨ���������ֹ����û��) 2�������ѽ���(���ͨ��������ֹʱ�䵽�˻��ʼ����û��)  3����˲�ͨ��   4�� �������(���ͨ�� �ڻ��ʼʱ�䵽�����ʱ��) 5����ѽ�ֹ(���ͨ����������ڵ���) 6����ѽ�ֹ(����˻������ֹ���ڵ���)  7 ��Ѿ��鵵(�Ѿ��ϴ�����)
	private int user_id;//�������
	private int super_admin=0;
	private String activity_desc="";
	private int is_apply=0;
	private ArrayList<ImageChoiceModel> picinfo=null;
	private String his_desc;//��������
	private long generate_time;//����ʱ��
	private int activity_type;//�����
	private String activity_url;//�����
	private String begin_time2;//type=2�Ļ��ʼʱ��
	
	public long getGenerate_time() {
		return generate_time;
	}
	public void setGenerate_time(long generate_time) {
		this.generate_time = generate_time;
	}
	public int getActivity_type() {
		return activity_type;
	}
	public void setActivity_type(int activity_type) {
		this.activity_type = activity_type;
	}
	public String getActivity_url() {
		return activity_url;
	}
	public void setActivity_url(String activity_url) {
		this.activity_url = activity_url;
	}
	public String getBegin_time2() {
		return begin_time2;
	}
	public void setBegin_time2(String begin_time2) {
		this.begin_time2 = begin_time2;
	}
	public String getHis_desc() {
		return his_desc;
	}
	public void setHis_desc(String his_desc) {
		this.his_desc = his_desc;
	}
	public int getSuper_admin() {
		return super_admin;
	}
	public void setSuper_admin(int super_admin) {
		this.super_admin = super_admin;
	}
	public String getActivity_desc() {
		return activity_desc;
	}
	public void setActivity_desc(String activity_desc) {
		this.activity_desc = activity_desc;
	}
	public int getIs_apply() {
		return is_apply;
	}
	public void setIs_apply(int is_apply) {
		this.is_apply = is_apply;
	}
	public ArrayList<ImageChoiceModel> getPicinfo() {
		return picinfo;
	}
	public void setPicinfo(ArrayList<ImageChoiceModel> picinfo) {
		this.picinfo = picinfo;
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
	public int getView_times() {
		return view_times;
	}
	public void setView_times(int view_times) {
		this.view_times = view_times;
	}
	public int getApply_number() {
		return apply_number;
	}
	public void setApply_number(int apply_number) {
		this.apply_number = apply_number;
	}
	public String getOffdate() {
		return offdate;
	}
	public void setOffdate(String offdate) {
		this.offdate = offdate;
	}
	public String getActivity_pic() {
		return activity_pic;
	}
	public void setActivity_pic(String activity_pic) {
		this.activity_pic = activity_pic;
	}
	public int getActivity_state() {
		return activity_state;
	}
	public void setActivity_state(int activity_state) {
		this.activity_state = activity_state;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	
}
