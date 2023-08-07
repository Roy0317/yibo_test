package com.yibo.yiboapp.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.utils.Utils;

public class DiceSurface extends SurfaceView implements Callback {
    private String tag = "DiceSurface";
    private Context context;
    private PlayThread thread;
    private SurfaceHolder surfaceHolder;
    private Handler handler;
    private MediaPlayer mp;
    private boolean choujiang = false;
    int[] random;
    int[] result;

    public DiceSurface(Context context) {
        super(context);
        this.context = context;
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }


    public DiceSurface(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }


    public DiceSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);

    }


    public void setHandler(Handler handler) {
        this.handler = handler;

    }

    public int[] getResult() {
        return random;
    }


    public void shiWan() {
        result = new int[6];
        result[0] = (int) Math.floor(Math.random() * 6);
        result[1] = (int) Math.floor(Math.random() * 6);
        result[2] = (int) Math.floor(Math.random() * 6);
        result[3] = (int) Math.floor(Math.random() * 6);
        result[4] = (int) Math.floor(Math.random() * 6);
        result[5] = (int) Math.floor(Math.random() * 6);
        start();

    }

    public void choujiang(char[] a) {
        result = new int[6];
        result[0] = Integer.parseInt(a[0] + "") - 1;
        result[1] = Integer.parseInt(a[1] + "") - 1;
        result[2] = Integer.parseInt(a[2] + "") - 1;
        result[3] = Integer.parseInt(a[3] + "") - 1;
        result[4] = Integer.parseInt(a[4] + "") - 1;
        result[5] = Integer.parseInt(a[5] + "") - 1;
        start();

    }

    private void start() {
        if (thread != null && thread.isAlive()) {
            Log.e(tag, "isAlive()");
            return;
        }
        thread = new PlayThread(surfaceHolder);
        thread.start();
        mp = new MediaPlayer();
        mp.setOnErrorListener(new OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mp.release();
                Log.e(tag, "mp.release()");
                return false;
            }
        });
        mp.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                Log.e(tag, "mp.release()");
            }
        });
        mp.setOnPreparedListener(new OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                Log.e(tag, "mp.start()");
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Canvas canvas = holder.lockCanvas();  // 先锁定当前surfaceView的画布
        canvas.drawBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.mid_autumnfestival_bowl), Utils.dip2px(context, 300), Utils.dip2px(context, 300), true), 0, Utils.dip2px(context, 0), new Paint());
        holder.unlockCanvasAndPost(canvas); //
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        if (thread != null) {
            thread.run = false;
        }
        if (mp != null) {
            mp.release();
        }
    }


    public void setChoujiang(boolean choujiang) {
        this.choujiang = choujiang;
    }

    class PlayThread extends Thread {
        SurfaceHolder surfaceHolder;
        //run()函数中控制循环的参数。
        boolean run = true;
        boolean isFinish = false;
        int imgId = R.drawable.d1;
        int degree = 0;
        int maxWidth = 150;
        int width = 200;
        int height = 200;
        int x = 60;
        int y = 50;
        int maxY = 100;
        int sleepTime = 20;
        int change = 1;
        int countdown = 30;


        public PlayThread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
            random = new int[6];
            random[0] = (int) Math.floor(Math.random() * 6);
            random[1] = (int) Math.floor(Math.random() * 6);
            random[2] = (int) Math.floor(Math.random() * 6);
            random[3] = (int) Math.floor(Math.random() * 6);
            random[4] = (int) Math.floor(Math.random() * 6);
            random[5] = (int) Math.floor(Math.random() * 6);
        }

        @Override
        public void run() {
            while (run) {
                Canvas canvas = null;
                try {
                    synchronized (surfaceHolder) {

                        canvas = surfaceHolder.lockCanvas();
                        Resources res = context.getResources();
                        if (degree >= 360) {
                            degree = 0;
                            random[0] = result[0];
                            random[1] = result[1];
                            random[2] = result[2];
                            random[3] = result[3];
                            random[4] = result[4];
                            random[5] = result[5];
                        } else {

                            degree += 50;
                        }
                        Bitmap bmp1 = BitmapFactory.decodeResource(res, imgId + random[0]);
                        Bitmap bmp2 = BitmapFactory.decodeResource(res, imgId + random[1]);
                        Bitmap bmp3 = BitmapFactory.decodeResource(res, imgId + random[2]);
                        Bitmap bmp4 = BitmapFactory.decodeResource(res, imgId + random[3]);
                        Bitmap bmp5 = BitmapFactory.decodeResource(res, imgId + random[4]);
                        Bitmap bmp6 = BitmapFactory.decodeResource(res, imgId + random[5]);
                        canvas.drawColor(Color.TRANSPARENT);
                        Matrix matrix = new Matrix();
                        matrix.reset();
                        matrix.setRotate(degree);
                        Bitmap tempbmp1 = Bitmap.createBitmap(Bitmap.createScaledBitmap(bmp1, width, height, true), 0, 0, width, height, matrix, true);
                        Bitmap tempbmp2 = Bitmap.createBitmap(Bitmap.createScaledBitmap(bmp2, width, height, true), 0, 0, width, height, matrix, true);
                        Bitmap tempbmp3 = Bitmap.createBitmap(Bitmap.createScaledBitmap(bmp3, width, height, true), 0, 0, width, height, matrix, true);
                        Bitmap tempbmp4 = Bitmap.createBitmap(Bitmap.createScaledBitmap(bmp4, width, height, true), 0, 0, width, height, matrix, true);
                        Bitmap tempbmp5 = Bitmap.createBitmap(Bitmap.createScaledBitmap(bmp5, width, height, true), 0, 0, width, height, matrix, true);
                        Bitmap tempbmp6 = Bitmap.createBitmap(Bitmap.createScaledBitmap(bmp6, width, height, true), 0, 0, width, height, matrix, true);
                        canvas.drawBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.mid_autumnfestival_bowl), Utils.dip2px(context, 300), Utils.dip2px(context, 300), true), 0, Utils.dip2px(context, 0), new Paint());
                        canvas.drawBitmap(tempbmp1, x + 150, y + 150, new Paint());
                        canvas.drawBitmap(tempbmp2, x + 250, y + 150, new Paint());
                        canvas.drawBitmap(tempbmp3, x + 350, y + 150, new Paint());
                        canvas.drawBitmap(tempbmp4, x + 150, y + 250, new Paint());
                        canvas.drawBitmap(tempbmp5, x + 250, y + 250, new Paint());
                        canvas.drawBitmap(tempbmp6, x + 350, y + 250, new Paint());
                        if (isFinish) {
                            if (countdown == 0) {
                                if (degree == 0) {
                                    handler.sendEmptyMessage(1);

                                    return;
                                }
                            } else
                                countdown--;

                        }
                        if (y < maxY) {
                            y += 30 + (change) * (change) / 4;
                            width = maxWidth - y / 40;
                            height = maxWidth - y / 40;
                        } else {
                            if (isFinish == false) {
                                isFinish = true;
                                mp.setDataSource(context, Uri.parse("android.resource://com.example.dicedemo/raw/" + R.raw.yaoyiayo));
                                mp.prepareAsync();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    run = false;
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
                try {
                    Thread.sleep(sleepTime);
                    change++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    public class InitThead extends Thread {
//        SurfaceHolder surfaceHolder;
//        Canvas canvas;
//        boolean run = true;
//
//        public InitThead(SurfaceHolder surfaceHolder) {
//            this.surfaceHolder = surfaceHolder;
//
//        }
//
//        @Override
//        public void run() {
//            super.run();
//            while (run) {
//                synchronized (surfaceHolder) {
//                    canvas = surfaceHolder.lockCanvas();
//                    canvas.drawBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.mid_autumnfestival_bowl), Utils.dip2px(context, 300), Utils.dip2px(context, 300), true), 0, Utils.dip2px(context, 80), new Paint());
//
//                }
//                run = false;
//            }
//        }
//    }
//
//
//    public void initBow() {
//        new InitThead(getHolder()).start();
//    }
}
