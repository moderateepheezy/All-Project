package com.renyu.alumni.myview;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class LinearSlidingLayout extends LinearLayout {

	private int screenWidth;


	private View leftLayout;


	private View rightLayout;


	private MarginLayoutParams leftLayoutParams;

	/**
	 * �Ҳ಼�ֲ���
	 */
	private MarginLayoutParams rightLayoutParams;

	/**
	 * �Ҳ಼�������Ի����������Ե��
	 */
	private int leftEdge = 0;

	/**
	 * �Ҳ಼�������Ի��������ұ�Ե��
	 */
	private int rightEdge = 0;

	/**
	 * ������ʾ��������಼��ʱ����ָ������Ҫ�ﵽ���ٶȡ�
	 */
	public static final int SNAP_VELOCITY = 200;

	/**
	 * �Ҳ಼�ֵ�ǰ����ʾ�������ء�ֻ����ȫ��ʾ������ʱ�Ż���Ĵ�ֵ�����������д�ֵ��Ч��
	 */
	private boolean isRightLayoutVisible;

	/**
	 * �Ƿ����ڻ�����
	 */
	private boolean isSliding;

	public LinearSlidingLayout(Context context) {
		super(context);
	}

	public LinearSlidingLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			// ��ȡ��಼�ֶ���
			leftLayout = getChildAt(0);
			leftLayoutParams = (MarginLayoutParams) leftLayout.getLayoutParams();
			leftLayoutParams.width = screenWidth;
			leftLayout.setLayoutParams(leftLayoutParams);
			// ��ȡ�Ҳ಼�ֶ���
			rightLayout = getChildAt(1);
			rightLayoutParams = (MarginLayoutParams) rightLayout.getLayoutParams();
			rightEdge = 0;
			leftEdge = rightLayoutParams.width;
		}
	}

	/**
	 * ʹ��ǰ�߳�˯��ָ���ĺ�������
	 * 
	 * @param millis
	 *            ָ����ǰ�߳�˯�߶�ã��Ժ���Ϊ��λ
	 */
	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	class ScrollTask extends AsyncTask<Integer, Integer, Integer> {

		@Override
		protected Integer doInBackground(Integer... speed) {
			int rightMargin = rightLayoutParams.rightMargin;
			// ���ݴ�����ٶ����������棬������������߽���ұ߽�ʱ������ѭ����
			while (true) {
				rightMargin = rightMargin + speed[0];
				if (rightMargin > rightEdge) {
					rightMargin = rightEdge;
					break;
				}
				if (rightMargin < -leftEdge) {
					rightMargin = -leftEdge;
					break;
				}
				publishProgress(rightMargin);
				sleep(speed[1]);
			}
			if (speed[0] > 0) {
				isRightLayoutVisible = true;
			} else {
				isRightLayoutVisible = false;
			}
			isSliding = false;
			return rightMargin;
		}

		@Override
		protected void onProgressUpdate(Integer... rightMargin) {
			rightLayoutParams.rightMargin = rightMargin[0];
			rightLayout.setLayoutParams(rightLayoutParams);
			leftLayoutParams.leftMargin = -leftEdge - rightMargin[0];
			leftLayout.setLayoutParams(leftLayoutParams);
		}

		@Override
		protected void onPostExecute(Integer rightMargin) {
			rightLayoutParams.rightMargin = rightMargin;
			rightLayout.setLayoutParams(rightLayoutParams);
			leftLayoutParams.leftMargin = -leftEdge - rightMargin;
			leftLayout.setLayoutParams(leftLayoutParams);
		}
	}

	/**
	 * �ⲿ���÷���
	 * 
	 * @return
	 */
	public boolean isRightLayoutVisiable() {
		return isRightLayoutVisible;
	}

	public void setRightLayoutVisiable(boolean visiable) {
		this.isRightLayoutVisible = visiable;
	}

	public boolean isSliding() {
		return isSliding;
	}

	public void setSliding(boolean sliding) {
		this.isSliding = sliding;
	}


	public void scrollToLeftLayout() {
		new ScrollTask().execute(-30, 20);
	}

	public void scrollToRightLayout() {
		new ScrollTask().execute(30, 20);
	}

	public void scrollWithoutDelay() {
		new ScrollTask().execute(-30, 0);
	}
}
