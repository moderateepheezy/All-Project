package com.renyu.alumni.service;

import com.renyu.alumni.service.LoginCallBack;

interface AlumniLogin {
	void getLoginResult();
	//用来注册回调的对象
	void registerCallback(LoginCallBack callback);   
    void unregisterCallback(LoginCallBack callback);
}