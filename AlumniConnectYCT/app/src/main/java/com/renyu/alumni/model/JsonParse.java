package com.renyu.alumni.model;

import android.content.Context;

import com.renyu.alumni.common.CommonUtils;
import com.renyu.alumni.common.ParamsManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class JsonParse {
	
	public static void showMessage(Context context, String str) {
		try {
			JSONObject obj=new JSONObject(str);
			if(obj.getInt("result")==1) {
				CommonUtils.showCustomToast(context, obj.getString("message"), true);
			}
			else {
				CommonUtils.showCustomToast(context, obj.getString("message"), false);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getErrorMessage(Context context, String str) {
		try {
			JSONObject obj=new JSONObject(str);
			return obj.getString("message");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public static int getLoginResult(Context context, String str) {
		try {
			JSONObject obj=new JSONObject(str);
			return obj.getInt("result");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * ��ȡ�û�����
	 * @param str
	 * @return
	 */
	public static UserModel getUserModel(String str) {
		try {
			JSONObject obj=new JSONObject(str);
			UserModel model=new UserModel();
			model.setUser_id(Integer.parseInt(obj.getString("user_id")));
			model.setToken(obj.getString("token"));
			model.setUser_name(obj.getString("user_name"));
			model.setAvatar_large(CommonUtils.convertNull(obj.getString("avatar_large")));
			return model;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * ������ݻ�ȡѧԺ��Ϣ
	 * @param str
	 * @return
	 */
	public static HashMap<String, String> getCollegeInfoByYear(String str) {
		HashMap<String, String> map=new HashMap<String, String>();
		try {
			JSONArray array=new JSONArray(str);
			for(int i=0;i<array.length();i++) {
				JSONObject obj=array.getJSONObject(i);
				map.put(obj.getString("college_name"), obj.getString("college_id"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * ����У����֯ȫ����Ϣ
	 * @param str
	 * @param type �Ƿ���ʾĳһ��������
	 * @return
	 */
	public static ArrayList<Object> getAllOrganizationModel(String str, String type) {
		ArrayList<Object> models=new ArrayList<Object>();
		try {
			JSONObject obj=new JSONObject(str);
			JSONArray classinfo=obj.getJSONArray("classinfo");
			if(type.equals("all")||type.equals("myclass")) {
				JSONArray createclassinfo=obj.getJSONArray("createclassinfo");
				models.add("�ҵİ༶");
				if(classinfo.length()==0&&createclassinfo.length()==0) {
					NoOrganizationModel model=new NoOrganizationModel();
					model.setNoOrganizationMessage("����δ�����κΰ༶\n������ϽǼӺ��Լ���");
					models.add(model);
				}
				for(int i=0;i<classinfo.length();i++) {
					ClassInfoModel model=new ClassInfoModel();
					JSONObject obj_classinfo=classinfo.getJSONObject(i);
					if(obj_classinfo.getString("admin_name").indexOf(",")!=-1) {
						model.setAdmin_name(obj_classinfo.getString("admin_name").split(",")[0]);
					}
					else {
						model.setAdmin_name(obj_classinfo.getString("admin_name"));
					}
					model.setClass_admin(obj_classinfo.getInt("class_admin"));
					model.setClass_id(obj_classinfo.getInt("class_id"));
					model.setClass_name(obj_classinfo.getString("class_name"));
					model.setClass_pic(obj_classinfo.getString("class_pic")==null?"":obj_classinfo.getString("class_pic"));
					model.setClass_role_state(obj_classinfo.getInt("class_role_state"));
					model.setStudent_num(obj_classinfo.getInt("student_num"));
					model.setStudent_apply(obj_classinfo.getInt("student_apply"));
					if(!CommonUtils.convertNull(obj_classinfo.getString("refuseStr")).equals("")) {
						String str_array="";
						JSONArray arrays=obj_classinfo.getJSONArray("refuseStr");
						for(int j=0;j<arrays.length();j++) {
							str_array+=arrays.getString(j)+"&";
						}
						model.setRefuseStr(str_array);
					}
					models.add(model);
				}
				if(createclassinfo.length()>0&&obj.getInt("super_admin")==1) {
					models.add("�༶�������");
				}
				for(int i=0;i<createclassinfo.length();i++) {
					CreateclassinfoModel model=new CreateclassinfoModel();
					JSONObject obj_classinfo=createclassinfo.getJSONObject(i);
					model.setClass_id(obj_classinfo.getInt("class_id"));
					model.setClass_name(obj_classinfo.getString("class_name"));
					model.setClass_pic(obj_classinfo.getString("class_pic")==null?"":obj_classinfo.getString("class_pic"));
					model.setClass_state(obj_classinfo.getInt("class_state"));
					model.setCreate_username(obj_classinfo.getString("create_username"));
					model.setSuper_admin(obj.getInt("super_admin"));
					model.setUser_id(obj_classinfo.getString("user_id"));
					if(!CommonUtils.convertNull(obj_classinfo.getString("refuseStr")).equals("")) {
						String str_array="";
						JSONArray arrays=obj_classinfo.getJSONArray("refuseStr");
						for(int j=0;j<arrays.length();j++) {
							str_array+=arrays.getString(j)+"&";
						}
						model.setRefuseStr(str_array);
					}
					models.add(model);
				}
			}
			if(type.equals("all")||type.equals("myalumni")) {
				JSONArray aluassociationinfo=obj.getJSONArray("aluassociationinfo");
				models.add("�ҵ�У�ѻ�");
				if(aluassociationinfo.length()==0) {
					NoOrganizationModel model=new NoOrganizationModel();
					model.setNoOrganizationMessage("����δ�����κ�У�ѻ�\n����У�ѻ��������ʶ����У�ѡ�����У�ѻ���֯���»��������ϽǼӺ��Լ���");
					models.add(model);
				}
				for(int i=0;i<aluassociationinfo.length();i++) {
					AluassociationinfoModel model=new AluassociationinfoModel();
					JSONObject obj_classinfo=aluassociationinfo.getJSONObject(i);
					if(obj_classinfo.getString("admin_name").indexOf(",")!=-1) {
						model.setAdmin_name(obj_classinfo.getString("admin_name").split(",")[0]);
					}
					else {
						model.setAdmin_name(obj_classinfo.getString("admin_name"));
					}
					model.setAluassociation_admin(obj_classinfo.getInt("aluassociation_admin"));
					model.setAluassociation_id(obj_classinfo.getInt("aluassociation_id"));
					model.setAluassociation_name(obj_classinfo.getString("aluassociation_name"));
					model.setAluassociation_num(obj_classinfo.getInt("aluassociation_num"));
					model.setAluassociation_pic(obj_classinfo.getString("aluassociation_pic")==null?"":obj_classinfo.getString("aluassociation_pic"));
					model.setAluassociation_role_state(obj_classinfo.getInt("aluassociation_role_state"));
					model.setAluassociation_apply(obj_classinfo.getInt("aluassociation_apply"));
					model.setAluassociation_desc(CommonUtils.convertNull(obj_classinfo.getString("aluassociation_desc")).equals("")?"":obj_classinfo.getString("aluassociation_desc"));
					if(!CommonUtils.convertNull(obj_classinfo.getString("refuseStr")).equals("")) {
						String str_array="";
						JSONArray arrays=obj_classinfo.getJSONArray("refuseStr");
						for(int j=0;j<arrays.length();j++) {
							str_array+=arrays.getString(j)+"&";
						}
						model.setRefuseStr(str_array);
					}
					models.add(model);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return models;
	}
	
	/**
	 * ����ȫ���༶��Ա��Ϣ
	 * @param str
	 * @return
	 */
	public static ArrayList<ClassUserModel> getAllClassUserModel(String str, ClassInfoModel ciModel) {
		ArrayList<ClassUserModel> models=new ArrayList<ClassUserModel>();
		try {
			JSONObject ciobj=new JSONObject(str);
			ciModel.setClass_id(ciobj.getInt("class_id"));
			ciModel.setStudent_apply(ciobj.getInt("student_apply"));
			ciModel.setAdmin_name(ciobj.getString("admin_name"));
			ciModel.setClass_pic(ciobj.getString("class_pic"));
			ciModel.setStudent_num(ciobj.getInt("student_num"));
			ciModel.setClass_name(ciobj.getString("class_name"));
			ciModel.setClass_admin(ciobj.getInt("class_admin"));
			ciModel.setClass_role_state(ciobj.getInt("class_role_state"));
			
			JSONArray array=ciobj.getJSONArray("personinfos");
			for(int i=0;i<array.length();i++) {
				ClassUserModel model=new ClassUserModel();
				JSONObject obj=array.getJSONObject(i);
				model.setClass_admin(obj.getInt("class_admin"));
				model.setClass_role_state(obj.getInt("class_role_state"));
				model.setUser_id(obj.getInt("user_id"));
				model.setUser_name(obj.getString("user_name"));
				model.setAvatar_large(CommonUtils.convertNull(obj.getString("avatar_large")));
				model.setIs_authentication(obj.getInt("is_authentication"));
				
				model.setCity_name(CommonUtils.convertNull(obj.getString("city_name")));
				model.setCity_secret(obj.getInt("city_secret"));
				model.setIndustry(CommonUtils.convertNull(obj.getString("industry")));
				model.setIndustry_secret(obj.getInt("industry_secret"));
				model.setCompanyname(CommonUtils.convertNull(obj.getString("companyname")));
				model.setCompany_secret(obj.getInt("company_secret"));
				models.add(model);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return models;
	}
	
	/**
	 * ����ȫ��У�ѻ��Ա��Ϣ
	 * @param str
	 * @return
	 */
	public static ArrayList<ClassUserModel> getAllAlumniUserModel(String str, AluassociationinfoModel amodel) {
		ArrayList<ClassUserModel> models=new ArrayList<ClassUserModel>();
		try {
			JSONObject aObj=new JSONObject(str);
			amodel.setAluassociation_apply(aObj.getInt("aluassociation_apply"));
			amodel.setAluassociation_num(aObj.getInt("aluassociation_num"));
			amodel.setAdmin_name(aObj.getString("admin_name"));
			amodel.setAluassociation_desc(CommonUtils.convertNull(aObj.getString("aluassociation_desc")));
			amodel.setAluassociation_pic(aObj.getString("aluassociation_pic"));
			amodel.setAluassociation_id(aObj.getInt("aluassociation_id"));
			amodel.setAluassociation_admin(aObj.getInt("aluassociation_admin"));
			amodel.setAluassociation_role_state(aObj.getInt("aluassociation_role_state"));
			
			JSONArray array=aObj.getJSONArray("personinfos");
			for(int i=0;i<array.length();i++) {
				ClassUserModel model=new ClassUserModel();
				JSONObject obj=array.getJSONObject(i);
				model.setClass_admin(obj.getInt("aluassociation_admin"));
				model.setClass_role_state(obj.getInt("aluassociation_role_state"));
				model.setUser_id(obj.getInt("user_id"));
				model.setUser_name(obj.getString("user_name"));
				model.setAvatar_large(CommonUtils.convertNull(obj.getString("avatar_large")));
				model.setIs_authentication(obj.getInt("is_authentication"));
				
				model.setCity_name(CommonUtils.convertNull(obj.getString("city_name")));
				model.setCity_secret(obj.getInt("city_secret"));
				model.setIndustry(CommonUtils.convertNull(obj.getString("industry")));
				model.setIndustry_secret(obj.getInt("industry_secret"));
				model.setCompanyname(CommonUtils.convertNull(obj.getString("companyname")));
				model.setCompany_secret(obj.getInt("company_secret"));
				model.setYear(CommonUtils.convertNull(obj.getString("year")));
				model.setClass_name(CommonUtils.convertNull(obj.getString("class_name")));
				
				models.add(model);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return models;
	}
	
	/**
	 * ������Ƭ��Ϣ
	 * @param str
	 * @return
	 */
	public static BussinessCardModel getBussinessCardModel(String str) {
		BussinessCardModel model=new BussinessCardModel();
		try {
			JSONObject obj=new JSONObject(str);
			model.setCity_name(CommonUtils.convertNull(obj.getString("city_name")));
			model.setCity_secret(obj.getInt("city_secret"));
			model.setCompany_department(CommonUtils.convertNull(obj.getString("company_department")));
			model.setCompany_position(CommonUtils.convertNull(obj.getString("company_position")));
			model.setCompanyname(CommonUtils.convertNull(obj.getString("companyname")));
			model.setMail(CommonUtils.convertNull(obj.getString("mail")));
			model.setMail_secret(obj.getInt("mail_secret"));
			model.setPhonenum(CommonUtils.convertNull(obj.getString("phonenum")));
			model.setPhonenum_secret(obj.getInt("phonenum_secret"));
			model.setQq(CommonUtils.convertNull(obj.getString("qq")));
			model.setQq_secret(obj.getInt("qq_secret"));
			model.setService_name(CommonUtils.convertNull(obj.getString("service_name")));
			model.setService_secret(obj.getInt("service_secret"));
			model.setUser_id(obj.getInt("user_id"));
			model.setUser_name(obj.getString("user_name"));
			model.setCompany_secret(obj.getInt("company_secret"));
			model.setDepartment_secret(obj.getInt("department_secret"));
			model.setPosition_secret(obj.getInt("position_secret"));
			model.setStore_state(obj.getInt("store_state"));
			model.setAvatar_large(obj.getString("avatar_large"));
			model.setIndustry(obj.getString("industry"));
			model.setIs_authentication(obj.getInt("is_authentication"));
			ArrayList<String> classinfos=new ArrayList<String>();
			JSONArray classinfoArray=obj.getJSONArray("classinfo");
			for(int i=0;i<classinfoArray.length();i++) {
				classinfos.add(classinfoArray.getString(i));
			}
			model.setClassinfos(classinfos);
			ArrayList<AluassociationinfoModel> aluassociationinfos=new ArrayList<AluassociationinfoModel>();
			JSONArray aluassociationinfoArray=obj.getJSONArray("aluassociationinfos");
			for(int i=0;i<aluassociationinfoArray.length();i++) {
				JSONObject obj_temp=aluassociationinfoArray.getJSONObject(i);
				AluassociationinfoModel model_temp=new AluassociationinfoModel();
				model_temp.setAluassociation_name(obj_temp.getString("aluassociation_name"));
				model_temp.setAdmin_name(CommonUtils.convertNull(obj_temp.getString("admin_name")).equals("")?"":obj_temp.getString("admin_name"));
				model_temp.setAluassociation_admin(CommonUtils.convertNull(obj_temp.getString("aluassociation_admin")).equals("")?0:obj_temp.getInt("aluassociation_admin"));
				model_temp.setAluassociation_apply(obj_temp.getInt("aluassociation_apply"));
				model_temp.setAluassociation_desc(CommonUtils.convertNull(obj_temp.getString("admin_name"))==null?"":obj_temp.getString("admin_name"));
				model_temp.setAluassociation_id(obj_temp.getInt("aluassociation_id"));
				model_temp.setAluassociation_num(obj_temp.getInt("aluassociation_num"));
				model_temp.setAluassociation_pic(CommonUtils.convertNull(obj_temp.getString("aluassociation_pic")).equals("")?"":obj_temp.getString("aluassociation_pic"));
				model_temp.setAluassociation_role_state(CommonUtils.convertNull(obj_temp.getString("aluassociation_role_state")).equals("")?3:obj_temp.getInt("aluassociation_role_state"));
				model_temp.setAluassociation_desc(CommonUtils.convertNull(obj_temp.getString("aluassociation_desc")).equals("")?"":obj_temp.getString("aluassociation_desc"));
				if(!CommonUtils.convertNull(obj_temp.getString("refuseStr")).equals("")) {
					String str_array="";
					JSONArray arrays=obj_temp.getJSONArray("refuseStr");
					for(int j=0;j<arrays.length();j++) {
						str_array+=arrays.getString(j)+"&";
					}
					model_temp.setRefuseStr(str_array);
				}
				aluassociationinfos.add(model_temp);
			}
			model.setAluassociationinfos(aluassociationinfos);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return model;
	}
	
	/**
	 * ��ȡ��������ѧУ��Ϣ
	 * @param str
	 * @return
	 */
	public static ArrayList<ClassInfoModel> getSearchClassInfoModel(String str) {
		ArrayList<ClassInfoModel> models=new ArrayList<ClassInfoModel>();
		try {
			JSONArray array=new JSONArray(str);
			for(int i=0;i<array.length();i++) {
				JSONObject obj=array.getJSONObject(i);
				ClassInfoModel model=new ClassInfoModel();
				model.setClass_id(Integer.parseInt(obj.getString("class_id")));
				model.setClass_name(obj.getString("class_name"));
				model.setClass_pic(obj.getString("class_pic")==null?"":obj.getString("class_pic"));
				model.setStudent_num(Integer.parseInt(obj.getString("student_num")));
				model.setAdmin_name(obj.getString("admin_name"));
				try {
					model.setClass_role_state(Integer.parseInt(obj.getString("class_role_state")));
				} catch(Exception e) {
					model.setClass_role_state(3);
				}
				if(!CommonUtils.convertNull(obj.getString("refuseStr")).equals("")) {
					String str_array="";
					JSONArray arrays=obj.getJSONArray("refuseStr");
					for(int j=0;j<arrays.length();j++) {
						str_array+=arrays.getString(j)+"&";
					}
					model.setRefuseStr(str_array);
				}
				model.setStudent_apply(Integer.parseInt(obj.getString("student_apply")));
				try {
					model.setClass_admin(Integer.parseInt(obj.getString("class_admin")));
				} catch(Exception e) {
					model.setClass_admin(0);
				}
				try {
					model.setClass_role_state(Integer.parseInt(obj.getString("class_role_state")));
				} catch(Exception e) {
					model.setClass_role_state(3);
				}
				
				models.add(model);
			}
 		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return models;
	}
	
	/**
	 * ��ȡ��������У�ѻ���Ϣ
	 * @param str
	 * @return
	 */
	public static ArrayList<AluassociationinfoModel> getSearchAluassociationinfoModel(String str) {
		ArrayList<AluassociationinfoModel> models=new ArrayList<AluassociationinfoModel>();
		try {
			JSONArray array=new JSONArray(str);
			for(int i=0;i<array.length();i++) {
				JSONObject aluassociationinfo=array.getJSONObject(i);
				AluassociationinfoModel model=new AluassociationinfoModel();
				try {
					if(aluassociationinfo.getString("admin_name").indexOf(",")!=-1) {
						model.setAdmin_name(aluassociationinfo.getString("admin_name").split(",")[0]);
					}
					else {
						model.setAdmin_name(aluassociationinfo.getString("admin_name"));
					}
				} catch(Exception e) {
					model.setAdmin_name("");
				}
				try {
					model.setAluassociation_pic(aluassociationinfo.getString("aluassociation_pic"));	
				} catch(Exception e) {
					model.setAluassociation_pic("");
				}
				model.setAluassociation_id(aluassociationinfo.getInt("aluassociation_id"));
				model.setAluassociation_name(aluassociationinfo.getString("aluassociation_name"));
				model.setAluassociation_num(aluassociationinfo.getInt("aluassociation_num"));
				model.setAluassociation_desc(CommonUtils.convertNull(aluassociationinfo.getString("aluassociation_desc")).equals("")?"":aluassociationinfo.getString("aluassociation_desc"));
				if(!CommonUtils.convertNull(aluassociationinfo.getString("refuseStr")).equals("")) {
					String str_array="";
					JSONArray arrays=aluassociationinfo.getJSONArray("refuseStr");
					for(int j=0;j<arrays.length();j++) {
						str_array+=arrays.getString(j)+"&";
					}
					model.setRefuseStr(str_array);
				}
				try {
					model.setAluassociation_role_state(aluassociationinfo.getInt("aluassociation_role_state"));
				} catch(Exception e) {
					model.setAluassociation_role_state(3);
				} 
				model.setAluassociation_num(aluassociationinfo.getInt("aluassociation_num"));
				model.setAluassociation_apply(aluassociationinfo.getInt("aluassociation_apply"));
				try {
					model.setAluassociation_role_state(Integer.parseInt(aluassociationinfo.getString("aluassociation_role_state")));
				} catch(Exception e) {
					model.setAluassociation_role_state(3);
				}
				model.setAluassociation_admin(CommonUtils.convertNull(aluassociationinfo.getString("aluassociation_admin")).equals("")?0:Integer.parseInt(aluassociationinfo.getString("aluassociation_admin")));
				models.add(model);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return models;
	}
	
	/**
	 * ��ȡ����
	 * @param str
	 * @return
	 */
	public static NewsGetModel getNewsGetModel(String str) {
		NewsGetModel models=new NewsGetModel();
		try {
			JSONObject obj=new JSONObject(str);
			JSONObject content=obj.getJSONObject("content");
			//�ڶ�ҳ��ʼû��ͷ��
			if(content.has("head_news")) {
				JSONArray head_news=content.getJSONArray("head_news");
				ArrayList<NewsModel> head_news_list=new ArrayList<NewsModel>();
				for(int i=0;i<head_news.length();i++) {
					NewsModel model=new NewsModel();
					JSONObject obj_=head_news.getJSONObject(i);
					model.setNewstime(obj_.getString("newstime"));
					model.setPic(obj_.getString("pic"));
					model.setTitle(obj_.getString("title"));
					model.setUrl(obj_.getString("url"));
					head_news_list.add(model);
				}
				models.setHead_news(head_news_list);
			}
			JSONArray news=content.getJSONArray("news");
			ArrayList<NewsModel> news_list=new ArrayList<NewsModel>();
			for(int i=0;i<news.length();i++) {
				NewsModel model=new NewsModel();
				JSONObject obj_=news.getJSONObject(i);
				model.setNewstime(obj_.getString("newstime"));
				model.setPic(obj_.getString("pic"));
				model.setTitle(obj_.getString("title"));
				model.setUrl(obj_.getString("url"));
				news_list.add(model);
			}
			models.setNews(news_list);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return models;
	}
	
	/**
	 * ��ȡ�»�б�
	 * @param str
	 * @return
	 */
	public static ArrayList<ActivityModel> getNewActivityModel(String str) {
		ArrayList<ActivityModel> models=new ArrayList<ActivityModel>();
		try {
			JSONArray array=new JSONArray(str);
			for(int i=0;i<array.length();i++) {
				JSONObject obj=array.getJSONObject(i);
				ActivityModel model=new ActivityModel();
				model.setView_times(obj.getInt("view_times"));
				model.setActivity_name(obj.getString("activity_name"));
				try {
					model.setCreate_org(obj.getString("create_org"));
				} catch(Exception e) {
					model.setCreate_org("");
				}
				model.setApply_time(obj.getLong("apply_time"));
				model.setActivity_state(obj.getInt("activity_state"));
				model.setUser_id(obj.getInt("user_id"));
				model.setActivity_id(obj.getInt("activity_id"));
				try {
					model.setActivity_pic(obj.getString("activity_pic"));
				} catch(Exception e) {
					model.setActivity_pic("");
				}
				model.setOffdate(obj.getString("offdate"));
				model.setAddress(obj.getString("address"));
				model.setEnd_time(obj.getLong("end_time"));
				model.setApply_number(obj.getInt("apply_number"));
				model.setBegin_time(obj.getLong("begin_time"));
				model.setActivity_type(obj.getInt("activity_type"));
				model.setActivity_url(obj.getString("activity_url"));
				model.setBegin_time2(obj.getString("begin_time2"));
				models.add(model);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return models;
	}
	
	/**
	 * ��ȡ�����
	 * @param str
	 * @return
	 */
	public static ActivityModel getActivityModel(String str) {
		ActivityModel model=new ActivityModel();
		try {
			JSONObject obj=new JSONObject(str);
			try {
				model.setActivity_desc(obj.getString("activity_desc"));
			} catch(Exception e) {
				model.setActivity_desc("");
			}
			try {
				model.setHis_desc(obj.getString("his_desc"));
			} catch(Exception e) {
				model.setHis_desc("");
			}
			model.setActivity_id(obj.getInt("activity_id"));
			model.setActivity_name(obj.getString("activity_name"));
			try {
				model.setActivity_pic(obj.getString("activity_pic"));
			} catch(Exception e) {
				model.setActivity_pic("");
			}
			model.setActivity_state(obj.getInt("activity_state"));
			model.setAddress(obj.getString("address"));
			model.setApply_number(obj.getInt("apply_number"));
			model.setApply_time(obj.getLong("apply_time"));
			model.setBegin_time(obj.getLong("begin_time"));
			model.setCreate_org(obj.getString("create_org"));
			model.setEnd_time(obj.getLong("end_time"));
			model.setIs_apply(obj.getInt("is_apply"));
			model.setOffdate(obj.getString("offdate"));
			model.setSuper_admin(obj.getInt("super_admin"));
			model.setUser_id(obj.getInt("user_id"));
			model.setView_times(obj.getInt("view_times"));
			JSONArray picinfo=obj.getJSONArray("picinfo");
			ArrayList<ImageChoiceModel> picInfoList=new ArrayList<ImageChoiceModel>();
			for(int i=0;i<picinfo.length();i++) {
				JSONObject picinfoObj=picinfo.getJSONObject(i);
				ImageChoiceModel imodel=new ImageChoiceModel();
				imodel.setContent(picinfoObj.getString("pic_desc"));
				imodel.setFlag(picinfoObj.getInt("pic_state"));
				imodel.setId(picinfoObj.getInt("pic_id"));
				imodel.setPath(ParamsManager.bucketName+picinfoObj.getString("pic_url"));
				picInfoList.add(imodel);
			}
			model.setPicinfo(picInfoList);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			model=null;
		}
		return model;
	}
	
	/**
	 * ��ȡ�����ĳ�Ա�б�
	 * @param str
	 * @return
	 */
	public static ArrayList<UserModel> getActivityMember(String str) {
		ArrayList<UserModel> models=new ArrayList<UserModel>();
		try {
			JSONArray array=new JSONArray(str);
			for(int i=0;i<array.length();i++) {
				JSONObject obj=array.getJSONObject(i);
				UserModel model=new UserModel();
				model.setUser_id(obj.getInt("user_id"));
				model.setUser_name(obj.getString("user_name"));
				model.setIs_authentication(obj.getInt("is_authentication"));
				model.setAvatar_large(CommonUtils.convertNull(obj.getString("avatar_large")));
				
				model.setCity_name(CommonUtils.convertNull(obj.getString("city_name")));
				model.setCity_secret(obj.getInt("city_secret"));
				model.setIndustry(CommonUtils.convertNull(obj.getString("industry")));
				model.setIndustry_secret(obj.getInt("industry_secret"));
				model.setCompanyname(CommonUtils.convertNull(obj.getString("companyname")));
				model.setCompany_secret(obj.getInt("company_secret"));
				model.setYear(CommonUtils.convertNull(obj.getString("year")).equals("")?0:Integer.parseInt(CommonUtils.convertNull(obj.getString("year"))));
				model.setClass_name(CommonUtils.convertNull(obj.getString("class_name")));
				
				models.add(model);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return models;
	}
	
	/**
	 * ��ţtoekn
	 * @param str
	 * @return
	 */
	public static String getQiNiuToke(String str) {
		try {
			JSONObject obj=new JSONObject(str);
			return obj.getString("token");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * ��ȡ��ǰ�û���Ϣ
	 * @param str
	 * @return
	 */
	public static UserModel getCurrentUserModel(String str) {
		System.out.println(str);
		try {
			JSONObject obj=new JSONObject(str);
			UserModel model=new UserModel();
			model.setCity_name(obj.getString("city_name")==null?"":CommonUtils.convertNull(obj.getString("city_name")));
			model.setCity_secret(obj.getInt("city_secret"));
			model.setCompany_department(obj.getString("company_department")==null?"":CommonUtils.convertNull(obj.getString("company_department")));
			model.setDepartment_secret(obj.getInt("department_secret"));
			model.setAvatar_large(CommonUtils.convertNull(obj.getString("avatar_large")));
			model.setCompany_position(obj.getString("company_position")==null?"":CommonUtils.convertNull(obj.getString("company_position")));
			model.setPosition_secret(obj.getInt("position_secret"));
			try {
				model.setQq(obj.getLong("qq"));
			} catch(Exception e) {
				model.setQq(0);
			}
			model.setQq_secret(obj.getInt("qq_secret"));
			model.setService_name(obj.getString("service_name")==null?"":CommonUtils.convertNull(obj.getString("service_name")));
			model.setService_secret(obj.getInt("service_secret"));
			model.setUser_id(obj.getInt("user_id"));
			model.setUser_name(obj.getString("user_name")==null?"":CommonUtils.convertNull(obj.getString("user_name")));
			model.setPhonenum(obj.getString("phonenum")==null?"":CommonUtils.convertNull(obj.getString("phonenum")));
			model.setPhonenum_secret(obj.getInt("phonenum_secret"));
			model.setCompanyname(obj.getString("companyname")==null?"":CommonUtils.convertNull(obj.getString("companyname")));
			model.setCompany_secret(obj.getInt("company_secret"));
			model.setMail(obj.getString("mail")==null?"":CommonUtils.convertNull(obj.getString("mail")));
			model.setMail_secret(obj.getInt("mail_secret"));
			model.setIndustry(obj.getString("industry")==null?"":CommonUtils.convertNull(obj.getString("industry")));
			model.setIndustry_secret(obj.getInt("industry_secret"));
			if(!(obj.getString("personal_tags")==null||obj.getString("personal_tags").equals(""))) {
				String[] tags=obj.getString("personal_tags").split(",");
				ArrayList<String> personal_tags=new ArrayList<String>();
				for(int i=0;i<tags.length;i++) {
					personal_tags.add(tags[i]);
				}
				model.setPersonal_tags(personal_tags);
			}
			else {
				model.setPersonal_tags(new ArrayList<String>());
			}
			return model;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * ��ȡ����ģ�ͼ���
	 * @param str
	 * @param user_id 
	 * @return
	 */
	public static ArrayList<PrivateLetterModel> getPrivateLetterModel(String str, int user_id) {
		ArrayList<PrivateLetterModel> models=new ArrayList<PrivateLetterModel>();
		try {
			JSONObject obj=new JSONObject(str);
			JSONArray messages=obj.getJSONArray("messages");
			for(int i=0;i<messages.length();i++) {
				JSONObject messages_obj=messages.getJSONObject(i);
				PrivateLetterModel model=new PrivateLetterModel();
				model.setPrivate_letter_contenttime(messages_obj.getLong("push_time"));
				model.setPrivate_letter_to(1);
				model.setPrivate_letter_usercontent(messages_obj.getString("message"));
				model.setPrivate_letter_userid(user_id);
				model.setPrivate_letter_type(messages_obj.getInt("message_type"));
				model.setPrivate_letter_success(1);
				models.add(model);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return models;
	}
	
	public static long getExtraTime(String str) {
		try {
			JSONObject obj=new JSONObject(str);
			return obj.getLong("systimediff");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * ��ȡ��ƥ�䵽�����û���Ϣ
	 * @param str
	 * @return
	 */
	public static ArrayList<String> getVerificationUserList(String str) {
		System.out.println(str);
		ArrayList<String> lists=new ArrayList<String>();
		try {
			JSONObject obj_=new JSONObject(str);
			JSONArray array=obj_.getJSONArray("personinfos");
			for(int i=0;i<array.length();i++) {
				JSONObject obj=array.getJSONObject(i);
				String result=obj.getString("xingming")+"&"+obj.getString("xuehao")+"&"+obj.getString("college_name");
				lists.add(result);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lists;
	}
	
	/**
	 * ��ȡϵͳ��Ϣ
	 * @param str
	 * @return
	 */
	public static ArrayList<String> getSystemMessageList(String str) {
		ArrayList<String> lists=new ArrayList<String>();
		try {
			JSONArray array=new JSONArray(str);
			for(int i=0;i<array.length();i++) {
				JSONObject obj=array.getJSONObject(i);
				lists.add(obj.getString("push_time")+"&"+obj.getString("message"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lists;
	}
	
	/**
	 * ��ȡ�汾��Ϣ
	 * @param str
	 * @return
	 */
	public static HashMap<String, String> getVersionInfo(String str) {
		HashMap<String, String> map=new HashMap<String, String>();
		try {
			JSONObject obj=new JSONObject(str);
			map.put("version_desc", obj.getString("version_desc"));
			map.put("version_id", obj.getString("version_id"));
			map.put("url", obj.getString("url"));
			map.put("force_update", obj.getString("force_update"));
			return map;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * ��˴����༶��Ϣ
	 * @param str
	 * @return
	 */
	public static ReviewModel getReviewModel(String str) {
		ReviewModel model=new ReviewModel();
		try {
			JSONObject obj=new JSONObject(str);
			
			JSONObject cObj=obj.getJSONObject("classinfo");
			ClassInfoModel cmodel=new ClassInfoModel();
			cmodel.setClass_id(cObj.getInt("class_id"));
			cmodel.setClass_name(cObj.getString("class_name"));
			cmodel.setDegree_name(cObj.getString("degree_name"));
			cmodel.setYear(cObj.getInt("year"));
			cmodel.setTeacherName(cObj.getString("teacher"));
			cmodel.setCollege_name(cObj.getString("college_name"));
			JSONArray teacher_array=cObj.getJSONArray("teacherinfos");
			ArrayList<TeacherinfosModel> tmodels=new ArrayList<TeacherinfosModel>();
			for(int i=0;i<teacher_array.length();i++) {
				JSONObject tObj=teacher_array.getJSONObject(i);
				TeacherinfosModel tmodel=new TeacherinfosModel();
				tmodel.setCollege_name(tObj.getString("college_name"));
				tmodel.setDegree_name(tObj.getString("degree_name"));
				tmodel.setTeacher(tObj.getString("teacher"));
				tmodels.add(tmodel);
			}
			cmodel.setTeacher(tmodels);
			model.setCmodel(cmodel);
			
			JSONObject uObj=obj.getJSONObject("userinfo");
			UserModel uModel=new UserModel();
			uModel.setIndustry(CommonUtils.convertNull(uObj.getString("industry")).equals("")?"":uObj.getString("industry"));
			uModel.setCompanyname(CommonUtils.convertNull(uObj.getString("companyname")).equals("")?"":uObj.getString("companyname"));
			uModel.setCompany_secret(uObj.getInt("company_secret"));
			uModel.setIndustry_secret(uObj.getInt("industry_secret"));
			uModel.setAvatar_large(CommonUtils.convertNull(uObj.getString("avatar_large")).equals("")?"":uObj.getString("avatar_large"));
			uModel.setUser_id(uObj.getInt("user_id"));
			uModel.setUser_name(uObj.getString("user_name"));
			uModel.setCity_name(CommonUtils.convertNull(uObj.getString("city_name")).equals("")?"":uObj.getString("city_name"));
			uModel.setCity_secret(uObj.getInt("city_secret"));
			uModel.setIs_authentication(uObj.getInt("is_authentication"));
			model.setUmodel(uModel);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return model;
	}
	
	/**
	 * ��ȡ�������б�
	 * @param str
	 * @return
	 */
	public static ArrayList<TeacherinfosModel> getTeacherModels(String str) {
		ArrayList<TeacherinfosModel> lists=new ArrayList<TeacherinfosModel>();
		try {
			JSONArray array=new JSONArray(str);
			for(int i=0;i<array.length();i++) {
				JSONObject tObj=array.getJSONObject(i);
				TeacherinfosModel tmodel=new TeacherinfosModel();
				tmodel.setCollege_name(tObj.getString("college_name"));
				tmodel.setDegree_name(tObj.getString("degree_name"));
				tmodel.setTeacher(tObj.getString("teacher"));
				lists.add(tmodel);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lists;
	}
	
	/**
	 * ����У�ѻ������Ϣ
	 * @param str
	 * @return
	 */
	public static ApprovaluserModel getApprovaluserModel(String str, String type) {
		ApprovaluserModel model=new ApprovaluserModel();
		try {
			JSONObject obj=new JSONObject(str);
			model.setAvatar_large(CommonUtils.convertNull(obj.getString("avatar_large")).equals("")?"":obj.getString("avatar_large"));
			model.setApply_validation(CommonUtils.convertNull(obj.getString("apply_validation")).equals("")?"":obj.getString("apply_validation"));
			model.setIndustry(CommonUtils.convertNull(obj.getString("industry")).equals("")?"":obj.getString("industry"));
			model.setUser_id(obj.getInt("user_id"));
			model.setUser_name(obj.getString("user_name"));
			model.setAdmin_name(CommonUtils.convertNull(obj.getString("admin_name")).equals("")?"":obj.getString("admin_name"));
			if(type.equals("ClassInfoModel")) {
				model.setAluassociation_num(obj.getInt("student_num"));
				model.setAluassociation_pic(CommonUtils.convertNull(obj.getString("class_pic")).equals("")?"":obj.getString("class_pic"));
				model.setAluassociation_id(obj.getInt("class_id"));
				model.setAluassociation_name(CommonUtils.convertNull(obj.getString("class_name")).equals("")?"":obj.getString("class_name"));
			}
			else if(type.equals("AluassociationinfoModel")) {
				model.setAluassociation_num(obj.getInt("aluassociation_num"));
				model.setAluassociation_pic(CommonUtils.convertNull(obj.getString("aluassociation_pic")).equals("")?"":obj.getString("aluassociation_pic"));
				model.setAluassociation_id(obj.getInt("aluassociation_id"));
				model.setAluassociation_name(CommonUtils.convertNull(obj.getString("aluassociation_name")).equals("")?"":obj.getString("aluassociation_name"));
			}
			model.setCity_name(CommonUtils.convertNull(obj.getString("city_name")).equals("")?"":obj.getString("city_name"));
			model.setCompanyname(CommonUtils.convertNull(obj.getString("companyname")).equals("")?"":obj.getString("companyname"));
			model.setIs_authentication(obj.getInt("is_authentication"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return model;
	}
	
	/**
	 * ��ȡ����Ƶ���б�
	 * @param str
	 * @return
	 */
	public static ArrayList<PublicIndexModel> getPubchannelIndex(String str) {
		ArrayList<PublicIndexModel> models=new ArrayList<PublicIndexModel>();
		try {
			JSONObject obj=new JSONObject(str);
			JSONObject data=obj.getJSONObject("data");
			JSONArray publist=data.getJSONArray("publist");
			for(int i=0;i<publist.length();i++) {
				PublicIndexModel model=new PublicIndexModel();
				JSONObject temp=publist.getJSONObject(i);
				model.setGenerate_time(temp.getString("generate_time"));
				model.setResource_id(temp.getString("resource_id"));
				model.setResource_pic(temp.getString("resource_pic"));
				model.setResource_title(temp.getString("resource_title"));
				model.setResource_type(temp.getInt("resource_type"));
				model.setUser_name(temp.getString("user_name"));
				model.setView_times(temp.getInt("view_times"));
				model.setResource_url(temp.getString("resource_url"));
				models.add(model);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return models;
	}
	
	/**
	 * ��ȡ�����
	 * @param str
	 * @return
	 */
	public static PostDetailModel getPubchannelActivity(String str) {
		PostDetailModel model=new PostDetailModel();
		try {
			JSONObject obj=new JSONObject(str);
			JSONObject data=obj.getJSONObject("data");
			model.setActivity_desc(data.getString("activity_desc"));
			model.setActivity_fee_desc(data.getString("activity_fee_desc"));
			model.setActivity_id(data.getInt("activity_id"));
			model.setActivity_name(data.getString("activity_name"));
			model.setActivity_state(data.getInt("activity_state"));
			model.setAddress(data.getString("address"));
			model.setApply_time(data.getString("apply_time"));
			model.setAvatar_large(data.getString("avatar_large"));
			model.setBegin_time(data.getString("begin_time"));
			model.setCreate_org(data.getString("create_org"));
			model.setEnd_time(data.getString("end_time"));
			model.setGenerate_time(data.getString("generate_time"));
			model.setIs_apply(data.getInt("is_apply"));
			model.setOffdate(data.getString("offdate"));
			model.setPublish_type(data.getInt("publish_type"));
			model.setSuper_admin(data.getInt("super_admin"));
			model.setUser_id(data.getInt("user_id"));
			model.setUser_name(data.getString("user_name"));
			model.setView_times(data.getInt("view_times"));
			ArrayList<String> picinfo=new ArrayList<String>();
			JSONArray picinfoArray=data.getJSONArray("picinfo");
			for(int i=0;i<picinfoArray.length();i++) {
				JSONObject picinfo_obj=picinfoArray.getJSONObject(i);
				picinfo.add(ParamsManager.bucketName+picinfo_obj.getString("pic_url"));
			}
			model.setPicinfo(picinfo);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			model=null;
		}
		return model;
	}
	
	/**
	 * ����Ƶ����Ƹ/����/��������鿴
	 * @param str
	 * @return
	 */
	public static PostCopDetailModel getPubchannelResource(String str) {
		PostCopDetailModel model=new PostCopDetailModel();
		try {
			JSONObject obj=new JSONObject(str);
			JSONObject data=obj.getJSONObject("data");
			model.setAvatar_large(data.getString("avatar_large"));
			model.setCommentscount(data.getInt("commentscount"));
			model.setGenerate_time(data.getString("generate_time"));
			model.setPublish_author(data.getString("publish_author"));
			model.setPublish_type(data.getInt("publish_type"));
			model.setResource_category(data.getInt("resource_category"));
			model.setResource_content(data.getString("resource_content"));
			model.setResource_id(data.getInt("resource_id"));
			model.setResource_tags(data.getString("resource_tags"));
			model.setResource_title(data.getString("resource_title"));
			model.setResource_type(data.getInt("resource_type"));
			model.setUser_id(data.getInt("user_id"));
			model.setUser_name(data.getString("user_name"));
			model.setView_times(data.getInt("view_times"));
			JSONArray picinfo=data.getJSONArray("picinfo");
			ArrayList<String> picInfoList=new ArrayList<String>();
			for(int i=0;i<picinfo.length();i++) {
				JSONObject picinfoObj=picinfo.getJSONObject(i);
				picInfoList.add(picinfoObj.getString("pic_url"));
			}
			model.setPicInfoList(picInfoList);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			model=null;
		}
		return model;
	}
	
	/**
	 * ���۲鿴
	 * @param str
	 * @return
	 */
	public static ArrayList<PostCommentsModel> getPubchannelComments(String str) {
		ArrayList<PostCommentsModel> models=new ArrayList<PostCommentsModel>();
		try {
			JSONObject obj=new JSONObject(str);
			JSONObject data=obj.getJSONObject("data");
			JSONArray comments=data.getJSONArray("comments");
			for(int i=0;i<comments.length();i++) {
				PostCommentsModel model=new PostCommentsModel();
				model.setAvatar_large(comments.getJSONObject(i).getString("avatar_large"));
				model.setComent_content(comments.getJSONObject(i).getString("comment_content"));
				model.setComment_id(comments.getJSONObject(i).getInt("comment_id"));
				model.setGenerate_time(comments.getJSONObject(i).getString("generate_time"));
				model.setUser_id(comments.getJSONObject(i).getInt("user_id"));
				model.setUser_name(comments.getJSONObject(i).getString("user_name"));
				model.setCity_name(comments.getJSONObject(i).getString("city_name"));
				model.setClass_name(comments.getJSONObject(i).getString("class_name"));
				model.setCollege_name(comments.getJSONObject(i).getString("college_name"));
				model.setCompanyname(comments.getJSONObject(i).getString("companyname"));
				model.setIndustry(comments.getJSONObject(i).getString("industry"));
				model.setResource_id(comments.getJSONObject(i).getInt("resource_id"));
				model.setYear(comments.getJSONObject(i).getString("year"));
				models.add(model);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return models;
	}
	
	/**
	 * ����Ƶ��������
	 * @param str
	 * @return
	 */
	public static ArrayList<UserModel> getSearchMan(String str) {
		ArrayList<UserModel> models=new ArrayList<UserModel>();
		try {
			JSONObject obj=new JSONObject(str);
			JSONObject data=obj.getJSONObject("data");
			JSONArray mans=data.getJSONArray("mans");
			for(int i=0;i<mans.length();i++) {
				JSONObject man=mans.getJSONObject(i);
				UserModel model=new UserModel();
				model.setUser_id(man.getInt("user_id"));
				model.setUser_name(man.getString("user_name"));
				model.setAvatar_large(man.getString("avatar_large"));
				model.setCity_name(man.getString("city_name"));
				model.setIndustry(man.getString("industry"));
				model.setCompanyname(man.getString("companyname"));
				model.setYear(man.getString("year").equals("")?0:man.getInt("year"));
				model.setClass_name(man.getString("class_name"));
				model.setCollege_name(man.getString("college_name"));
				models.add(model);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return models;
	}
	
	/**
	 * ��ȡ������ʶ
	 * @param result
	 * @return
	 */
	public static String getDonateid(String result) {
		String donateid="-1";
		try {
			JSONObject obj=new JSONObject(result);
			if(obj.getInt("result")==1) {
				JSONObject data=obj.getJSONObject("data");
				donateid=data.getString("donateid");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return donateid;
	}
	
	/**
	 * ��ȡ����������־
	 * @param result
	 * @return
	 */
	public static String getDonate_order_id(String result) {
		String donate_order_id="";
		try {
			JSONObject obj=new JSONObject(result);
			if(obj.getInt("result")==1) {
				JSONObject data=obj.getJSONObject("data");
				donate_order_id=data.getString("donate_order_id");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return donate_order_id;
	}
	
	/**
	 * ��ȡ΢��Ԥ֧����Ϣ
	 * @param result
	 * @return
	 */
	public static PayModel getWeixinPayModel(String result) {
		PayModel model=null;
		try {
			JSONObject obj=new JSONObject(result);
			if(obj.getInt("result")==1) {
				JSONObject data=obj.getJSONObject("data");
				model=new PayModel();
				model.setAppid(data.getString("appid"));
				model.setNoncestr(data.getString("noncestr"));
				model.setPackage_(data.getString("package"));
				model.setPartnerid(data.getString("partnerid"));
				model.setPrepayid(data.getString("prepayid"));
				model.setSign(data.getString("sign"));
				model.setTimestamp(data.getString("timestamp"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			model=null;
		}
		return model;
	}
	
	/**
	 * ��ȡ����Ԥ֧����Ϣ
	 * @param result
	 * @return
	 */
	public static String getAliPayModel(String result) {
		String alipay_str="";
		try {
			JSONObject obj=new JSONObject(result);
			if(obj.getInt("result")==1) {
				alipay_str=obj.getString("alipay_str");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return alipay_str;
	}
	
	/**
	 * ֧��״̬
	 * @param result
	 * @return
	 */
	public static HashMap<String, String> getCheckResult(String result) {
		HashMap<String, String> map=new HashMap<String, String>();
		try {
			JSONObject obj=new JSONObject(result);
			if(obj.getInt("result")==1) {
				map.put("result", "1");
				JSONObject data=obj.getJSONObject("data");
				map.put("donate_money", data.getString("donate_money"));
				map.put("donate_state", data.getString("donate_state"));
			}
			else {
				map.put("result", "0");
				map.put("comments", obj.getString("comments"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("result", "0");
			map.put("comments", "��������ʧ�ܣ�");
		}
		return map;
	}
}
