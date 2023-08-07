package com.yibo.yiboapp.route.LDNetActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yibo.yiboapp.BuildConfig;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.activity.BaseActivity;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.route.LDNetDiagnoService.LDNetDiagnoListener;
import com.yibo.yiboapp.route.LDNetDiagnoService.LDNetDiagnoService;

import java.nio.charset.Charset;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static com.example.anuo.immodule.utils.AESUtils.byteToHexString;


public class RouteCheckingActivity extends BaseActivity implements OnClickListener,
        LDNetDiagnoListener {


    public static void createIntent(Context context) {
        Intent intent = new Intent(context, RouteCheckingActivity.class);
        context.startActivity(intent);
    }

    private Button btn;
    private ProgressBar progress;
    private TextView text;
    private EditText edit;
    private String showInfo = "";
    private boolean isRunning = false;
    private LDNetDiagnoService _netDiagnoService;

    private String domainNameNew;
    private String encryptDomainName;
    private String Algorithm = "AES";
    private String AlgorithmProvider = "AES/CBC/PKCS5Padding";
    public final String DEFAULT_IV = "0>2$#~*6(~9a7#D$";
    public final String DEFAULT_KEY = "5Po&)11n&v3#M.{:";
    private Button btn_copy_domain;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_checking);
        initView();
    }


    @Override
    protected void initView() {
        super.initView();
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(this);
        progress = (ProgressBar) findViewById(R.id.progress);
        progress.setVisibility(View.INVISIBLE);
        text = (TextView) findViewById(R.id.text);
        edit = (EditText) findViewById(R.id.domainName);
        btn_copy_domain = findViewById(R.id.btn_copy_domain);
        edit.clearFocus();
        tvMiddleTitle.setText("路由检测");
        tvBackText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setOnBackPress();
            }
        });
        btn_copy_domain.setOnClickListener(this);

        startChecking();

    }

    @Override
    public void onClick(View v) {
        if (v == btn) {
            UsualMethod.copy(text.getText().toString().trim(), RouteCheckingActivity.this);
            ToastUtils.showShort("复制成功");
        }else if (v==btn_copy_domain){
            UsualMethod.copy(encryptDomainName, RouteCheckingActivity.this);
            ToastUtils.showShort("复制成功");
        }
    }

    private void startChecking() {
        if (!isRunning) {
            showInfo = "";
            String domainName = BuildConfig.domain_url;
//            https://yibo11.com:59789
            int count = 0  ;
            for (int i = 0; i < domainName.length(); i++) {
                if (":".equals(String.valueOf(domainName.charAt(i)))){
                    count++;
                }
            }
            if (count==1){
                //没有端口
                domainNameNew = domainName.substring(domainName.lastIndexOf("/") + 1 );
            }else{
                //有端口
                try {
                    domainNameNew = domainName.substring(domainName.lastIndexOf("/") + 1 ,domainName.lastIndexOf(":"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            encryptDomainName = encrypt(domainName);
            edit.setText(domainNameNew);
            _netDiagnoService = new LDNetDiagnoService(getApplicationContext(),
                    "testDemo", "网络诊断应用", "1.0.0", "huipang@corp.netease.com",
                    "deviceID(option)", domainNameNew, "carriname", "ISOCountyCode",
                    "MobilCountryCode", "MobileNetCode", this,encryptDomainName,2);
            // 设置是否使用JNIC 完成traceroute
            _netDiagnoService.setIfUseJNICTrace(true);
//        _netDiagnoService.setIfUseJNICConn(true);
            _netDiagnoService.execute();
            progress.setVisibility(View.VISIBLE);
            text.setText("Traceroute with max 30 hops...");
//            btn.setText("停止诊断");
            btn.setEnabled(false);
            edit.setInputType(InputType.TYPE_NULL);
        } else {
            progress.setVisibility(View.GONE);
//            btn.setText("开始诊断");
            _netDiagnoService.cancel(true);
            _netDiagnoService = null;
            btn.setEnabled(true);
            edit.setInputType(InputType.TYPE_CLASS_TEXT);
        }

        isRunning = !isRunning;
    }


    //记录用户首次点击返回键的时间
    private long firstTime = 0;

    @Override
    public void onBackPressed() {
        setOnBackPress();
    }

    private void setOnBackPress() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            Toast.makeText(RouteCheckingActivity.this, "再按一次退出页面", Toast.LENGTH_SHORT).show();
            firstTime = secondTime;
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (_netDiagnoService != null) {
            _netDiagnoService.stopNetDialogsis();
        }
    }


    @Override
    public void OnNetDiagnoFinished(String log) {
        text.setText(log);
        System.out.println("");
        progress.setVisibility(View.GONE);
//        btn.setText("开始诊断");
        btn.setEnabled(true);
        edit.setInputType(InputType.TYPE_CLASS_TEXT);
        isRunning = false;
    }

    @Override
    public void OnNetDiagnoUpdated(String log) {
        showInfo += log;
        text.setText(showInfo);
    }

    public String encrypt(String domain) {
        if (TextUtils.isEmpty(domain)) {
            return domain;
        }
        //把字符串转为字节数组
        byte[] b = domain.getBytes();
        for(int i=0;i<b.length;i++) {
            b[i] += 1;//在原有的基础上+1
        }
        return new String(b);
    }

    public String encrypt(String source, String key, String iv) {
        try {
            SecretKey secretKey = new SecretKeySpec(key.getBytes(), Algorithm);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes("utf-8"));
            Cipher cipher = Cipher.getInstance(AlgorithmProvider);
            cipher.init(1, secretKey, ivParameterSpec);
            byte[] cipherBytes = cipher.doFinal(source.getBytes(Charset.forName("utf-8")));
            return byteToHexString(cipherBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
