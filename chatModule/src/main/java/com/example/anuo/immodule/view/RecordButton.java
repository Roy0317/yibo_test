package com.example.anuo.immodule.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.appcompat.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.anuo.immodule.R;
import com.example.anuo.immodule.utils.ChatSpUtils;
import com.example.anuo.immodule.utils.LogUtils;
import com.example.anuo.immodule.utils.ToastUtils;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ┌───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┐
 * │Esc│ │ F1│ F2│ F3│ F4│ │ F5│ F6│ F7│ F8│ │ F9│F10│F11│F12│ │P/S│S L│P/B│ ┌┐    ┌┐    ┌┐
 * └───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┘ └┘    └┘    └┘
 * ┌──┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───────┐┌───┬───┬───┐┌───┬───┬───┬───┐
 * │~`│! 1│@ 2│# 3│$ 4│% 5│^ 6│& 7│* 8│( 9│) 0│_ -│+ =│ BacSp ││Ins│Hom│PUp││N L│ / │ * │ - │
 * ├──┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─────┤├───┼───┼───┤├───┼───┼───┼───┤
 * │Tab │ Q │ W │ E │ R │ T │ Y │ U │ I │ O │ P │{ [│} ]│ | \ ││Del│End│PDn││ 7 │ 8 │ 9 │   │
 * ├────┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴─────┤└───┴───┴───┘├───┼───┼───┤ + │
 * │Caps │ A │ S │ D │ F │ G │ H │ J │ K │ L │: ;│" '│ Enter  │             │ 4 │ 5 │ 6 │   │
 * ├─────┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴────────┤    ┌───┐    ├───┼───┼───┼───┤
 * │Shift  │ Z │ X │ C │ V │ B │ N │ M │< ,│> .│? /│  Shift   │    │ ↑ │    │ 1 │ 2 │ 3 │   │
 * ├────┬──┴─┬─┴──┬┴───┴───┴───┴───┴───┴──┬┴───┼───┴┬────┬────┤┌───┼───┼───┐├───┴───┼───┤ E││
 * │Ctrl│Ray │Alt │         Space         │ Alt│code│fuck│Ctrl││ ← │ ↓ │ → ││   0   │ . │←─┘│
 * └────┴────┴────┴───────────────────────┴────┴────┴────┴────┘└───┴───┴───┘└───────┴───┴───┘
 * <p>
 * Author:anuo
 * Date  :2019/6/6
 * Desc  :com.example.anuo.immodule.view
 */
public class RecordButton extends AppCompatButton {
    public RecordButton(Context context) {
        super(context);
        init();
    }

    public RecordButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private String mFile;


    private OnFinishedRecordListener finishedListener;
    private OnStartRecordListener startRecordListener;
    /**
     * 最短录音时间
     **/
    private int MIN_INTERVAL_TIME = 1000;
    /**
     * 最长录音时间
     **/
    private int MAX_INTERVAL_TIME = 1000 * 15;


    private static View view;

    private TextView mStateTV;

    private ImageView mStateIV;

    private MediaRecorder mRecorder;
    private ObtainDecibelThread mThread;
    private Handler volumeHandler;
    private ExecutorService mExecutorService;

    public void release() {
        //activity销毁时，停止后台任务，避免内存泄露
        if (mExecutorService != null) {
            mExecutorService.shutdownNow();
        }
    }


    private float y;


    public void setOnFinishedRecordListener(OnFinishedRecordListener listener) {
        finishedListener = listener;
    }

    public void setStartRecordListener(OnStartRecordListener startRecordListener) {
        this.startRecordListener = startRecordListener;
    }

    private static long startTime;
    private Dialog recordDialog;
    private static int[] res = {R.drawable.ic_volume_0, R.drawable.ic_volume_1, R.drawable.ic_volume_2,
            R.drawable.ic_volume_3, R.drawable.ic_volume_4, R.drawable.ic_volume_5, R.drawable.ic_volume_6
            , R.drawable.ic_volume_7, R.drawable.ic_volume_8};


    @SuppressLint("HandlerLeak")
    private void init() {
        //录音的JNI函数不具备线程安全性，所以要用单线程
        mExecutorService = Executors.newSingleThreadExecutor();
        volumeHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case -100:
                        // 录音时间太短
//                        stopRecording();
                        recordDialog.dismiss();
                        break;
                    case -1:
                        // 超时
                        finishRecord();
                        break;
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                        mStateIV.setImageResource(res[msg.what]);
                        break;
                }
            }
        };
    }

//    private AnimationDrawable anim;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!ChatSpUtils.instance(getContext()).getSendAudio().equals("1")) {
            // 禁止允许发送语音
            ToastUtils.showToast(getContext(), "您无法在该房间发送语音，请联系客服！");
            return true;
        }
        int action = event.getAction();
        y = event.getY();
        if (mStateTV != null && mStateIV != null && y < 0) {
            mStateTV.setText("松开手指,取消发送");
            mStateIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_volume_cancel));
        } else if (mStateTV != null) {
            mStateTV.setText("手指上滑,取消发送");
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (startRecordListener != null) {
                    startRecordListener.onStartRecord();
                }
                this.setText("松开发送");
                this.setBackgroundResource(R.drawable.shape_session_btn_voice_press);
                initDialogAndStartRecord();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                this.setText("按住录音(限时15秒)");
                this.setBackgroundResource(R.drawable.shape_session_btn_voice_normal);
                if (y >= 0 && (System.currentTimeMillis() - startTime <= MAX_INTERVAL_TIME)) {
                    LogUtils.e("ACTION_CANCEL", "结束录音");
                    finishRecord();
                } else {  //当手指向上滑，会cancel
                    recordDialog.dismiss();
                    File file = new File(mFile);
                    file.delete();
                }
                break;
        }

        return true;
    }

    /**
     * 初始化录音对话框 并 开始录音
     */
    private void initDialogAndStartRecord() {
        startTime = System.currentTimeMillis();
        recordDialog = new Dialog(getContext(), R.style.like_toast_dialog_style);
        // view = new ImageView(getContext());
        view = View.inflate(getContext(), R.layout.dialog_record, null);
        mStateIV = (ImageView) view.findViewById(R.id.rc_audio_state_image);
        mStateTV = (TextView) view.findViewById(R.id.rc_audio_state_text);
        mStateIV.setImageDrawable(getResources().getDrawable(R.drawable.anim_mic));
//        anim = (AnimationDrawable) mStateIV.getDrawable();
        mStateIV.setVisibility(View.VISIBLE);
        //mStateIV.setImageResource(R.drawable.ic_volume_1);
        mStateTV.setVisibility(View.VISIBLE);
        mStateTV.setText("手指上滑,取消发送");
        recordDialog.setContentView(view, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        recordDialog.setOnDismissListener(onDismiss);
        WindowManager.LayoutParams lp = recordDialog.getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        recordDialog.show();
        //提交后台任务，执行录音逻辑
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                startRecording();
            }
        });
    }

    /**
     * 放开手指，结束录音处理
     */
    private void finishRecord() {
        long intervalTime = System.currentTimeMillis() - startTime;
        if (intervalTime < MIN_INTERVAL_TIME) {
            LogUtils.e("finishRecord", "录音时间太短");
            mStateIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_volume_wraning));
            mStateTV.setText("录音时间太短");
            volumeHandler.sendEmptyMessageDelayed(-100, 500);
//            anim.stop();
//            File file = new File(mFile);
//            file.delete();
            return;
        }
        stopRecording();

        LogUtils.e("finishRecord:", "录音完成, 录音完成的路径:" + mFile);
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(mFile);
            mediaPlayer.prepare();
            mediaPlayer.getDuration();
            if (finishedListener != null) {
                int i = mediaPlayer.getDuration() / 1000;
                finishedListener.onFinishedRecord(mFile, i + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            recordDialog.dismiss();
        }
    }

    /**
     * 取消录音对话框和停止录音
     */
//    public void cancelRecord() {
//        stopRecording();
//        recordDialog.dismiss();
//        File file = new File(mFile);
//        file.delete();
//    }

    //获取类的实例
    // ExtAudioRecorder extAudioRecorder; //压缩的录音（WAV）

    /**
     * 执行录音操作
     */
    //int num = 0 ;
    private void startRecording() {
        mFile = getContext().getFilesDir() + "/" + "voice_" + System.currentTimeMillis() + ".amr";
        if (mRecorder != null) {
            mRecorder.reset();
        } else {
            mRecorder = new MediaRecorder();
        }
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mRecorder.setAudioChannels(1);
        mRecorder.setAudioSamplingRate(8000);
        mRecorder.setAudioEncodingBitRate(64);
        File file = new File(mFile);
        LogUtils.e("startRecording:", "创建文件的路径:" + mFile);
        LogUtils.e("startRecording:", "文件创建成功:" + file.exists());
        mRecorder.setOutputFile(mFile);
        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (Exception e) {
            LogUtils.e("startRecording:", "preparestart异常,重新开始录音:" + e.toString());
            e.printStackTrace();
            mRecorder.release();
            mRecorder = null;
            startRecording();
        }
        mThread = new ObtainDecibelThread();
        mThread.run();
    }


    private void stopRecording() {

        if (mThread != null) {
            mThread.exit();
            mThread = null;
        }
        if (mRecorder != null) {
            try {
                mRecorder.stop();//停止时没有prepare，就会报stop failed
                mRecorder.reset();
                mRecorder.release();
                mRecorder = null;
            } catch (RuntimeException pE) {
                pE.printStackTrace();
            } finally {
                if (recordDialog.isShowing()) {
                    recordDialog.dismiss();
                }
            }
        }

    }

    private class ObtainDecibelThread extends Thread {

        private volatile boolean running = true;

        public void exit() {
            running = false;
        }

        @Override
        public void run() {
            while (running) {
                if (mRecorder == null || !running) {
                    break;
                }
                if (System.currentTimeMillis() - startTime < MIN_INTERVAL_TIME) {
                    continue;
                }
                // int x = recorder.getMaxAmplitude(); //振幅
                int db = mRecorder.getMaxAmplitude();
                LogUtils.e("ObtainDecibelThread:", "检测到的分贝:" + db);
                if (db == 0) {
                    volumeHandler.sendEmptyMessage(0);
                } else if (db > 0 && db <= 200) {
                    volumeHandler.sendEmptyMessage(1);
                } else if (db > 200 && db <= 400) {
                    volumeHandler.sendEmptyMessage(2);
                } else if (db > 400 && db <= 800) {
                    volumeHandler.sendEmptyMessage(3);
                } else if (db > 800 && db <= 1600) {
                    volumeHandler.sendEmptyMessage(4);
                } else if (db > 1600 && db <= 3200) {
                    volumeHandler.sendEmptyMessage(5);
                } else if (db > 3200 && db <= 4800) {
                    volumeHandler.sendEmptyMessage(6);
                } else if (db > 4800 && db <= 6000) {
                    volumeHandler.sendEmptyMessage(7);
                } else {
                    volumeHandler.sendEmptyMessage(8);
                }

                long l = System.currentTimeMillis() - startTime - MAX_INTERVAL_TIME;
                if (l >= 0) {
                    volumeHandler.sendEmptyMessage(-1);
                }

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private DialogInterface.OnDismissListener onDismiss = new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
            stopRecording();
        }
    };

    public interface OnFinishedRecordListener {
        void onFinishedRecord(String audioPath, int time);
    }

    public interface OnStartRecordListener {
        void onStartRecord();
    }
}
