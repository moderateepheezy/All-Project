package com.renyu.alumni.service;

import com.renyu.alumni.service.LoginCallBack;

interface AlumniLogin {
	void getLoginResult();
	//����ע��ص��Ķ���
	void registerCallback(LoginCallBack callback);   
    void unregisterCallback(LoginCallBack callback);
}