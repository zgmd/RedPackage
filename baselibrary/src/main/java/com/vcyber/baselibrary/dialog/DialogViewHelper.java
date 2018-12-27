package com.vcyber.baselibrary.dialog;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Author   : jack
 * Date     : 2018/8/23 10:11
 * E-mail   : 1215530740@qq.com
 * Describe :
 */
public class DialogViewHelper {

    private View mContentView;
    //存放通过findviewbyid找到的view
    private SparseArray<WeakReference<View>> mViews = new SparseArray<>();

    public DialogViewHelper(Context context, int viewId) {
        this.mContentView = LayoutInflater.from(context).inflate(viewId, null);
    }

    public DialogViewHelper(Context context, View view) {
        this.mContentView = view;
    }

    public void setContentView(View contentView) {
        this.mContentView = contentView;
    }

    public View getContentView() {
        return mContentView;
    }

    public void setOnClickListener(int viewId, View.OnClickListener listener) {
//        if (mContentView != null) {
//            View view = mContentView.findViewById(viewId);
//
//            if (view == null) {
//                new IllegalArgumentException("点击事件的viewId没有找到，请检查CustomDialog.Builder.setOnClickListener()方法");
//            }
//            view.setOnClickListener(listener);
//        } else {
//            throw new IllegalArgumentException("dialog的view没有设置，可以通过setContentView()来设置");
//        }

        getView(viewId).setOnClickListener(listener);

    }

    public void setText(int viewId, CharSequence text) {
           View view =  getView(viewId);
           if(view instanceof TextView){
               ((TextView) view).setText(text);
           }else if(view instanceof Button){
               ((Button) view).setText(text);
           }else if(view instanceof EditText){
               ((EditText) view).setText(text);
           }else {
               throw new ClassCastException("您需要设置文本的view，不支持文本设置");
           }
    }

    private View getView(int viewId) {
        if (mContentView == null) {
            throw new IllegalArgumentException("dialog的view没有设置，可以通过setContentView()来设置");
        }
        View tragView = (mViews.get(viewId)!= null ? mViews.get(viewId).get():null);
        if (tragView == null) {
            tragView = mContentView.findViewById(viewId);
            if (tragView == null) {
                 throw new IllegalArgumentException("viewId没有找到,您输入的viewID有误，或者重新build一下工程");
            }
            mViews.put(viewId, new WeakReference<View>(tragView));
        }
        return tragView;
    }
}
