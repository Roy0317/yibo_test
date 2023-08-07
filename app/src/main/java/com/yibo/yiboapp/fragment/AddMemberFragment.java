package com.yibo.yiboapp.fragment;

import android.content.Intent;
import androidx.cardview.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;


import com.google.gson.reflect.TypeToken;

import com.simon.widget.ToastUtil;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.activity.MemberListActivity;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.AddMyMemberBean;
import com.yibo.yiboapp.utils.Utils;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;

/**
 * Author: Ray
 * created on 2018年10月12日21:25:24
 * description : "添加会员"Fragment
 */
public class AddMemberFragment extends RecommendationBaseFragment implements View.OnClickListener, SessionResponse.Listener<CrazyResult> {
    private static final String TAG = "AddMemberFragment";

    private EditText editTextUserName;

    private EditText editTextPassword;

    private EditText editTextPasswordConfirm;

    public static final int ADD_MEMBER_REQUEST = 0x01;

    @Override
    public void initWidget() {
        editTextUserName = find(R.id.et_input_user_name);
        editTextPassword = find(R.id.et_input_password);
        editTextPasswordConfirm = find(R.id.et_input_password_again);
        CardView cardViewConfirm = find(R.id.cd_confirm);
        cardViewConfirm.setOnClickListener(this);

    }

    /**
     * 初始化布局
     */
    @Override
    public View initView(LayoutInflater layoutInflater) {
        return layoutInflater.inflate(R.layout.fragment_add_member, null);
    }

    /**
     * 加载网络数据
     */
    @Override
    public void fetchData() {
        //doNothing
    }

    /**
     * 点击事件
     *
     * @param v 点击的控件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cd_confirm:
                onNextStep();
                break;
        }
    }

    /**
     * 点击确定时的逻辑判断
     */
    private void onNextStep() {

        boolean addMember = activity!=null
                && UsualMethod.getConfigFromJson(activity)!=null
                && UsualMethod.getConfigFromJson(activity).getOnoff_adduser_front()!=null
                && UsualMethod.getConfigFromJson(activity).getOnoff_adduser_front().equals("on");
        if (!addMember){
            ToastUtil.showToast(activity,"前台暂不可以手动添加会员，请联系管理员");
            return;
        }

        String userName = getStringContent(editTextUserName);
        String password = getStringContent(editTextPassword);
        String passwordConfirm = getStringContent(editTextPasswordConfirm);

        //仿照web版进行各种限制（xxxxxxxxx）
        if (TextUtils.isEmpty(userName)) {
            showToast("用户名不能为空");
//        } else if (userName.length() < 6) {
//            showToast("用户名为6-20位数字或密码组合");
        } else if (TextUtils.isEmpty(password)) {
            showToast("密码不能为空");
        } else if (password.length() < 6) {
            showToast("密码为6-15位的数字或密码组合");
        } else if (TextUtils.isEmpty(passwordConfirm)) {
            showToast("确认密码不能为空");
//        } else if (Utils.isNumeric(userName)) {
//            showToast("用户名不能为纯数字");
        } else if (Utils.isChar(userName)) {
            showToast("用户名不能为纯字母");
        }
//        else if (Utils.isNumeric(password)) {
//            showToast("密码不能为纯数字");
//        }
        else if (Utils.isChar(password)) {
            showToast("密码不能为纯字母");
        } else if (!password.equals(passwordConfirm)) {
            showToast("两次密码输入不相同，请再次确认！");
        } else {
            //满足一切苛刻的条件才能进入这里
            StringBuilder params = new StringBuilder();

            params.append("?");
            params.append("username").append("=").append(userName).append("&");
            params.append("pwd").append("=").append(password).append("&");
            params.append("okPwd").append("=").append(passwordConfirm);

            Utils.LOG(TAG, "the add member params = " + params.toString());

            //访问的url
            String urls = Urls.BASE_URL + Urls.PORT + Urls.RECOMMEND_ADDUSER_URL + params;
            //构建http请求
            CrazyRequest<CrazyResult<AddMyMemberBean>> request = new AbstractCrazyRequest.Builder().
                    url(urls.toString())
                    .seqnumber(ADD_MEMBER_REQUEST)
                    .listener(this)
                    .headers(Urls.getHeader(activity))
                    .placeholderText(getString(R.string.add_member_ongoing))
                    .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                    .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                    .convertFactory(GsonConverterFactory.create(new TypeToken<AddMyMemberBean>() {
                    }.getType()))
                    .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
                    .create();

            RequestManager.getInstance().startRequest(activity, request);

        }


    }


    private String getStringContent(EditText editText) {
        return editText.getText().toString().trim();
    }

    @Override
    public void onResponse(SessionResponse<CrazyResult> response) {
        RequestManager.getInstance().afterRequest(response);
        if (activity.isFinishing() || response == null) return;
        int action = response.action;
        switch (action) {
            case ADD_MEMBER_REQUEST:
                CrazyResult result = response.result;
                if (result == null || !result.crazySuccess) {
                    showToast(getString(R.string.add_member_fail));
                    return;
                }

                AddMyMemberBean addMyMemberBean = (AddMyMemberBean) result.result;
                if (!addMyMemberBean.isSuccess()) {
                    showToast(!Utils.isEmptyString(addMyMemberBean.getMsg()) ? addMyMemberBean.getMsg() :
                            getString(R.string.add_member_fail));
                    //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                    //所以此接口当code == 0时表示帐号被踢，或登录超时
                    if (addMyMemberBean.getCode() == 0) {
                        UsualMethod.loginWhenSessionInvalid(activity);
                    }
                    return;
                }

                if (addMyMemberBean.isContent()) {
                    startActivity(new Intent(activity, MemberListActivity.class));
                    showToast("会员添加成功");
                } else {
                    showToast("会员添加失败，请尝试重新登录或者联系客服");
                }
                YiboPreference.instance(activity).setToken(addMyMemberBean.getAccessToken());
                break;

        }


    }
}
