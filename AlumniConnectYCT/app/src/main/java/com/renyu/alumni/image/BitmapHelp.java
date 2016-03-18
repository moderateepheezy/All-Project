package com.renyu.alumni.image;


import android.content.Context;

import com.lidroid.xutils.BitmapUtils;

public class BitmapHelp {
	
    private BitmapHelp() {
    	
    }

    private static BitmapUtils bitmapUtils;

    /**
     * BitmapUtils���ǵ����� ������Ҫ���ض����ȡʵ���ķ���
     *
     * @param appContext application context
     * @return
     */
    public static BitmapUtils getBitmapUtils(Context appContext) {
        if (bitmapUtils == null) {
            bitmapUtils = new BitmapUtils(appContext);
            //�ķ������Խ��ͼƬ���ز�������bug
            //bitmapUtils.clearCache();
        }
        return bitmapUtils;
    }
}
