package com.danielhan.codelayout;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;


/**
 * @author DanielHan
 * @date 2018/1/25
 */

public class CodeLayout extends LinearLayout implements LifecycleObserver {
    //数字位数
    private int count = 6;
    private LayoutInflater layoutInflater;
    private View itemView;
    private StringBuilder mStringBuilder = new StringBuilder();
    private OnCodeListener mOnCodeListener;
    private int curPosition;
    private String TAG = this.getClass().getSimpleName();
    private Context mContext;

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().length() == 1) {
                mStringBuilder.append(s);
                if (curPosition != count - 1) {
                    View curView = getChildAt(curPosition);
                    curView.setFocusable(false);
                    curView.setFocusableInTouchMode(false);
                    curPosition++;
                    curView = getChildAt(curPosition);
                    curView.setFocusable(true);
                    curView.setFocusableInTouchMode(true);
                    curView.requestFocus();
                } else {
                    if (mStringBuilder.length() == count && mOnCodeListener != null) {
                        mOnCodeListener.onComplete();
                    }
                }
            }
        }
    };

    public CodeLayout(Context context) {
        this(context, null);
    }

    public CodeLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CodeLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
        for (int i = 0; i < count; i++) {
            itemView = layoutInflater.inflate(R.layout.item_codeview, null);
            LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.leftMargin = 20;
            itemView.setLayoutParams(params);
            if (i != 0) {
                ((EditText) itemView).setFocusable(false);
                ((EditText) itemView).setFocusableInTouchMode(false);
            }
            ((EditText) itemView).addTextChangedListener(textWatcher);
            addView(itemView);
        }
    }

    public interface OnCodeListener {
        void onComplete();
    }

    public void setmOnCodeListener(OnCodeListener mOnCodeListener) {
        this.mOnCodeListener = mOnCodeListener;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void openKeyborad() {
        Log.e(TAG, "Called From CodeLayout Class, called onResume() of Activity >>>>>> openKeyborad()");
        getChildAt(0).postDelayed(new Runnable() {
            @Override
            public void run() {
                SoftInputUtil.openKeybord((EditText) getChildAt(0), mContext.getApplicationContext());
            }
        }, 300);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        Log.e(TAG, "Called From CodeLayout Class, called onPause() of Activity >>>>>> onPause()");
        SoftInputUtil.closeKeybord((EditText) getChildAt(0), mContext.getApplicationContext());
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        Log.e(TAG, "Called From CodeLayout Class, called onStop() of Activity >>>>>> onStop()");
        SoftInputUtil.closeKeybord((EditText) getChildAt(0), mContext.getApplicationContext());
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void cleanup() {
        Log.e(TAG, "Called From CodeLayout Class, called onDestroy() of Activity >>>>>> cleanup()");
        for (int i = 0; i < count; i++) {
            EditText item = (EditText) getChildAt(i);
            item.removeTextChangedListener(textWatcher);
        }
        SoftInputUtil.closeKeybord((EditText) getChildAt(0), mContext.getApplicationContext());
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (curPosition != 0 && curPosition != mStringBuilder.length() - 1) {
                View curView = getChildAt(curPosition);
                curView.setFocusable(false);
                curView.setFocusableInTouchMode(false);
                curPosition--;
                curView = getChildAt(curPosition);
                curView.setFocusable(true);
                curView.setFocusableInTouchMode(true);
                curView.requestFocus();
                ((EditText) curView).setText("");
                mStringBuilder.deleteCharAt(curPosition);
            } else if (curPosition == mStringBuilder.length() - 1) {
                View curView = getChildAt(curPosition);
                ((EditText) curView).setText("");
                mStringBuilder.deleteCharAt(curPosition);
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}
