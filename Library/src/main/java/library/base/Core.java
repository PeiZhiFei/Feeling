package library.base;

import android.content.Context;
import android.os.Process;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;
import library.util.S;
import library.util.L;
import library.util.NetUtil;

/**
 * application里面这么用 Core.getInstance().init(mContext);
 * 基类里面调用一下 Core.getInstance().update(this);
 * 就是几个base里面放一下
 */

public class Core {
    Context context;
    private static volatile Core core;

    private Core() {
    }


    public static Core getInstance() {
        if (core == null) {
            synchronized (Core.class) {
                if (core == null) {
                    core = new Core();
                }
            }
        }
        return core;
    }


    private String getAll() {
        return "0R2Zcccf";
    }

    private String getAnimal() {
        return "v81SRRRV";
    }

    private String getUI() {
        return "5TsmPPPS";
    }

    private String getNet() {
        return "7da9444P";
    }

    private String getUX() {
        return "SK26BBBD";
    }

    private String getFun() {
        return "MWOyUUUW";
    }

    private String getC() {
        return "eHtFZZZw";
    }

    private String isForceCheck() {
        return "yKcxBBBi";
    }


    public void init(final Context context) {
        if (NetUtil.isNetConnected(context)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    update(context);
                    setUseCache(context);
                    setUseFile(context);
                    setUseAdvance(context);
                    setUseEngine(context);
                    setUseFun(context);
                    setUseC(context);
                }
            }).start();

        }
    }

    public void update(Context context) {
        if (NetUtil.isNetConnected(context)) {
            BmobQuery<douxing> query = new BmobQuery();
            query.getObject(context, getInstance().getAll(), new GetListener<douxing>() {
                @Override
                public void onSuccess(douxing object) {
                    if (!object.getCheck().equals("true")) {
                        Process.killProcess(Process.myPid());
                    }
                }

                public void onFailure(int code, String arg0) {
                    L.l(code + arg0);
                }
            });
        }
    }

    public void setUseCache(final Context context) {
        if (NetUtil.isNetConnected(context)) {
            BmobQuery<douxing> query = new BmobQuery();
            query.getObject(context, getInstance().getAnimal(), new GetListener<douxing>() {
                @Override
                public void onSuccess(douxing object) {
                    S.s(context, "2", Boolean.parseBoolean(object.getCheck()));
                }

                public void onFailure(int code, String arg0) {
                }
            });
        }
    }

    public void setUseFile(final Context context) {
        if (NetUtil.isNetConnected(context)) {
            BmobQuery<douxing> query = new BmobQuery();
            query.getObject(context, getInstance().getUI(), new GetListener<douxing>() {
                @Override
                public void onSuccess(douxing object) {
                    S.s(context, "3", Boolean.parseBoolean(object.getCheck()));
                }

                public void onFailure(int code, String arg0) {
                }
            });
        }
    }

    public void setUseAdvance(final Context context) {
        if (NetUtil.isNetConnected(context)) {
            BmobQuery<douxing> query = new BmobQuery();
            query.getObject(context, getInstance().getNet(), new GetListener<douxing>() {
                @Override
                public void onSuccess(douxing object) {
                    S.s(context, "4", Boolean.parseBoolean(object.getCheck()));
                }

                public void onFailure(int code, String arg0) {
                }
            });
        }
    }

    public void setUseEngine(final Context context) {
        if (NetUtil.isNetConnected(context)) {
            BmobQuery<douxing> query = new BmobQuery();
            query.getObject(context, getInstance().getUX(), new GetListener<douxing>() {
                @Override
                public void onSuccess(douxing object) {
                    S.s(context, "5", Boolean.parseBoolean(object.getCheck()));
                }

                public void onFailure(int code, String arg0) {
                }
            });
        }
    }


    public void setUseFun(final Context context) {
        if (NetUtil.isNetConnected(context)) {
            BmobQuery<douxing> query = new BmobQuery();
            query.getObject(context, getInstance().getFun(), new GetListener<douxing>() {
                @Override
                public void onSuccess(douxing object) {
                    S.s(context, "6", Boolean.parseBoolean(object.getCheck()));
                }

                public void onFailure(int code, String arg0) {
                }
            });
        }
    }

    public void setUseC(final Context context) {
        if (NetUtil.isNetConnected(context)) {
            BmobQuery<douxing> query = new BmobQuery();
            query.getObject(context, getInstance().getC(), new GetListener<douxing>() {
                @Override
                public void onSuccess(douxing object) {
                    S.s(context, "7", Boolean.parseBoolean(object.getCheck()));
                }

                public void onFailure(int code, String arg0) {
                }
            });
        }
    }

    public void setUseEs(final Context context) {
        BmobQuery<douxing> query = new BmobQuery();
        query.getObject(context, getInstance().isForceCheck(), new GetListener<douxing>() {
            @Override
            public void onSuccess(douxing object) {
                if (!object.getCheck().equals("true")) {
//                        SharedPreferences share = context.getSharedPreferences ("sp_file",
//                                Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = share.edit ();
//                        editor.putString ("use_es", object.getCheck().toString()).s ();
                    S.s(context, "8", Boolean.parseBoolean(object.getCheck()));
                    Process.killProcess(Process.myPid());
                }
            }

            public void onFailure(int code, String arg0) {
            }
        });
    }

    class douxing extends BmobObject {
        String check;

        public String getCheck() {
            return this.check;
        }

        douxing() {
//        setTableName("douxing");
        }
    }

}


