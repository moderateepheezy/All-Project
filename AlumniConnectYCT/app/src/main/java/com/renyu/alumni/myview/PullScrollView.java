package com.renyu.alumni.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

import com.renyu.alumni.R;

/**
 * �Զ���ScrollView
 *
 * @author markmjw
 * @date 2013-09-13
 */
public class PullScrollView extends ScrollView {
    /** ����ϵ��,ԽС������Խ��. */
    private static final float SCROLL_RATIO = 0.5f;

    /** ��������ת�ľ���. */
    private static final int TURN_DISTANCE = 100;

    /** ͷ��view. */
    private View mHeader;

    /** ͷ��view�߶�. */
    private int mHeaderHeight;

    /** ͷ��view��ʾ�߶�. */
    private int mHeaderVisibleHeight;

    /** ScrollView��content view. */
    private View mContentView;

    /** ScrollView��content view����. */
    private Rect mContentRect = new Rect();

    /** �״ε����Y����. */
    private float mTouchDownY;

    /** �Ƿ�ر�ScrollView�Ļ���. */
    private boolean mEnableTouch = false;

    /** �Ƿ�ʼ�ƶ�. */
    private boolean isMoving = false;

    /** �Ƿ��ƶ�������λ��. */
    private boolean isTop = false;

    /** ͷ��ͼƬ��ʼ�����͵ײ�. */
    private int mInitTop, mInitBottom;

    /** ͷ��ͼƬ�϶�ʱ�����͵ײ�. */
    private int mCurrentTop, mCurrentBottom;

    /** ״̬�仯ʱ�ļ�����. */
    private OnTurnListener mOnTurnListener;

    private enum State {
        /**����*/
        UP,
        /**�ײ�*/
        DOWN,
        /**����*/
        NORMAL
    }

    /** ״̬. */
    private State mState = State.NORMAL;

    public PullScrollView(Context context) {
        super(context);
        init(context, null);
    }

    public PullScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PullScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // set scroll mode
        setOverScrollMode(OVER_SCROLL_NEVER);

        if (null != attrs) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PullScrollView);

            if (ta != null) {
                mHeaderHeight = (int) ta.getDimension(R.styleable.PullScrollView_headerHeight, -1);
                mHeaderVisibleHeight = (int) ta.getDimension(R.styleable
                        .PullScrollView_headerVisibleHeight, -1);
                ta.recycle();
            }
        }
    }

    /**
     * ����Header
     *
     * @param view
     */
    public void setHeader(View view) {
        mHeader = view;
    }

    /**
     * ����״̬�ı�ʱ�ļ�����
     *
     * @param turnListener
     */
    public void setOnTurnListener(OnTurnListener turnListener) {
        mOnTurnListener = turnListener;
    }

    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            mContentView = getChildAt(0);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (getScrollY() == 0) {
            isTop = true;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mTouchDownY = ev.getY();
            mCurrentTop = mInitTop = mHeader.getTop();
            mCurrentBottom = mInitBottom = mHeader.getBottom();
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mContentView != null) {
            doTouchEvent(ev);
        }

        // ��ֹ�ؼ�����Ļ���.
        return mEnableTouch || super.onTouchEvent(ev);
    }

    /**
     * �����¼�����
     *
     * @param event
     */
    private void doTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_MOVE:
                doActionMove(event);
                break;

            case MotionEvent.ACTION_UP:
                // �ع�����
                if (isNeedAnimation()) {
                    rollBackAnimation();
                }

                if (getScrollY() == 0) {
                    mState = State.NORMAL;
                }

                isMoving = false;
                mEnableTouch = false;
                break;

            default:
                break;
        }
    }

    /**
     * ִ���ƶ�����
     *
     * @param event
     */
    private void doActionMove(MotionEvent event) {
        // ������������ʱ����״̬����Ϊ�����������������϶��������϶������˺��״δ�������Ӧ������
        if (getScrollY() == 0) {
            mState = State.NORMAL;

            // ��������������ʼλ��ʱ������Touch down������Ϊ��ǰTouch�������
            if (isTop) {
                isTop = false;
                mTouchDownY = event.getY();
            }
        }

        float deltaY = event.getY() - mTouchDownY;

        // �����״�Touch����Ҫ�жϷ�λ��UP OR DOWN
        if (deltaY < 0 && mState == State.NORMAL) {
            mState = State.UP;
        } else if (deltaY > 0 && mState == State.NORMAL) {
            mState = State.DOWN;
        }

        if (mState == State.UP) {
            deltaY = deltaY < 0 ? deltaY : 0;

            isMoving = false;
            mEnableTouch = false;

        } else if (mState == State.DOWN) {
            if (getScrollY() <= deltaY) {
                mEnableTouch = true;
                isMoving = true;
            }
            deltaY = deltaY < 0 ? 0 : deltaY;
        }

        if (isMoving) {
            // ��ʼ��content view����
            if (mContentRect.isEmpty()) {
                // ���������Ĳ���λ��
                mContentRect.set(mContentView.getLeft(), mContentView.getTop(), mContentView.getRight(),
                        mContentView.getBottom());
            }

            // ����header�ƶ�����(�����ƶ��ľ���*����ϵ��*0.5)
            float headerMoveHeight = deltaY * 0.5f * SCROLL_RATIO;
            mCurrentTop = (int) (mInitTop + headerMoveHeight);
            mCurrentBottom = (int) (mInitBottom + headerMoveHeight);

            // ����content�ƶ�����(�����ƶ��ľ���*����ϵ��)
            float contentMoveHeight = deltaY * SCROLL_RATIO;

            // ����content�ƶ��ľ��룬���ⳬ��header�ĵױ�Ե
            int headerBottom = mCurrentBottom - mHeaderVisibleHeight;
            int top = (int) (mContentRect.top + contentMoveHeight);
            int bottom = (int) (mContentRect.bottom + contentMoveHeight);

            if (top <= headerBottom) {
                // �ƶ�content view
                mContentView.layout(mContentRect.left, top, mContentRect.right, bottom);

                // �ƶ�header view
                mHeader.layout(mHeader.getLeft(), mCurrentTop, mHeader.getRight(), mCurrentBottom);
            }
        }
    }

    private void rollBackAnimation() {
        TranslateAnimation tranAnim = new TranslateAnimation(0, 0,
                Math.abs(mInitTop - mCurrentTop), 0);
        tranAnim.setDuration(200);
        mHeader.startAnimation(tranAnim);

        mHeader.layout(mHeader.getLeft(), mInitTop, mHeader.getRight(), mInitBottom);

        // �����ƶ�����
        TranslateAnimation innerAnim = new TranslateAnimation(0, 0, mContentView.getTop(), mContentRect.top);
        innerAnim.setDuration(200);
        mContentView.startAnimation(innerAnim);
        mContentView.layout(mContentRect.left, mContentRect.top, mContentRect.right, mContentRect.bottom);

        mContentRect.setEmpty();

        // �ص�������
        if (mCurrentTop > mInitTop + TURN_DISTANCE && mOnTurnListener != null){
            mOnTurnListener.onTurn();
        }
    }

    /**
     * �Ƿ���Ҫ��������
     */
    private boolean isNeedAnimation() {
        return !mContentRect.isEmpty() && isMoving;
    }

    /**
     * ��ת�¼�������
     *
     * @author markmjw
     */
    public interface OnTurnListener {
        /**
         * ��ת�ص�����
         */
        public void onTurn();
    }
}
