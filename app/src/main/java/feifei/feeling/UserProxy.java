package feifei.feeling;

import android.content.Context;
import android.util.Log;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import feifei.feeling.bean.User;

public class UserProxy {
    public static final String SEX_MALE   = "male";
    public static final String SEX_FEMALE = "female";

    public static final String TAG = "UserProxy";

    private Context mContext;

    public UserProxy(Context context) {
        this.mContext = context;
    }

    public void signUp(String userName, String password, String email) {
        User user = new User();
        user.setUsername(userName);
        user.setPassword(password);
        user.setEmail(email);
        user.setSex(SEX_FEMALE);
        user.setSignature("这个家伙很懒，什么也不说。。。");
        user.signUp(mContext, new SaveListener() {

            @Override
            public void onSuccess() {
                if (signUpLister != null) {
                    signUpLister.onSignUpSuccess();
                } else {
                }
            }

            @Override
            public void onFailure(int arg0, String msg) {
                if (signUpLister != null) {
                    signUpLister.onSignUpFailure(msg);
                } else {
                }
            }
        });
    }

    public interface ISignUpListener {
        void onSignUpSuccess();

        void onSignUpFailure(String msg);
    }

    private ISignUpListener signUpLister;

    public void setOnSignUpListener(ISignUpListener signUpLister) {
        this.signUpLister = signUpLister;
    }


    public User getCurrentUser() {
        User user = BmobUser.getCurrentUser(mContext, User.class);
        if (user != null) {
            Log.i(TAG, "本地用户信息" + user.getObjectId() + "-"
                    + user.getUsername() + "-"
                    + user.getSessionToken() + "-"
                    + user.getCreatedAt() + "-"
                    + user.getUpdatedAt() + "-"
                    + user.getSignature() + "-"
                    + user.getSex());
            return user;
        } else {
            Log.i(TAG, "本地用户为null,请登录。");
        }
        return null;
    }

    public static boolean getCurrentUser(Context mContext) {
        User user = BmobUser.getCurrentUser(mContext, User.class);
        return user != null;
    }

    public void login(String userName, String password) {
        final BmobUser user = new BmobUser();
        user.setUsername(userName);
        user.setPassword(password);
        user.login(mContext, new SaveListener() {

            @Override
            public void onSuccess() {
                if (loginListener != null) {
                    loginListener.onLoginSuccess();
                } else {
                }
            }

            @Override
            public void onFailure(int arg0, String msg) {
                if (loginListener != null) {
                    loginListener.onLoginFailure(msg);
                } else {
                }
            }
        });
    }

    public interface ILoginListener {
        void onLoginSuccess();

        void onLoginFailure(String msg);
    }

    private ILoginListener loginListener;

    public void setOnLoginListener(ILoginListener loginListener) {
        this.loginListener = loginListener;
    }

    public void logout() {
        BmobUser.logOut(mContext);
        Log.i(TAG, "logout result:" + (null == getCurrentUser()));
    }

    public void update(String... args) {
        User user = getCurrentUser();
        user.setUsername(args[0]);
        user.setEmail(args[1]);
        user.setPassword(args[2]);
        user.setSex(args[3]);
        user.setSignature(args[4]);
        //...
        user.update(mContext, new UpdateListener() {

            @Override
            public void onSuccess() {
                if (updateListener != null) {
                    updateListener.onUpdateSuccess();
                } else {
                }
            }

            @Override
            public void onFailure(int arg0, String msg) {
                if (updateListener != null) {
                    updateListener.onUpdateFailure(msg);
                } else {
                }
            }
        });
    }

    public interface IUpdateListener {
        void onUpdateSuccess();

        void onUpdateFailure(String msg);
    }

    private IUpdateListener updateListener;

    public void setOnUpdateListener(IUpdateListener updateListener) {
        this.updateListener = updateListener;
    }
}
