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

public class LoginActivity extends BaseActivity implements UserProxy.ILoginListener, UserProxy.ISignUpListener {

    @Bind(R.id.user_name_input)
    ClearEditText userNameInput;
    @Bind(R.id.user_password_input)
    ClearEditText userPasswordInput;
    @Bind(R.id.register)
    Button        register;
    @Bind(R.id.login)
    Button        login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.login)
    public void onClick() {
        String name = userNameInput.getText().toString();
        String password = userPasswordInput.getText().toString();
        if (TextUtils.isEmpty(name)) {
            TS.s(this, "请输入用户名");
            AnimUtil.animShakeText(userNameInput);
        } else if (TextUtils.isEmpty(password)) {
            TS.s(this, "请输入密码");
            AnimUtil.animShakeText(userPasswordInput);
        } else {
            UserProxy userProxy = new UserProxy(this);
            userProxy.setOnLoginListener(this);
            userProxy.login(name, password);
        }
    }


    @Override
    public void onLoginSuccess() {
        TS.s(this, "登陆成功");
        String s = getIntent().getStringExtra("from");
        if (TextUtils.equals(s, "splash")) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            finish();
        }
    }

    @Override
    public void onLoginFailure(String msg) {
        TS.s(this, "登陆失败" + msg);
    }

    @OnClick(R.id.register)
    public void onClick2() {
        startActivity(new Intent(this, RegisiterActivity.class));
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
