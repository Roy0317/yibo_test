package com.yibo.yiboapp.utils;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import androidx.fragment.app.Fragment;
import androidx.core.content.FileProvider;
import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.yibo.yiboapp.BuildConfig;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;

/**
 * 上传头像工具类
 *
 * @author pythoner
 */
public class FaceUtils {

    private String[] items = new String[]{"相册", "拍照"};
    /* 头像名称 */
    private static final String IMAGE_FILE_NAME = "faces.jpg";
    /* 请求码 */
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;
    private Fragment context;
    Map<String, Object> params;
    private Uri imageCropUri;

    private final int ST_UPLOAD = 0;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ST_UPLOAD:
                    if (onFaceUploadListener == null) {
                        return;
                    }
                    DataWrap datas = (DataWrap) msg.obj;
                    if (datas == null) {
                        return;
                    }
                    if (datas.success) {
                        try {
                            JSONObject response = new JSONObject(datas.json);
                            boolean isSuccess = response.getBoolean("success");
                            if (isSuccess) {
                                String originUrl = "";
                                if (!response.isNull("content")) {
                                    originUrl = response.getString("content");
                                }
                                onFaceUploadListener.onFaceUploadSuccess(originUrl);
                            } else {
                                String error = response.getString("msg");
                                onFaceUploadListener.onFaceUploadFailed(error);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            onFaceUploadListener.onFaceUploadFailed(e.getMessage());
                        }
                    } else {
                        onFaceUploadListener.onFaceUploadFailed("upload header image error");
                    }
                    break;
                default:
                    break;
            }

        }

    };

    public FaceUtils(Fragment context, Map<String, Object> params) {
        this.context = context;
        this.params = params;
    }

    public void showSettingFaceDialog() {
        new AlertDialog.Builder(context.getActivity())
                .setTitle("图片来源")
                .setCancelable(true)
                .setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:// Local Image
                                Intent intent = new Intent(
                                        Intent.ACTION_GET_CONTENT);
                                intent.addCategory(Intent.CATEGORY_OPENABLE);
                                intent.setType("image/*");
                                context.startActivityForResult(intent, IMAGE_REQUEST_CODE);
                                break;
                            case 1:// Take Picture
                                Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                // 判断存储卡是否可以用，可用进行存储
                                if (hasSdcard()) {
                                    String faceImagePath = Utils.createFilepath(Utils.DIR_CATEGORY.IMAGE, IMAGE_FILE_NAME);
                                    File picFile = new File(faceImagePath);
                                    Uri uri;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        uri = FileProvider.getUriForFile(context.getActivity(), BuildConfig.APPLICATION_ID + ".fileprovider",
                                                picFile);    //第二个参数是manifest中定义的`authorities`
                                    } else {
                                        uri = Uri.fromFile(picFile);
                                    }
                                    intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                }
                                context.startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 结果码不等于取消时候
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case FaceUtils.IMAGE_REQUEST_CODE:
                    uri = data.getData();
                    getImageToView();
                    break;
                case FaceUtils.CAMERA_REQUEST_CODE:
                    File tempFile = new File(Utils.createFilepath(Utils.DIR_CATEGORY.IMAGE, IMAGE_FILE_NAME));
                    if (tempFile.exists()) {
                        uri = Uri.fromFile(tempFile);
                        getImageToView();
                    }
                    break;
                case FaceUtils.RESULT_REQUEST_CODE:
                    getImageToView();
                    break;
            }
        }
    }

    private boolean hasSdcard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    Uri uri;

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    private void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
            return;
        }

        this.uri = uri;

        Intent intent = new Intent("com.android.camera.action.CROP");
//        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String url = getPath(context.getActivity(), uri);
            File picFile = new File(url);
            //第二个参数是manifest中定义的`authorities`
            uri = FileProvider.getUriForFile(context.getActivity(), BuildConfig.APPLICATION_ID + ".fileprovider", picFile);
            intent.setType("image/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);    //这一步很重要。给目标应用一个临时的授权。
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);    //这一步很重要。给目标应用一个临时的授权。
            intent.setDataAndType(uri, "image/*");

            List<ResolveInfo> resInfoList = context.getActivity().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                context.getActivity().grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }

            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        } else {
            String url = getPath(context.getActivity(), uri);
            File picFile = new File(url);
            intent.setDataAndType(Uri.fromFile(picFile), "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picFile));
        }

        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);


        context.startActivityForResult(intent, RESULT_REQUEST_CODE);
    }


    private void getImageToView() {
        try {
            //intent现在会返回空，所以不能取用intent中的数值，但是可以取我们已经存好的临时文件
            Bitmap bitmap = BitmapFactory.decodeStream(context.getActivity().getContentResolver().openInputStream(uri));
            //把bitmap写入文件
            saveBitmap(bitmap);
            //然后上传图片到服务端保存;
            File file = new File(Utils.createFilepath(Utils.DIR_CATEGORY.IMAGE, IMAGE_FILE_NAME));
            String filePath = file.getAbsolutePath();
            uploadFace(filePath, this.params, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * 保存裁剪之后的图片数据
     */
    private void setData(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            if (photo != null) {
                //这里是保存图片到本地
                saveBitmap(photo);
                //然后上传图片到服务端保存;
                File file = new File(Utils.createFilepath(Utils.DIR_CATEGORY.IMAGE, IMAGE_FILE_NAME));
                String filePath = file.getAbsolutePath();
                uploadFace(filePath, this.params, true);
            }

        }
    }

    private OnFaceUploadListener onFaceUploadListener;

    public void setOnFaceUploadListener(
            OnFaceUploadListener onFaceUploadListener) {
        this.onFaceUploadListener = onFaceUploadListener;
    }

    public interface OnFaceUploadListener {
        void onFaceUploadSuccess(String imgUrl);

        void onFaceUploadFailed(String error);
    }

    private void saveBitmap(Bitmap bitmap) {
        File f = new File(Utils.createFilepath(Utils.DIR_CATEGORY.IMAGE, IMAGE_FILE_NAME));
        try {
            f.createNewFile();
            FileOutputStream fos = null;
            fos = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uploadFace(final String filePath, final Map<String, Object> params, final boolean delete) {
        if (!context.getActivity().isFinishing()) {
            ToastUtils.showShort("正在上传头像...");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                UsualMethod.uploadLocalFile(context.getActivity().getApplicationContext(), params, filePath, Urls.HEADER_URL,
                        new FileUploadCallback() {
                            @Override
                            public void uploadResult(boolean success, String json) {
                                if (success && delete) {
                                    new File(filePath).delete();
                                }
                                DataWrap obj = new DataWrap(success, json);
                                sendMessage(ST_UPLOAD, obj);
                            }
                        });
            }
        }).start();
    }

    private void sendMessage(int st, Object obj) {
        Message message = new Message();
        message.what = st;
        message.obj = obj;
        handler.sendMessage(message);
    }

    class DataWrap {
        String json;
        boolean success;

        DataWrap(boolean success, String json) {
            this.success = success;
            this.json = json;
        }
    }

    @SuppressLint("NewApi")
    private static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
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
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

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
    private static String getDataColumn(Context context, Uri uri,
                                        String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }

}
