package com.renyu.alumni.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class SlidingListView extends ListView implements OnScrollListener{

	/**
	 * ��Ļ���
	 */
	private int screenWidth;

	/**
	 * �ڱ��ж�Ϊ����֮ǰ�û���ָ�����ƶ���������
	 */
	private int touchSlop;

	/**
	 * �󲼾�
	 */
	private View leftLayout;

	/**
	 * �Ҳ���
	 */
	private View rightLayout;

	/**
	 * ��ǰ������
	 */
	private LinearSlidingLayout curChild;

	/**
	 * �ϴλ�����
	 */
	private LinearSlidingLayout priorChild;

	/**
	 * �󲼾ֲ���
	 */
	private MarginLayoutParams leftLayoutParams;

	/**
	 * �Ҳ��ֲ���
	 */
	private MarginLayoutParams rightLayoutParams;

	/**
	 * �Ӳ��������Ի����������Ե��
	 */
	private int leftEdge = 0;

	/**
	 * �Ӳ��������Ի��������ұ�Ե��
	 */
	private int rightEdge = 0;

	/**
	 * ���ڼ�����ָ�������ٶȡ�
	 */
	private VelocityTracker mVelocityTracker;

	/**
	 * ������ʾ��������಼��ʱ����ָ������Ҫ�ﵽ���ٶȡ�
	 */
	public static final int SNAP_VELOCITY = 200;

	/**
	 * ��¼��ָ����ʱ�ĺ����ꡣ
	 */
	private float xDown;

	/**
	 * ��¼��ָ����ʱ�������ꡣ
	 */
	private float yDown;

	/**
	 * ��¼��ָ�ƶ�ʱ�ĺ����ꡣ
	 */
	private float xMove;

	/**
	 * ��¼��ָ�ƶ�ʱ�������ꡣ
	 */
	private float yMove;

	/**
	 * ��¼�ֻ�̧��ʱ�ĺ����ꡣ
	 */
	private float xUp;

	/**
	 * ��ָ����ʱ��λ��
	 */
	private int downPosition = 0;

	/**
	 * ��һ�ΰ��µ�λ��
	 */
	private int priorPosition = -1;

	/**
	 * ��һ���ɼ�λ��
	 */
	private int firstVisiablePosition;
	
	/**
	 * �Ƿ��ڻ���
	 */
	private boolean isScrolling;

	public SlidingListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		setOnScrollListener(this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//�˴���������headview��footview������»��������ڼ�������
		downPosition = pointToPosition((int) event.getX(), (int) event.getY());
		//System.out.println(downPosition+" "+firstVisiablePosition+" "+getCount()+" "+getFooterViewsCount()+" "+getHeaderViewsCount());
		//��headview��footview�����ִ���������
		if(downPosition-getHeaderViewsCount()<0||downPosition+getFooterViewsCount()==getCount()) {
			return super.onTouchEvent(event);
		}
		
		createVelocityTracker(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			xDown = event.getRawX();
			yDown = event.getRawY();
			downPosition = pointToPosition((int) event.getX(), (int) event.getY());
			firstVisiablePosition = getFirstVisiblePosition();
			curChild = (LinearSlidingLayout) getChildAt(downPosition - firstVisiablePosition);
			//�����ʱ�����һ��ѡ�е�����
			try {
				if (priorPosition != -1) {
					priorChild = (LinearSlidingLayout) getChildAt(priorPosition - firstVisiablePosition);
					if (priorChild != null && downPosition != priorPosition && priorChild.isRightLayoutVisiable()) {
						priorChild.scrollToLeftLayout();
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			xMove = event.getRawX();
			yMove = event.getRawY();
			int moveDistanceX = (int) (xMove - xDown);
			int distanceY = (int) (yMove - yDown);
			if (Math.abs(moveDistanceX) > Math.abs(distanceY) && curChild != null && !isScrolling) {
				leftLayout = curChild.getChildAt(0);
				rightLayout = curChild.getChildAt(1);
				leftLayoutParams = (MarginLayoutParams) leftLayout.getLayoutParams();
				leftLayoutParams.width = screenWidth;
				leftLayout.setLayoutParams(leftLayoutParams);
				rightLayoutParams = (MarginLayoutParams) rightLayout.getLayoutParams();
				rightEdge = 0;
				leftEdge = rightLayoutParams.width;
					if (!curChild.isRightLayoutVisiable() && -moveDistanceX >= touchSlop && (curChild.isSliding() || Math.abs(distanceY) <= touchSlop)) {
						curChild.setSliding(true);
						leftLayoutParams.leftMargin = moveDistanceX;
						if (leftLayoutParams.leftMargin < -leftEdge) {
							leftLayoutParams.leftMargin = -leftEdge;
						}
						leftLayout.setLayoutParams(leftLayoutParams);
						rightLayoutParams.rightMargin = -leftEdge - moveDistanceX;
						if (rightLayoutParams.rightMargin > rightEdge) {
							rightLayoutParams.rightMargin = rightEdge;
						}
						rightLayout.setLayoutParams(rightLayoutParams);
					}
					if (curChild.isRightLayoutVisiable() && moveDistanceX >= touchSlop) {
						curChild.setSliding(true);
						rightLayoutParams.rightMargin = rightEdge - moveDistanceX;
						if (rightLayoutParams.rightMargin < -leftEdge) {
							rightLayoutParams.rightMargin = -leftEdge;
						}
						rightLayout.setLayoutParams(rightLayoutParams);
						leftLayoutParams.leftMargin = moveDistanceX - leftEdge;
						if (leftLayoutParams.leftMargin > rightEdge) {
							leftLayoutParams.leftMargin = rightEdge;
						}
						leftLayout.setLayoutParams(leftLayoutParams);
					}
			}
			break;
		case MotionEvent.ACTION_UP:
			xUp = event.getRawX();
			int upDistanceX = (int) (xUp - xDown);
			if (curChild != null && !isScrolling) {
				if (curChild.isSliding()) {
					// ��ָ̧��ʱ�������жϵ�ǰ���Ƶ���ͼ���Ӷ������ǹ�������಼�֣����ǹ������Ҳ಼��
					if (wantToShowLeftLayout()) {
						if (shouldScrollToLeftLayout()) {
							curChild.scrollToLeftLayout();
						} else {
							curChild.scrollToRightLayout();
						}
					} else if (wantToShowRightLayout()) {
						if (shouldScrollToRightLayout()) {
							curChild.scrollToRightLayout();
						} else {
							curChild.scrollToLeftLayout();
						}
					}
				}
				//����Ѿ������ˣ��ص��÷��������и�ԭ����
				else if (upDistanceX < touchSlop && curChild.isRightLayoutVisiable()) {
					curChild.scrollToLeftLayout();
				}
				//δ���������ʱ�򣬻ص��÷���������ִ�е��view����
				else {
					lis.click(downPosition);
				}
			}
			recycleVelocityTracker();
			priorPosition = downPosition;
			break;
		}
		//�����ǰ�������һ���������һ�����ɼ������ĵ��¼����������»���
		if (curChild != null && curChild.isSliding() || (priorChild != null && priorChild.isRightLayoutVisiable())) {
			return true;
		}
		return super.onTouchEvent(event);
	}
	
	OnSlidingListViewItemClickListener lis=null;
	public void setOnSlidingListViewItemClickListener(OnSlidingListViewItemClickListener lis) {
		this.lis=lis;
	}
	
	public interface OnSlidingListViewItemClickListener {
		public void click(int downPosition);
	}

	/**
	 * ����VelocityTracker���󣬲��������¼����뵽VelocityTracker���С�
	 * 
	 * @param event
	 *            �Ҳ಼�ּ����ؼ��Ļ����¼�
	 */
	private void createVelocityTracker(MotionEvent event) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
	}

	/**
	 * ��ȡ��ָ���Ҳ಼�ֵļ���View�ϵĻ����ٶȡ�
	 * 
	 * @return �����ٶȣ���ÿ�����ƶ��˶�������ֵΪ��λ��
	 */
	private int getScrollVelocity() {
		mVelocityTracker.computeCurrentVelocity(1000);
		int velocity = (int) mVelocityTracker.getXVelocity();
		return Math.abs(velocity);
	}

	/**
	 * ����VelocityTracker����
	 */
	private void recycleVelocityTracker() {
		mVelocityTracker.recycle();
		mVelocityTracker = null;
	}

	/**
	 * �жϵ�ǰ���Ƶ���ͼ�ǲ�������ʾ�Ҳ಼�֡������ָ�ƶ��ľ����Ǹ������ҵ�ǰ�Ҳ಼���ǲ��ɼ��ģ�����Ϊ��ǰ��������Ҫ��ʾ�Ҳ಼�֡�
	 * 
	 * @return ��ǰ��������ʾ�Ҳ಼�ַ���true�����򷵻�false��
	 */
	private boolean wantToShowRightLayout() {
		return xUp - xDown < 0;
	}

	/**
	 * �жϵ�ǰ���Ƶ���ͼ�ǲ�������ʾ��಼�֡������ָ�ƶ��ľ������������ҵ�ǰ�Ҳ಼���ǿɼ��ģ�����Ϊ��ǰ��������Ҫ��ʾ��಼�֡�
	 * 
	 * @return ��ǰ��������ʾ��಼�ַ���true�����򷵻�false��
	 */
	private boolean wantToShowLeftLayout() {
		return xUp - xDown > 0;
	}

	/**
	 * �ж��Ƿ�Ӧ�ù�������಼��չʾ�����������ָ�ƶ���������Ҳ಼�ֵ�1/2��������ָ�ƶ��ٶȴ���SNAP_VELOCITY��
	 * ����ΪӦ�ù�������಼��չʾ������
	 * 
	 * @return ���Ӧ�ù�������಼��չʾ��������true�����򷵻�false��
	 */
	private boolean shouldScrollToLeftLayout() {
		return xUp - xDown > rightLayoutParams.width / 2 || getScrollVelocity() > SNAP_VELOCITY;
	}

	/**
	 * �ж��Ƿ�Ӧ�ù������Ҳ಼��չʾ�����������ָ�ƶ���������Ҳ಼�ֵ�1/2�� ������ָ�ƶ��ٶȴ���SNAP_VELOCITY��
	 * ����ΪӦ�ù������Ҳ಼��չʾ������
	 * 
	 * @return ���Ӧ�ù������Ҳ಼��չʾ��������true�����򷵻�false��
	 */
	private boolean shouldScrollToRightLayout() {
		return xDown - xUp > rightLayoutParams.width / 2 || getScrollVelocity() > SNAP_VELOCITY;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * �ж��Ƿ������»���
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {
		case OnScrollListener.SCROLL_STATE_FLING:
			isScrolling = true;
			break;
		case OnScrollListener.SCROLL_STATE_IDLE:
			isScrolling = false;
			break;
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
			isScrolling = true;
			break;

		default:
			break;
		}
		
	}
	
	

}
