package com.vcyber.baselibrary.pictureselector;

import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;

import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.vcyber.baselibrary.R;
import com.vcyber.baselibrary.permission.Permission;
import com.vcyber.baselibrary.permission.PermissionHelper;
import com.vcyber.baselibrary.permission.requestPermissionFail;
import com.vcyber.baselibrary.pictureselector.Constant.SelectedImgList;
import com.vcyber.baselibrary.pictureselector.Interface.IFolderChangeListener;
import com.vcyber.baselibrary.pictureselector.Interface.ILoadPicListener;
import com.vcyber.baselibrary.pictureselector.Interface.IPreviewOnClickListener;
import com.vcyber.baselibrary.pictureselector.Interface.ItemOnClickListener;
import com.vcyber.baselibrary.pictureselector.adapter.FolderAdapter;
import com.vcyber.baselibrary.pictureselector.adapter.ImgAdapter;
import com.vcyber.baselibrary.pictureselector.adapter.PreviewAdapter;
import com.vcyber.baselibrary.pictureselector.entity.Folder;
import com.vcyber.baselibrary.pictureselector.entity.MediaFile;
import com.vcyber.baselibrary.utils.FileUtils;
import com.vcyber.baselibrary.utils.Logger;
import com.vcyber.baselibrary.widget.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PictureSelectorActivity extends AppCompatActivity implements View.OnClickListener {

    //裁剪code
    private static final int CLIP_CODE = 0X99;
    PicSelectorControl mControl;
    private RecyclerView mRvImageList;
    //页面标题
    private TextView mTvTitle;
    //显示图片的adapter
    private ImgAdapter mImgAdapter;
    //图片列表集合
    private ArrayList<MediaFile> mImgList = new ArrayList<>();
    //文件夹集合
    private List<Folder> mFolderList = new ArrayList<>();
    //是否需要生成文件夹
    private boolean mHasFolderGened;
    //预览图片的viewPager
    private ViewPager mPreviewVP;
    //预览图片的adapter
    private PreviewAdapter mPreviewAdapter;
    //返回键
    private ImageView mIvBack;
    //确认键
    private Button mBtnConfirm;
    //选择文件夹
    private Button mBtnFolderSelect;
    //通过相机拍摄图片，保存文件
    private File mTempFile;

    private static final int LOADER_ALL = 0;
    public static final int REQUEST_SD_PERMISSION_CODE = 0X11;
    public static final int REQUEST_CAMERA_PERMISSION_CODE = 0X22;
    private ListPopupWindow mFolderPopupWindow;
    private FolderAdapter mFolderListAdapter;
    private static final int REQUEST_CAMERA_CODE = 0X12;
    private boolean mIsOpenCamera = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        mControl = (PicSelectorControl) getIntent().getExtras().get("control");
        Logger.i("打开了选择图片的acitivity,config--->" + mControl);

        if (mControl.mOpenCamera) {
            Logger.i("直接打开打开相机");
            PermissionHelper.proxy(PictureSelectorActivity.this, REQUEST_CAMERA_PERMISSION_CODE);
        } else {
            initView();
            PermissionHelper.proxy(PictureSelectorActivity.this, REQUEST_SD_PERMISSION_CODE);
            Logger.e("哎呀--》onCreate（）");
            initData();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mControl.mOpenCamera) {
            mIsOpenCamera = true;
        }

    }

    /**
     * 初始化view相关
     */
    private void initView() {
        //设置布局view
        View contentView = null;
        if (mControl.mPicSelectorViewId != 0) {
            contentView = LayoutInflater.from(this).inflate(mControl.mPicSelectorViewId, null);
        }
        if (mControl.mPicSelectorView != null) {
            contentView = mControl.mPicSelectorView;
        }
        if (contentView == null) {
            contentView = LayoutInflater.from(this).inflate(R.layout.activity_picture_selector, null);
        }
        setContentView(contentView);
        mRvImageList = contentView.findViewById(mControl.mRecyclerViewId = mControl.mRecyclerViewId == 0 ? R.id.rvImageList : mControl.mRecyclerViewId);
        mTvTitle = contentView.findViewById(mControl.mTitleId = mControl.mTitleId == 0 ? R.id.tvTitle : mControl.mTitleId);
        mPreviewVP = contentView.findViewById(mControl.mVPId = mControl.mVPId == 0 ? R.id.viewPager : mControl.mVPId);
        mIvBack = contentView.findViewById(mControl.mBackId = mControl.mBackId == 0 ? R.id.ivBack : mControl.mBackId);
        mBtnConfirm = contentView.findViewById(mControl.mConfirmId = mControl.mConfirmId == 0 ? R.id.btnConfirm : mControl.mConfirmId);
        mBtnFolderSelect = contentView.findViewById(mControl.mFolderSelect = mControl.mFolderSelect == 0 ? R.id.btnSelectFolder : mControl.mFolderSelect);
        if (mControl.mMultiSelect) {
            mBtnConfirm.setText(getString(R.string.confirm) + "(" + SelectedImgList.mList.size() + "/" + mControl.mMaxSize + ")");
        } else {
            mBtnConfirm.setVisibility(View.GONE);
        }
        mTvTitle.setText(getString(R.string.all_images));
        mBtnConfirm.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        mBtnFolderSelect.setOnClickListener(this);
        mBtnFolderSelect.setText(getString(R.string.all_images));
        mImgAdapter = new ImgAdapter(this, mControl, mImgList);
        mImgAdapter.setmItemOnClickListener(imgsItemOnClickListener);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, mControl.mColumn);
        mRvImageList.setLayoutManager(layoutManager);
        mRvImageList.setAdapter(mImgAdapter);

        mPreviewAdapter = new PreviewAdapter(PictureSelectorActivity.this, mImgList, mControl);
        mPreviewVP.setAdapter(mPreviewAdapter);
        mPreviewAdapter.setPreviewOnClickListener(mPreviewClickListener);
    }

    /**
     * 读取图库数据加载，配置
     */
    private void initData() {
        ReadPicHelper.getInstacne(this).setLoadPicListener(new ILoadPicListener() {
            @Override
            public void onLoadFinsh(ArrayList<MediaFile> imgs, ArrayList<Folder> folders) {
                mImgList.clear();
                if (mControl.mFirstItemOpenCamera) {
                    mImgList.add(new MediaFile());
                }
                mFolderList.addAll(folders);
                mImgList.addAll(imgs);
                mImgAdapter.notifyDataSetChanged();
                mPreviewAdapter.notifyDataSetChanged();
                Logger.e("哎呀--》initData()");
            }
        });
    }


    /**
     * 读取SD（图库）卡权限申请成功回调
     *
     * @param id
     */
    @Permission(permissionNames = Manifest.permission.READ_EXTERNAL_STORAGE, id = REQUEST_SD_PERMISSION_CODE)
    protected void loadImg(int id) {
        LoaderManager.getInstance(this).initLoader(LOADER_ALL, null, ReadPicHelper.getInstacne(this).mLoaderCallback);
    }

    /**
     * 权限授权失败回调
     *
     * @param id
     */
    @requestPermissionFail
    public void requestPermissionFail(int id) {
        switch (id) {
            case REQUEST_SD_PERMISSION_CODE:
                ToastUtils.show(this, getString(R.string.request_sd_permission_fail_tip));
                break;
            case REQUEST_CAMERA_PERMISSION_CODE:
                ToastUtils.show(this, getString(R.string.request_camera_permission_fail_tip));
                break;
            default:
                ToastUtils.show(this, getString(R.string.request_permission_fail_tip));
                break;
        }
    }

    /**
     * 打开相机拍照
     */
    @Permission(permissionNames = Manifest.permission.CAMERA, id = REQUEST_CAMERA_PERMISSION_CODE)
    private void openCamera(int id) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            mTempFile = new File(FileUtils.createRootPath(this) + "/" + System.currentTimeMillis() + ".jpg");
            Logger.i("file uri path--->" + mTempFile.getAbsolutePath());
            FileUtils.createFile(mTempFile);

            Uri uri = FileProvider.getUriForFile(this,
                    FileUtils.getApplicationId(this) + ".image_provider", mTempFile);
            Logger.i("uri--->" + uri);

            //检查相机的intent 对应的activity是否授予了可通过URI访问的权限
//            List<ResolveInfo> resInfoList = getPackageManager()
//                    .queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY);
//            for (ResolveInfo resolveInfo : resInfoList) {
//                String packageName = resolveInfo.activityInfo.packageName;
//                grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION
//                        | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            }

            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri); //Uri.fromFile(tempFile)
            startActivityForResult(cameraIntent, REQUEST_CAMERA_CODE);
        } else {
            Toast.makeText(this, getString(R.string.open_camera_failure), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 设置imgs，item的点击监听
     */
    ItemOnClickListener imgsItemOnClickListener = new ItemOnClickListener() {
        @Override
        public void onBigPicClickListener(int postion) {
            //有相机选框情况
            if (mControl.mFirstItemOpenCamera) {
                if (postion == 0) {
                    PermissionHelper.proxy(PictureSelectorActivity.this, REQUEST_CAMERA_PERMISSION_CODE);
                    Logger.i("打开相机");
                } else {
                    Logger.i("点击预览");
                    if (mControl.mMultiSelect) {
                        mPreviewAdapter.notifyDataSetChanged();
                        mPreviewVP.setCurrentItem(mControl.mFirstItemOpenCamera ? postion - 1 : postion, false);
                        mPreviewVP.setVisibility(View.VISIBLE);
                    } else {
                        //TODO 单选
                        if (mControl.mClip) {
                            startClip(mImgList.get(postion).filePath);
                        } else {
                            ArrayList<String> paths = new ArrayList<>();
                            paths.add(mImgList.get(postion).filePath);
                            returnResult(paths);
                        }
                    }
                }
            } else {
                //无相机选框情况
                Logger.i("点击预览");
                //TODO 预览图片
                if (mControl.mMultiSelect) {
                    mPreviewAdapter.notifyDataSetChanged();
                    mPreviewVP.setCurrentItem(mControl.mFirstItemOpenCamera ? postion - 1 : postion, false);
                    mPreviewVP.setVisibility(View.VISIBLE);
                } else {
                    //TODO 单选
                    if (mControl.mClip) {
                        //裁剪
                        startClip(mImgList.get(postion).filePath);
                    } else {
                        ArrayList<String> paths = new ArrayList<>();
                        paths.add(mImgList.get(postion).filePath);
                        returnResult(paths);
                    }
                }
            }
        }

        @Override
        public void onSmallPicClickListener(int selectedNum) {
            Logger.i("复选框被点击了");
            if (mControl.mMultiSelect) {
                mBtnConfirm.setText(getString(R.string.confirm) + "(" + selectedNum + "/" + mControl.mMaxSize + ")");
            }
        }
    };

    /**
     * 开启图片裁剪
     */
    private void startClip(String path) {
        Intent intent = new Intent(this, ScreenshotActivity.class);
        intent.putExtra(ScreenshotActivity.CLIP_MODE, mControl.mClipMode);
        intent.putExtra(ScreenshotActivity.PIC_PATH, path);
        startActivityForResult(intent, CLIP_CODE);
    }

    /**
     * 返回结果
     */
    private void returnResult(ArrayList<String> paths) {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(PicSelector.SELECT_RESULT, paths);
        setResult(RESULT_OK, intent);
        SelectedImgList.mList.clear();
        finish();
    }
    /**
     * 返回结果
     */
    private void returnResult(String path) {
        ArrayList<String> paths = new ArrayList<>();
        paths.add(path);
        Intent intent = new Intent();
        intent.putStringArrayListExtra(PicSelector.SELECT_RESULT, paths);
        setResult(RESULT_OK, intent);
        SelectedImgList.mList.clear();
        finish();
    }

    /**
     * 预览图片view的点击回调（viewpager的item）
     */
    IPreviewOnClickListener mPreviewClickListener = new IPreviewOnClickListener() {
        @Override
        public void previewOnClickListener() {
            if (mPreviewVP.getVisibility() == View.VISIBLE) {
                mPreviewVP.setVisibility(View.GONE);
            }
        }

        @Override
        public void checkViewOnClickListener(int postion) {
            if (mControl.mFirstItemOpenCamera) {
                mImgAdapter.notifyItemChanged(postion + 1);
            } else {
                mImgAdapter.notifyItemChanged(postion);
            }
            if (mControl.mMultiSelect) {
                mBtnConfirm.setText(getString(R.string.confirm) + "(" + SelectedImgList.mList.size() + "/" + mControl.mMaxSize + ")");
            }
        }
    };

    /**
     * 拦截返回键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (mPreviewVP.getVisibility() == View.VISIBLE) {
            mPreviewVP.setVisibility(View.GONE);
            mImgAdapter.notifyDataSetChanged();
            return false;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mControl.mBackId) {
            Logger.i("点击了返回箭头");
            if (mPreviewVP.getVisibility() == View.VISIBLE) {
                mPreviewVP.setVisibility(View.GONE);
                mImgAdapter.notifyDataSetChanged();
            } else {
                finish();
            }
        } else if (id == mControl.mConfirmId) {
            Logger.i("点击了确认");
            //图库选择成功，返回数据给上一个页面
            ArrayList<String> picPath = new ArrayList<>();
            for (MediaFile file : SelectedImgList.mList) {
                picPath.add(file.filePath);
            }
            returnResult(picPath);
        } else if (id == mControl.mFolderSelect) {
            Logger.i("点击选择文件夹");
            WindowManager wm = getWindowManager();
            final int size = wm.getDefaultDisplay().getWidth() / 3 * 2;
            if (mFolderListAdapter == null) {
                mFolderListAdapter = new FolderAdapter(this, mFolderList, mControl);
            }
            if (mFolderPopupWindow == null) {
                createPopupFolderList(size, size);
            }
            if (mFolderPopupWindow.isShowing()) {
                mFolderPopupWindow.dismiss();
            } else {
                mFolderPopupWindow.show();
                if (mFolderPopupWindow.getListView() != null) {
                    mFolderPopupWindow.getListView().setDivider(new ColorDrawable(ContextCompat.getColor(PictureSelectorActivity.this, R.color.bottom_bg)));
                }
                setBackgroundAlpha(0.6f);
            }
        }
    }

    /**
     * 文件夹 选择
     *
     * @param width
     * @param height
     */
    private void createPopupFolderList(int width, int height) {
        mFolderPopupWindow = new ListPopupWindow(this);
        mFolderPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#aaaaaa")));
        mFolderPopupWindow.setAdapter(mFolderListAdapter);
        mFolderPopupWindow.setContentWidth(width);
        mFolderPopupWindow.setWidth(width);
        mFolderPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mFolderPopupWindow.setAnchorView(mBtnFolderSelect);
        mFolderPopupWindow.setModal(true);

        mFolderListAdapter.setOnFloderChangeListener(new IFolderChangeListener() {
            @Override
            public void onChange(int position, Folder folder) {
                mFolderPopupWindow.dismiss();
                mBtnFolderSelect.setText(folder.name);
                mTvTitle.setText(folder.name);
                if (position == 0) {
                    mImgList.clear();
                    if (mControl.mFirstItemOpenCamera) {
                        mImgList.add(new MediaFile());
                    }
                    mImgList.addAll(ReadPicHelper.getInstacne(PictureSelectorActivity.this).getmImgsList());
                    mImgAdapter.notifyDataSetChanged();
                } else {
                    mImgList.clear();
                    if (mControl.mFirstItemOpenCamera) {
                        mImgList.add(new MediaFile());
                    }
                    mImgList.addAll(folder.images);
                    mImgAdapter.notifyDataSetChanged();
                }
            }
        });
        mFolderPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
    }

    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CAMERA_CODE:
                if (resultCode == RESULT_OK) {
                    if (mTempFile != null) {
                        //TODO 裁剪
                        if (mControl.mClip) {
                            startClip(mTempFile.getAbsolutePath());
                            return;
                        }
                        //此处为拍照成功，回传上一个页面
                        ArrayList<String> result = new ArrayList();
                        result.add(mTempFile.getAbsolutePath());
                        returnResult(result);
                    }
                } else {
                    //拍照失败，可能使用户点击了返回键
                    Logger.e("我是相机返回--------");
                    finish();
                }
                break;
            case CLIP_CODE:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        String path = data.getStringExtra(PicSelector.SELECT_RESULT);
                        returnResult(path);
                        Logger.i("裁剪返回数据--->path--->" + path);
                    }
                } else {
                    if(mControl.mOpenCamera){
                        finish();
                    }
                }
                break;
        }


    }


}
