package com.feifei.game;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.feifei.lifetools.R;
import com.feifei.util.AnimUtil;
import com.rey.material.widget.Slider;


public class GameActivity extends AppCompatActivity implements DialogColor.Callback, View.OnClickListener {

    public Drawable[] drawable = new Drawable[13];
    //arrayback问题,对象类型不要直接等于
    public String[] array = new String[13];
    public static int textcolor = Color.DKGRAY;
    public static int textsize = 20;
    public static int numbers = 5;
    public static boolean isImage = true;
    //新纪录使用
    boolean newGame = true;
    TextView scoretext;
    TextView highscoretext;
    private static GameActivity gameActivity;
    GameFragment gameFragment;
    SharedPreferences sp;
    SharedPreferences.Editor ed;
    FragmentManager fragmentManager;

    //为了让外界获取到mainactivity的方法
    public GameActivity() {
        gameActivity = this;
    }

    public static GameActivity getGameActivity() {
        return gameActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        showUpdate("http://music.baidu.com/cms/mobile/static/apk/BaiduMusic-pcwebdownpagetest.apk");
        sp = getSharedPreferences(Constant.PREFERENCE_FILE, Context.MODE_PRIVATE);
        ed = sp.edit();
        initDrawable();
        updateConfig();
        setContentView(R.layout.ui_main);
        gameFragment = new GameFragment();
        fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, gameFragment);
        fragmentTransaction.commit();
        scoretext = (TextView) findViewById(R.id.score);
        highscoretext = (TextView) findViewById(R.id.high);
        highscoretext.setText("最高分: " + getBestScore());

//        new  android.support.v7.app.AlertDialog.Builder(this)
//                .setTitle("234")
//                .setPositiveButton("34", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                })
//                .setView(R.layout.custom_edit)
//                .show();

    }

    private void initDrawable() {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(Constant.DRAWABLE_RADIUS);
        gradientDrawable.setColor(0x30ffffff);
        drawable[0] = gradientDrawable;
//        drawable[0] = Constant.getEmptyDrawable();
        array[0] = "";
    }

    private void updateConfig() {
        numbers = sp.getInt(Constant.KEY_COLUMENUMBER, 5);
        isImage = sp.getBoolean(Constant.KEY_IMAGE, true);
        if (sp.getBoolean(Constant.KEY_IMAGE, true)) {
            for (int i = 1; i < drawable.length; i++) {
                String s2 = Constant.path + i + ".PNG";
                //如果有自定义图片就使用
                if (Tools.isFileExist(s2)) {
                    drawable[i] = Tools.bitmap2Drawable(Tools.file2Bitmap(s2));
                } else {
                    drawable[i] = getResources().getDrawable(Constant.dw[i - 1]);
                }
            }

        } else if (sp.getBoolean(Constant.KEY_DEFAULT, false)) {
            textsize = 20;
            textcolor = Color.DKGRAY;
            numbers = 5;
            try {
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(textcolor));
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 1; i < drawable.length; i++) {
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setCornerRadius(Constant.DRAWABLE_RADIUS);
                gradientDrawable.setColor(Constant.color[i]);
                drawable[i] = gradientDrawable;
                array[i] = Constant.arrayBack[i];
            }

        } else {
            textcolor = sp.getInt(Constant.KEY_TEXTCOLOR, Color.DKGRAY);
            try {
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(textcolor));
            } catch (Exception e) {
                e.printStackTrace();
            }
            textsize = sp.getInt(Constant.KEY_TEXTSIZE, 20);
            for (int i = 1; i < drawable.length; i++) {
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setCornerRadius(Constant.DRAWABLE_RADIUS);
                gradientDrawable.setColor(sp.getInt(Constant.KEY_COLORARRAY + i, Constant.color[i]));
                drawable[i] = gradientDrawable;
                array[i] = sp.getString(Constant.KEY_EDITARRAY + i, Constant.arrayBack[i]);
            }
        }
    }


    int score = 0;

    public void addScore(int s) {
        score += s;
        scoretext.setText("" + score);
        if (score > getBestScore()) {
            if (newGame) {
                try {
                    new MaterialDialog.Builder(this)
                            .title("新纪录诞生")
                            .titleGravity(GravityEnum.CENTER)
                            .titleColorRes(android.R.color.holo_purple)
                            .content("恭喜你创造了新纪录！")
                            .contentGravity(GravityEnum.CENTER)
                            .contentColorRes(android.R.color.black)
                            .theme(Theme.LIGHT)
                            .show();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    newGame = false;
                }
            }

        }
        int maxScore = Math.max(score, getBestScore());
        ed.putInt(Constant.KEY_HIGHSCORE, maxScore);
        ed.apply();
        showBest(maxScore);

    }

    public void showBest(int s) {
        highscoretext.setText("最高分: " + s);
    }

    public void clearScore() {
        score = 0;
        scoretext.setText("0");
    }

    public void restart(View v) {
        updateConfig();
        newGame = true;
        gameFragment.normal();
        // 初始化MediaPlay对象 ，准备播放音乐
        MediaPlayer mPlayer = MediaPlayer.create(this, R.raw.news);
        // 开始播放
        mPlayer.start();
    }

    //如何转成局部变量
    Slider seekbar2 = null, seekbar1 = null;
    TextView textx = null, texty = null;
    Button colorbutton = null;

//    public void grid(View v) {
//        MaterialDialog dialog = new MaterialDialog.Builder(this)
//                .title("网格设置")
//                .titleGravity(GravityEnum.CENTER)
//                .titleColorRes(android.R.color.holo_purple)
//                .customView(R.layout.d_setting, true)
//                .positiveText("确定")
//                .positiveColorRes(android.R.color.holo_blue_dark)
//                .negativeText("取消")
//                .negativeColorRes(android.R.color.holo_green_dark)
//                .callback(new MaterialDialog.ButtonCallback() {
//                    @Override
//                    public void onPositive(MaterialDialog dialog) {
//                        ed.putInt(Constant.KEY_TEXTSIZE, seekbar2.getValue());
//                        ed.putInt(Constant.KEY_COLUMENUMBER, seekbar1.getValue());
////                        numbers = seekbar1.getValue();
//                        ed.apply();
//                        restart(null);
//                    }
//                })
//                .build();
//        colorbutton = (Button) dialog.getCustomView().findViewById(R.id.buttoncolor);
//        colorbutton.setTextColor(sp.getInt(Constant.KEY_TEXTCOLOR, Color.DKGRAY));
//        seekbar1 = (Slider) dialog.getCustomView().findViewById(R.id.seekBar1);
//        textx = (TextView) dialog.getCustomView().findViewById(R.id.textx);
//        seekbar2 = (Slider) dialog.getCustomView().findViewById(R.id.seekBar2);
//        texty = (TextView) dialog.getCustomView().findViewById(R.id.texty);
//        int n = sp.getInt(Constant.KEY_COLUMENUMBER, 5);
//        int m = sp.getInt(Constant.KEY_TEXTSIZE, 20);
//        textx.setText("列数：" + n);
//        texty.setText("字体大小：" + m);
//        seekbar2.setValue((m), true);
//        seekbar1.setValue((n), true);
//        seekbar2.setOnPositionChangeListener(new Slider.OnPositionChangeListener() {
//            @Override
//            public void onPositionChanged(Slider slider, boolean b, float v, float v1, int i, int i1) {
//                texty.setText("字体大小：" + i1);
//            }
//        });
//        seekbar1.setOnPositionChangeListener(new Slider.OnPositionChangeListener() {
//            @Override
//            public void onPositionChanged(Slider slider, boolean b, float v, float v1, int i, int i1) {
//                textx.setText("列数：" + i1);
//            }
//        });
//        dialog.show();
//    }

    public void feedback(View v) {
        new MaterialDialog.Builder(this)
                .title("意见反馈")
                .titleGravity(GravityEnum.START)
                .titleColorRes(android.R.color.holo_purple)
//                    .customView(R.layout.d_edittext, true)
                .positiveText("确定")
                .positiveColorRes(android.R.color.holo_blue_dark)
                .negativeText("取消")
                .negativeColorRes(android.R.color.holo_green_dark)
                .input("请给我一点建议吧", "", false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        // if ((!TextUtils.isEmpty(input.toString()))&&(!input.toString().trim().equals(""))) {
                        if (!Tools.isNetworkAvailable(GameActivity.this)) {
                            Toast.makeText(GameActivity.this, "发送失败，请先打开网络吧！", Toast.LENGTH_SHORT).show();
                        } else {
                            SendUtil.sendMail(input.toString());
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(GameActivity.this, "感谢你的反馈！", Toast.LENGTH_SHORT).show();

                                }
                            }, 1000);
                            dialog.dismiss();

                        }
                    }
                }).show();
    }

    public void color(View v) {
        new DialogColor().show(this, selectedColorIndex);
    }


    public void role(View v) {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("图像模式")
                .titleGravity(GravityEnum.CENTER)
                .titleColorRes(android.R.color.holo_purple)
                .customView(R.layout.d_images, true)
                .positiveText("确定")
                .positiveColorRes(android.R.color.holo_blue_dark)
                .negativeText("游戏规则")
                .negativeColorRes(android.R.color.holo_green_dark)
                .neutralText("默认头像")
                .neutralColorRes(android.R.color.holo_orange_dark)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        ed.putBoolean(Constant.KEY_IMAGE, true);
                        ed.apply();
                        restart(null);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        dialog.dismiss();
                        new MaterialDialog.Builder(GameActivity.this)
                                .title("游戏规则")
                                .titleGravity(GravityEnum.CENTER)
                                .titleColorRes(android.R.color.holo_purple)
                                .content(Constant.role)
                                .contentGravity(GravityEnum.START)
                                .contentColorRes(android.R.color.black)
                                .theme(Theme.LIGHT)
                                .show();
                    }

                    @Override
                    public void onNeutral(MaterialDialog dialog) {
                        Tools.deleteImages();
                        ed.putBoolean(Constant.KEY_IMAGE, true);
                        ed.apply();
                        restart(null);
                    }
                })
                .build();

        final GridLayout list = (GridLayout) dialog.getCustomView().findViewById(R.id.grid);
        for (int i = 0; i < list.getChildCount(); i++) {
            ImageView child = (ImageView) list.getChildAt(i);
            icon[i] = child;
            child.setTag(i);
            child.setOnClickListener(this);
            String s2 = Constant.path + (i + 1) + ".PNG";
            if (Tools.isFileExist(s2)) {
                Bitmap b = Tools.file2Bitmap(s2);
                child.setImageBitmap(b);
            } else {
                child.setBackgroundResource(Constant.dw[i]);
            }
        }
        dialog.show();
    }

    private EditText[] editTexts = new EditText[12];//自定义文字序列
    private Button[] image = new Button[12];//自定义色调序列
    private ImageView[] icon = new ImageView[12];//自定义头像序列
    private Button v;//统一颜色dialog的数组按钮
    private Integer index;//头像序列的tag

    public void custom(View v) {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("色调模式")
                .titleGravity(GravityEnum.CENTER)
                .titleColorRes(android.R.color.holo_purple)
                .customView(R.layout.d_edittext, true)
                .positiveText("确定")
                .positiveColorRes(android.R.color.holo_blue_dark)
                .negativeText("取消")
                .negativeColorRes(android.R.color.holo_green_dark)
                .neutralText("默认")
                .neutralColorRes(android.R.color.holo_orange_dark)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        for (int i = 0; i < editTexts.length; i++) {
                            ed.putString(Constant.KEY_EDITARRAY + (i + 1), editTexts[i].getText().toString());
                            ed.putInt(Constant.KEY_COLORARRAY + (i + 1), image[i].getHighlightColor());
                        }
                        ed.putInt(Constant.KEY_TEXTSIZE, seekbar2.getValue());
                        ed.putInt(Constant.KEY_COLUMENUMBER, seekbar1.getValue());
                        ed.putBoolean(Constant.KEY_IMAGE, false);
                        ed.putBoolean(Constant.KEY_DEFAULT, false);
                        ed.apply();
                        restart(null);
                    }

                    @Override
                    public void onNeutral(MaterialDialog dialog) {
                        ed.putBoolean(Constant.KEY_IMAGE, false);
                        ed.putBoolean(Constant.KEY_DEFAULT, true);
                        ed.apply();
                        restart(null);
                    }

//                    @Override
//                    public void onNegative(MaterialDialog dialog) {
//                        ed.putInt(Constant.KEY_TEXTSIZE, seekbar2.getValue());
//                        ed.putInt(Constant.KEY_COLUMENUMBER, seekbar1.getValue());
//                        ed.apply();
//                        restart(null);
//                    }
                })
                .cancelable(false)
                .build();
        editTexts[0] = (EditText) dialog.getCustomView().findViewById(R.id.text1);
        editTexts[1] = (EditText) dialog.getCustomView().findViewById(R.id.text2);
        editTexts[2] = (EditText) dialog.getCustomView().findViewById(R.id.text3);
        editTexts[3] = (EditText) dialog.getCustomView().findViewById(R.id.text4);
        editTexts[4] = (EditText) dialog.getCustomView().findViewById(R.id.text5);
        editTexts[5] = (EditText) dialog.getCustomView().findViewById(R.id.text6);
        editTexts[6] = (EditText) dialog.getCustomView().findViewById(R.id.text7);
        editTexts[7] = (EditText) dialog.getCustomView().findViewById(R.id.text8);
        editTexts[8] = (EditText) dialog.getCustomView().findViewById(R.id.text9);
        editTexts[9] = (EditText) dialog.getCustomView().findViewById(R.id.text10);
        editTexts[10] = (EditText) dialog.getCustomView().findViewById(R.id.text11);
        editTexts[11] = (EditText) dialog.getCustomView().findViewById(R.id.text12);

        image[0] = (Button) dialog.getCustomView().findViewById(R.id.imageView1);
        image[1] = (Button) dialog.getCustomView().findViewById(R.id.imageView2);
        image[2] = (Button) dialog.getCustomView().findViewById(R.id.imageView3);
        image[3] = (Button) dialog.getCustomView().findViewById(R.id.imageView4);
        image[4] = (Button) dialog.getCustomView().findViewById(R.id.imageView5);
        image[5] = (Button) dialog.getCustomView().findViewById(R.id.imageView6);
        image[6] = (Button) dialog.getCustomView().findViewById(R.id.imageView7);
        image[7] = (Button) dialog.getCustomView().findViewById(R.id.imageView8);
        image[8] = (Button) dialog.getCustomView().findViewById(R.id.imageView9);
        image[9] = (Button) dialog.getCustomView().findViewById(R.id.imageView10);
        image[10] = (Button) dialog.getCustomView().findViewById(R.id.imageView11);
        image[11] = (Button) dialog.getCustomView().findViewById(R.id.imageView12);

        seekbar1 = (Slider) dialog.getCustomView().findViewById(R.id.seekBar1);
        seekbar2 = (Slider) dialog.getCustomView().findViewById(R.id.seekBar2);
        textx = (TextView) dialog.getCustomView().findViewById(R.id.textx);
        texty = (TextView) dialog.getCustomView().findViewById(R.id.texty);
        seekbar2.setOnPositionChangeListener(new Slider.OnPositionChangeListener() {
            @Override
            public void onPositionChanged(Slider slider, boolean b, float v, float v1, int i, int i1) {
                texty.setText("字号：" + i1);
//                ed.putInt(Constant.KEY_TEXTSIZE, seekbar2.getValue()).apply();

            }
        });
        seekbar1.setOnPositionChangeListener(new Slider.OnPositionChangeListener() {
            @Override
            public void onPositionChanged(Slider slider, boolean b, float v, float v1, int i, int i1) {
                textx.setText("列数：" + i1);
//                ed.putInt(Constant.KEY_COLUMENUMBER, seekbar1.getValue()).apply();
            }
        });

        for (int i = 0; i < image.length; i++) {
            image[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GameActivity.this.v = (Button) v;
                    new DialogColor().show(GameActivity.this, selectedColorIndex);
                }
            });
        }

        colorbutton = (Button) dialog.getCustomView().findViewById(R.id.buttoncolor);


        boolean isDefault = sp.getBoolean(Constant.KEY_DEFAULT, false);

        if (isDefault) {
            for (int i = 0; i < image.length; i++) {
                //这里得用局部变量
                ShapeDrawable coloredCircle = new ShapeDrawable(new OvalShape());
                coloredCircle.getPaint().setColor(Constant.color[i + 1]);
                image[i].setBackgroundDrawable(coloredCircle);
                image[i].setHighlightColor(Constant.color[i + 1]);
            }
            //这里默认清了edittext
            for (int i = 0; i < editTexts.length; i++) {
                editTexts[i].setText("");
            }
            textx.setText("列数：" + numbers);
            texty.setText("字号：" + textsize);
            seekbar1.setValue((numbers), true);
            seekbar2.setValue((textsize), true);
            colorbutton.setTextColor(textcolor);
//            colorbutton.setBackgroundColor(textcolor);
        } else {
            for (int i = 0; i < image.length; i++) {
                //这里得用局部变量
                ShapeDrawable coloredCircle = new ShapeDrawable(new OvalShape());
                coloredCircle.getPaint().setColor(sp.getInt(Constant.KEY_COLORARRAY + (i + 1), Constant.color[i]));
                image[i].setBackgroundDrawable(coloredCircle);
                image[i].setHighlightColor(sp.getInt(Constant.KEY_COLORARRAY + (i + 1), Constant.color[i]));
            }

            for (int i = 0; i < editTexts.length; i++) {
                editTexts[i].setText(sp.getString(Constant.KEY_EDITARRAY + (i + 1), ""));
            }

            int n = sp.getInt(Constant.KEY_COLUMENUMBER, 5);
            int m = sp.getInt(Constant.KEY_TEXTSIZE, 20);
            textx.setText("列数：" + n);
            texty.setText("字号：" + m);
            seekbar1.setValue((n), true);
            seekbar2.setValue((m), true);
            colorbutton.setTextColor(sp.getInt(Constant.KEY_TEXTCOLOR, Color.DKGRAY));
//            colorbutton.setBackgroundColor(sp.getInt(Constant.KEY_TEXTCOLOR, Color.DKGRAY));
        }


        dialog.show();
    }


    public int getBestScore() {
        return sp.getInt(Constant.KEY_HIGHSCORE, 0);
    }

    int selectedColorIndex = -1;

    @Override
    public void onColorSelection(int index, int color, int darker) {
        if (v != null) {
            ShapeDrawable coloredCircle = new ShapeDrawable(new OvalShape());
            coloredCircle.getPaint().setColor(color);
            v.setBackgroundDrawable(coloredCircle);
            v.setHighlightColor(color);
            v = null;
        } else {
            selectedColorIndex = index;
            try {
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (colorbutton != null) {
                colorbutton.setTextColor(color);
            }
            ed.putInt(Constant.KEY_TEXTCOLOR, color);
            ed.apply();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(darker);
                getWindow().setNavigationBarColor(color);
            }
        }
    }


    //头像序列的监听器
    @Override
    public void onClick(View v) {
        if (v.getTag() != null) {
            index = (Integer) v.getTag();
//            dialog.dismiss();
        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/jpeg");
//        if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.KITKAT){
//            startActivityForResult(intent, SELECT_PIC_KITKAT);
        startActivityForResult(intent, 2);
//        this.v = (Button) v;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            Uri imageFileUri = data.getData();//获取选择图片的URI
            Bitmap b = Tools.centerSquareScaleBitmap(BitmapFactory.decodeFile(Tools.getRealFilePath(this, imageFileUri)),
                    (int) (Tools.getWidth(this) / 4.5));//经测试发现4.5最好
            Bitmap round = Tools.getRoundedImage(b);
//          Tools.saveBitmap(b, Environment.getExternalStorageDirectory().getAbsolutePath() + "/", "test" + (index+1));
            Tools.saveBitmap(this, round, "test" + (index + 1));
            icon[index].setImageBitmap(round);
        }
    }


       @Override
       public boolean onCreateOptionsMenu(Menu menu) {
           super.onCreateOptionsMenu(menu);
           menu.add(0, 1, 1, "反馈").setIcon(R.drawable.icon).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
           return true;
       }

       @Override
       public boolean onOptionsItemSelected(MenuItem item) {
           super.onOptionsItemSelected(item);
           if (item.getItemId() == 1) {
           feedback(null);
           }
           return true;
       }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AnimUtil.animBackSlideFinish(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
