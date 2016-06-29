package feifei.feeling.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import feifei.feeling.R;
import library.util.BitmapUtil;
import library.util.FileUtils;
import library.util.L;
import library.util.S;
import library.util.TS;
import library.widget.ActionSheet;
import me.crosswall.photo.pick.PickConfig;

public class ImageChooser {

    private List<String> mItemList;

    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_VIDEO = 2;

    /* 请求码 */
    private static final int IMAGE_REQUEST_CODE  = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final int ZOOM_REQUEST_CODE   = 3;
    private static final int VIDEO_REQUEST_CODE  = 4;

    private Object  mActivityOrFragment;
    private boolean isChoose;

    private boolean isPhoto;
    private boolean isZoom;
    private boolean isVideo;
    private int     count;

    public void setOnImageChosenListener(OnImageChosenListener onImageChosenListener) {
        this.mOnImageChosenListener = onImageChosenListener;
    }

    private OnImageChosenListener mOnImageChosenListener;

    public void setMax(int count) {
        this.count = count;
    }

    public interface OnImageChosenListener {

        void onImageChosen(Bitmap bitmap, int type);
    }

    public void setOnOperationClickListener(OnOperationClickListener onOperationClickListener) {
        this.mOnOperationClickListener = onOperationClickListener;
    }

    private OnOperationClickListener mOnOperationClickListener;

    public interface OnOperationClickListener {

        boolean onOperationClick(int type);
    }

    public ImageChooser(Object activityOrFragment, boolean isChoose, boolean isPhoto, boolean isZoom, boolean isVideo, int count) {
        this.mActivityOrFragment = activityOrFragment;
        this.isChoose = isChoose;
        this.isPhoto = isPhoto;
        this.isZoom = isZoom;
        this.isVideo = isVideo;
        this.count = count;
        mItemList = new ArrayList<>();
    }

//    public ImageChooser(Object activityOrFragment, boolean isChoose, boolean isPhoto, boolean isZoom) {
//        this(activityOrFragment, isChoose, isPhoto, isZoom, false);
//    }

    public ImageChooser(Object activityOrFragment, boolean isChoose, boolean isPhoto, boolean isZoom, int count) {
        this(activityOrFragment, isChoose, isPhoto, isZoom, false, count);
    }

    private Context getContext() {
        if (mActivityOrFragment instanceof Activity) {
            return (Context) mActivityOrFragment;
        } else if (mActivityOrFragment instanceof Fragment) {
            return ((Fragment) mActivityOrFragment).getActivity();
        } else {
            throw new RuntimeException("not context:" + mActivityOrFragment.getClass().getCanonicalName());
        }
    }

    public void takeImage(String title) {
        mItemList.clear();
        if (isChoose) {
            mItemList.add(getContext().getResources().getString(R.string.choose_sd_Pic));
        }
        if (isPhoto) {
            mItemList.add(getContext().getResources().getString(R.string.choose_camera));
        }
        if (isVideo) {
            mItemList.add(getContext().getResources().getString(R.string.choose_video));
        }
//        showDialog(title, mItemList.toArray(new String[mItemList.size()]));
        showDialog(title, mItemList);
    }

    private void showDialog(String title, List<String> items) {
//这里去掉了样式的个性化
//        getContext().setTheme(R.style.ActionSheetStyleIOS7);
        if (S.readBoolear(getContext(), "3", false)) {
            ActionSheet menuView = new ActionSheet(getContext());
            menuView.setCancelButtonTitle("取消");// before add items
            menuView.addItems(items);
            menuView.setCancelableOnTouchMenuOutside(true);
            menuView.setItemClickListener(itemPosition -> {
                switch (itemPosition) {
                    case 0:
                        if (mOnOperationClickListener != null && !mOnOperationClickListener.onOperationClick(TYPE_IMAGE)) {
                            return;
                        }
                        choosePhoto();// 选择本地图片
                        break;
                    case 1:
                        if (mOnOperationClickListener != null && !mOnOperationClickListener.onOperationClick(TYPE_IMAGE)) {
                            return;
                        }
                        takePhoto();// 拍照
                        break;
                    case 2:
                        if (mOnOperationClickListener != null && !mOnOperationClickListener.onOperationClick(TYPE_VIDEO)) {
                            return;
                        }
//                        takeVideo();
                        break;
                }
            });
            menuView.showMenu();
        } else {
//        D.dl(getContext(), title, items, new D.OnItemListener() {
//            @Override
//            public void onItemClick(int pos) {
//                switch (pos) {
//                    case 0:
//                        if (mOnOperationClickListener != null && !mOnOperationClickListener.onOperationClick(TYPE_IMAGE)) {
//                            return;
//                        }
//                        choosePhoto();// 选择本地图片
//                        break;
//                    case 1:
//                        if (mOnOperationClickListener != null && !mOnOperationClickListener.onOperationClick(TYPE_IMAGE)) {
//                            return;
//                        }
//                        takePhoto();// 拍照
//                        break;
//                    case 2:
//                        if (mOnOperationClickListener != null && !mOnOperationClickListener.onOperationClick(TYPE_VIDEO)) {
//                            return;
//                        }
//                        takeVideo();
//                        break;
//                }
//            }
//        }, true);
            new AlertDialog.Builder(getContext())
                    .setTitle(title)
                    .setItems(items.toArray(new String[items.size()]), (dialog, which) -> {
                        if (mItemList.get(which).equals(getContext().getResources().getString(R.string.choose_sd_Pic))) {
                            if (mOnOperationClickListener != null && !mOnOperationClickListener.onOperationClick(TYPE_IMAGE)) {
                                return;
                            }
                            choosePhoto();
                        } else if (mItemList.get(which).equals(getContext().getResources().getString(R.string.choose_camera))) {
                            if (mOnOperationClickListener != null && !mOnOperationClickListener.onOperationClick(TYPE_IMAGE)) {
                                return;
                            }
                            takePhoto();
                        } else if (mItemList.get(which).equals(getContext().getResources().getString(R.string.choose_video))) {
                            if (mOnOperationClickListener != null && !mOnOperationClickListener.onOperationClick(TYPE_VIDEO)) {
                                return;
                            }
//                            takeVideo();
                        }
                    })
                    .setNegativeButton(R.string.cancel, (dialog, which) -> {
                        dialog.dismiss();
                    }).show();
        }
    }

    //图片选择器
    private void choosePhoto() {
        if (S.readBoolear(getContext(), "6", false)) {
            new PickConfig.Builder((Activity) mActivityOrFragment)
                    .pickMode(PickConfig.MODE_MULTIP_PICK)
                    .maxPickSize(count)
                    .spanCount(3)
                    //.showGif(true)
                    .checkImage(false) //default false
                    .useCursorLoader(false) //default true
                    .toolbarColor(R.color.colorPrimary)
                    .build();
        } else {
            Intent intentFromGallery = new Intent();
            intentFromGallery.setType("image/*"); // 设置文件类型
            intentFromGallery
                    .setAction(Intent.ACTION_GET_CONTENT);
            intentFromGallery
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            if (mActivityOrFragment instanceof Activity) {
                ((Activity) mActivityOrFragment).startActivityForResult(intentFromGallery,
                        IMAGE_REQUEST_CODE);
            } else if (mActivityOrFragment instanceof Fragment) {
                ((Fragment) mActivityOrFragment).startActivityForResult(intentFromGallery,
                        IMAGE_REQUEST_CODE);
            }
        }
    }

    private void takePhoto() {
        Intent intentFromCapture = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断存储卡是否可以用，可用进行存储
        if (FileUtils.hasSdcard()) {
            File file = new File(getContext().getExternalCacheDir().getAbsolutePath()
                    + File.separator + "temp.jpg");
            if (file.exists())
                file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            intentFromCapture.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(file));
        }
        intentFromCapture
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (mActivityOrFragment instanceof Activity) {
            ((Activity) mActivityOrFragment).startActivityForResult(intentFromCapture,
                    CAMERA_REQUEST_CODE);
        } else if (mActivityOrFragment instanceof Fragment) {
            ((Fragment) mActivityOrFragment).startActivityForResult(intentFromCapture,
                    CAMERA_REQUEST_CODE);
        }
    }

    /**
     * 功能描述: 摄像
     */
//    private void takeVideo() {
//        Intent intent = new Intent();
//        intent.setClass(getContext(), VideoT.class);
//
//        if (mActivityOrFragment instanceof Activity) {
//            ((Activity) mActivityOrFragment).startActivityForResult(intent,
//                    VIDEO_REQUEST_CODE);
//        } else if (mActivityOrFragment instanceof Fragment) {
//            ((Fragment) mActivityOrFragment).startActivityForResult(intent,
//                    VIDEO_REQUEST_CODE);
//        }
//    }

    public static final int MAX_WIDTH  = 400;
    public static final int MAX_HEIGHT = 400;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    if (data != null) {
                        //这里是裁剪
                        if (isZoom)
                            startPhotoZoom(getPath(getContext(), data.getData()));
                        else {
                            Uri originalUri = data.getData(); // 获得图片的uri
                            String path = getPath(getContext(), originalUri);
                            L.l(path);
                            Bitmap bitmap = BitmapUtil.decodeSampledBitmapFromFile(path, MAX_WIDTH, MAX_HEIGHT);
                            if (mOnImageChosenListener != null)
                                mOnImageChosenListener.onImageChosen(bitmap, TYPE_IMAGE);
                        }
                    }
                    break;
                case PickConfig.PICK_REQUEST_CODE:
                    if (data != null) {
                        ArrayList<String> pick = data.getStringArrayListExtra(PickConfig.EXTRA_STRING_ARRAYLIST);
                        if (isZoom) {
                            startPhotoZoom(pick.get(0));
                        } else {
                            for (int i = 0; i < pick.size(); i++) {
                                Bitmap bitmap = BitmapUtil.decodeSampledBitmapFromFile(pick.get(i), MAX_WIDTH, MAX_HEIGHT);
                                if (mOnImageChosenListener != null)
                                    mOnImageChosenListener.onImageChosen(bitmap, TYPE_IMAGE);
                            }
                        }
                    }
                    break;


                case CAMERA_REQUEST_CODE:
                    if (FileUtils.hasSdcard()) {
                        File file = new File(getContext().getExternalCacheDir().getAbsolutePath() + File.separator + "temp.jpg");
                        BitmapFactory.Options option = new BitmapFactory.Options();
                        option.inSampleSize = 2;
                        Bitmap bitmap = BitmapUtil.decodeSampledBitmapFromFile(file.getAbsolutePath(), MAX_WIDTH, MAX_HEIGHT);
                        Matrix matrix = new Matrix();
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();
                        matrix.preRotate(readPictureDegree(file.toString()));
                        Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                                matrix, true);// 从新生成图片
                        FileUtils.writeBitmap2SDcard(bitmap2,
                                file.toString());
                        if (bitmap != bitmap2 && !bitmap.isRecycled())
                            bitmap.recycle();
                        if (isZoom) {
                            startPhotoZoom(getPath(getContext(), Uri.fromFile(file)));
                        } else {
                            if (mOnImageChosenListener != null) {
                                mOnImageChosenListener.onImageChosen(bitmap2, TYPE_IMAGE);
                            }
                        }
                    } else {
                        TS.s(getContext(), R.string.save_camera_fail, 2);
                    }
                    break;
                case ZOOM_REQUEST_CODE:
                    if (data != null) {
                        getResult(data);
                    }
                    break;
//                case VIDEO_REQUEST_CODE:
//                    // 捕获返回的拍照路径  (注意： 这里还不能简单的用qid，来命名文件，因为你得先上传图片，然后采用qid，你只能把拍好的视屏改名)
//                    String filePath = FileUtils.getChatterVideoDir(getContext());
//                    File file = new File(filePath);
//                    if (!TextUtils.isEmpty(filePath) && file.exists()) {
//                        int size = getContext().getResources().getDimensionPixelSize(R.dimen.icon_size_xlarge);
//                        Bitmap videoBitmap = VideoImageThum.getVideoThumbnail(FileUtils.getChatterVideoDir(getContext()), size, size,
//                                MediaStore.Images.Thumbnails.MICRO_KIND);
//                        if (mOnImageChosenListener != null) {
//                            mOnImageChosenListener.onImageChosen(videoBitmap, TYPE_VIDEO);
//                        }
//                    }
//                    break;
            }
        }
    }

    private void getResult(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            final Bitmap bitmap = extras.getParcelable("data");
            if (mOnImageChosenListener != null)
                mOnImageChosenListener.onImageChosen(bitmap, TYPE_IMAGE);
        }
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 裁剪图片方法实现
     */
    public void startPhotoZoom(String filepath) {

        Uri newUri = Uri.fromFile(new File(filepath));
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(newUri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 3);
        intent.putExtra("aspectY", 3);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        try {
            if (mActivityOrFragment instanceof Activity) {
                ((Activity) mActivityOrFragment).startActivityForResult(intent,
                        ZOOM_REQUEST_CODE);
            } else if (mActivityOrFragment instanceof Fragment) {
                ((Fragment) mActivityOrFragment).startActivityForResult(intent,
                        ZOOM_REQUEST_CODE);
            }

        } catch (ActivityNotFoundException anfe) {
            if (mActivityOrFragment instanceof Context) {
                TS.s((Context) mActivityOrFragment, "您的设备没有裁剪照片的软件", 2);
            }
        }

    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            // isExternalStorageDocument
            if ("com.android.externalstorage.documents".equals(uri.getAuthority())) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            // isDownloadsDocument
            else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            // isMediaDocument
            else if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

}
