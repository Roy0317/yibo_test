package com.yibo.yiboapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.yibo.yiboapp.R;

import java.util.Hashtable;


/*
* 显示微信收款码
* */
public class QrCodeImgActivity extends BaseActivity {

    private ImageView qrImg;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_qrcode_img);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText("二维码");
        qrImg = findViewById(R.id.act_qrcode_img);
        final String qrCode = this.getIntent().getStringExtra("qrCode");
        createQRcodeImage(qrCode);

        qrImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mBitmap != null) {
                    String res = MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap,
                            "qrcode", "save_qrcode");
                    Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(res));
                    sendBroadcast(scannerIntent);
                    showToast("二维码图片已保存,请使用微信进行扫码付款");
                } else {
                    showToast("保存失败，请重试");
                }
                return true;
            }
        });

    }

    private  Bitmap mBitmap = null;

    public void createQRcodeImage(String url)
    {

       int w = 300;
       int h = 300;
        try
        {
            //判断URL合法性
            if (url == null || "".equals(url) || url.length() < 1)
            {
                return;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, w, h, hints);
            int[] pixels = new int[w * h];
            //下面这里按照二维码的算法，逐个生成二维码的图片，
            //两个for循环是图片横列扫描的结果
            for (int y = 0; y < h; y++)
            {
                for (int x = 0; x < w; x++)
                {
                    if (bitMatrix.get(x, y))
                    {
                        pixels[y * w + x] = 0xff000000;
                    }
                    else
                    {
                        pixels[y * w + x] = 0xffffffff;
                    }
                }
            }
            //生成二维码图片的格式，使用ARGB_8888
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mBitmap.setPixels(pixels, 0, w, 0, 0, w, h);
            //显示到我们的ImageView上面
            qrImg.setImageBitmap(mBitmap);
        }
        catch (WriterException e)
        {
            e.printStackTrace();
        }
    }



}
