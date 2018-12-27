package com.vcyber.baselibrary.pictureselector.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vcyber.baselibrary.R;
import com.vcyber.baselibrary.pictureselector.Interface.IFolderChangeListener;
import com.vcyber.baselibrary.pictureselector.PicSelectorControl;
import com.vcyber.baselibrary.pictureselector.PictureSelectorActivity;
import com.vcyber.baselibrary.pictureselector.entity.Folder;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Author   : jack
 * Date     :2018/9/5 12:19
 * E-mail   : 1215530740@qq.com
 * Describe :
 */
public class FolderAdapter extends BaseAdapter {

    private IFolderChangeListener mListener;
    List<Folder> mFolderList;
    PicSelectorControl mControl;
    Context mContext;
    private LayoutInflater mInflater;
    //默认被选中的是0，即第一个文件夹
    private int mSelected = 0;

    public FolderAdapter(Context context, List<Folder> folderList, PicSelectorControl control) {
        this.mFolderList = folderList;
        this.mContext = context;
        this.mControl = control;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        if (mFolderList != null) {
            return mFolderList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mFolderList != null ? mFolderList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        //如果view未被实例化过，缓存池中没有对应的缓存
        if (convertView == null) {
            viewHolder = new ViewHolder();
            // 由于我们只需要将XML转化为View，并不涉及到具体的布局，所以第二个参数通常设置为null
            convertView = mInflater.inflate(R.layout.item_img_sel_folder, null);
            //对viewHolder的属性进行赋值
            viewHolder.ivFolder = (ImageView) convertView.findViewById(R.id.ivFolder);
            viewHolder.ivIndicator = (ImageView) convertView.findViewById(R.id.indicator);
            viewHolder.tvFolderName = (TextView) convertView.findViewById(R.id.tvFolderName);
            viewHolder.tvImageNum = (TextView) convertView.findViewById(R.id.tvImageNum);
            //通过setTag将convertView与viewHolder关联
            convertView.setTag(viewHolder);
        } else {//如果缓存池中有对应的view缓存，则直接通过getTag取出viewHolder
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Folder folder = mFolderList.get(position);

        // 设置控件的数据

        Glide.with(mContext).load(folder.cover.filePath).into(viewHolder.ivFolder);
        viewHolder.ivIndicator.setVisibility(View.GONE);
        viewHolder.tvFolderName.setText(folder.name);
        viewHolder.tvImageNum.setText(String.format(mContext.getResources().getString(R.string.folder_all),folder.images.size()));

        if(mSelected==position){
            viewHolder.ivIndicator.setVisibility(View.VISIBLE);
        }else{
            viewHolder.ivIndicator.setVisibility(View.GONE);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectIndex(position);
            }
        });
        return convertView;
    }

    public void setOnFloderChangeListener(IFolderChangeListener listener) {
        this.mListener = listener;
    }

    public void setSelectIndex(int position) {
        if (mSelected == position)
            return;
        if (mListener != null)
            mListener.onChange(position, mFolderList.get(position));
        mSelected = position;
        notifyDataSetChanged();
    }

    public int getSelectIndex() {
        return mSelected;
    }

    // ViewHolder用于缓存控件，三个属性分别对应item布局文件的三个控件
    class ViewHolder {
        public ImageView ivFolder;
        public ImageView ivIndicator;
        public TextView tvFolderName;
        public TextView tvImageNum;
    }
}
