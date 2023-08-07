package com.example.anuo.immodule.utils;

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
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import crazy_wrapper.Crazy.Utils.Utils;

public class PhotoUtil {

    /* 头像名称 */
    private static final String IMAGE_FILE_NAME = "chat_avatar.jpg";
    private static final String CROPPED_IMAGE_FILE_NAME = "chat_cropped_avatar.jpg";
    /* 请求码 */
    public static final int IMAGE_REQUEST_CODE = 0;
    public static final int CAMERA_REQUEST_CODE = 1;
    public static final int RESULT_REQUEST_CODE = 2;

    public void pickPhoto(final Activity activity){
        String[] options = new String[]{"相册", "拍照"};
        new AlertDialog.Builder(activity)
                .setTitle("图片来源")
                .setCancelable(true)
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:// Local Image
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.addCategory(Intent.CATEGORY_OPENABLE);
                                intent.setType("image/*");
                                activity.startActivityForResult(intent, IMAGE_REQUEST_CODE);
                                break;
                            case 1:// Take Picture
                                Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                // 判断存储卡是否可以用，可用进行存储
                                if (hasSdcard()) {
                                    String faceImagePath = getPhotoFilePath();
                                    File picFile = new File(faceImagePath);
                                    Uri uri = Uri.fromFile(picFile);
                                    intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                }
                                activity.startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); }
                }).show();
    }

    private boolean hasSdcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public Uri getPhotoUriOnActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case IMAGE_REQUEST_CODE:
                return data.getData();
            case CAMERA_REQUEST_CODE:
                File tempFile = new File(getPhotoFilePath());
                if (tempFile.exists()) {
                    return Uri.fromFile(tempFile);
                }
                break;
        }

        return null;
    }

    /**
     * 裁剪图片方法实现
     */
    public void startPhotoZoom(Activity activity, Uri uri) {
        if (uri == null) {
            Utils.LOG("PhotoUtil", "The uri does not exist.");
            return;
        }

        Intent intent = new Intent("com.android.camera.action.CROP");
        String url = getPath(activity, uri);
        File picFile = new File(url);
        Uri picUri = getSafeUri(activity, picFile);
        intent.setDataAndType(picUri, "image/*");

        File croppedFile = new File(getCroppedPhotoFilePath());
        Uri croppedUri = getSafeUri(activity, croppedFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, croppedUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);


        //将存储图片的uri读写权限授权给剪裁工具应用
        List<ResolveInfo> resInfoList = activity.getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            activity.grantUriPermission(packageName, croppedUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        activity.startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    private Uri getSafeUri(Activity activity, File file){
        Uri fileUri;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            fileUri = FileProvider.getUriForFile(activity,
                    activity.getPackageName() + ".fileprovider", file);
        }else {
            fileUri = Uri.fromFile(file);
        }

        return fileUri;
    }

    public Bitmap getCroppedPhotoBitmap(Activity activity) {
        try {
            File f = new File(getCroppedPhotoFilePath());
            Uri photoUri = Uri.fromFile(f);
            Bitmap bitmap = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(photoUri));
//            saveBitmap(bitmap);
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getCroppedImageType(){
        String filePath = getCroppedPhotoFilePath();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        return options.outMimeType;
    }

    public String getPhotoFilePath(){
        return Utils.createFilepath(Utils.DIR_CATEGORY.IMAGE, IMAGE_FILE_NAME);
    }

    public String getCroppedPhotoFilePath(){
        return Utils.createFilepath(Utils.DIR_CATEGORY.IMAGE, CROPPED_IMAGE_FILE_NAME);
    }

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
            } else if (isDownloadsDocument(uri)) {// DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {// MediaProvider
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
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {// MediaStore (and general)
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {// File
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
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private void saveBitmap(Bitmap bitmap) {
        File f = new File(getPhotoFilePath());
        try {
            f.createNewFile();
            FileOutputStream fos = null;
            fos = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
