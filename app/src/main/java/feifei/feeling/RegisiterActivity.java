package feifei.feeling;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import library.util.AnimUtil;
import library.util.TS;
import library.widget.ClearEditText;

public class RegisiterActivity extends BaseActivity implements UserProxy.ISignUpListener {

    @Bind(R.id.user_name_input)
    ClearEditText userNameInput;
    @Bind(R.id.user_password_input)
    ClearEditText userPasswordInput;
    @Bind(R.id.user_email_input)
    ClearEditText userEmailInput;
    @Bind(R.id.register)
    Button        register;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_register);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.register)
    public void onClick() {
        String name = userNameInput.getText().toString();
        String password = userPasswordInput.getText().toString();
        String email = userEmailInput.getText().toString();
        if (TextUtils.isEmpty(name)) {
            TS.s(this, "请输入用户名");
            AnimUtil.animShakeText(userNameInput);
        } else if (TextUtils.isEmpty(password)) {
            TS.s(this, "请输入密码");
            AnimUtil.animShakeText(userPasswordInput);
        } else if (TextUtils.isEmpty(email)) {
            TS.s(this, "请输入邮箱");
            AnimUtil.animShakeText(userEmailInput);
        } else {
            UserProxy userProxy = new UserProxy(this);
            userProxy.setOnSignUpListener(this);
            userProxy.signUp(name, password, email);
        }
    }

    @Override
    public void onSignUpSuccess() {
        TS.s(this, "注册成功");
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onSignUpFailure(String msg) {
        TS.s(this, "注册失败" + msg);
    }
}
