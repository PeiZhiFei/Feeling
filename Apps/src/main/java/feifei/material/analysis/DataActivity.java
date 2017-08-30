package feifei.material.analysis;


import feifei.material.main.BaseActivity;

public abstract class DataActivity extends BaseActivity {
    public float[] orderCount7 = new float[7];
    public float[] orderMoney7 = new float[7];
    public String[] orderTime7 = new String[7];

    public float[] orderCount30 = new float[30];
    public float[] orderMoney30 = new float[30];
    public String[] orderTime30 = new String[30];

    protected String todayCount = "……";
    protected String todayMoney = "……";
    protected String allStore = "……";
    protected String allCount = "……";
    protected String allMoney = "……";

    protected void getData(boolean first) {
        //原来是这样的
        getTodayData();
        get30Data(first);
    }

    public static final String URL = "";

    public void getTodayData() {
        getData("simple");
    }

    public void get30Data(boolean first) {
        getData("30");
    }

    protected void getData(String tag) {

        switch (tag) {
            case "simple":
                todayCount = "13";
                todayMoney = "530";
                allStore = "25";
                allCount = "17654";
                allMoney = "438564";
                onResult(1);
                break;
            case "30":
                for (int i = 0; i < orderCount7.length; i++) {
                    orderCount7[i] = 5 + i * i * i;
                    orderMoney7[i] = 1000 + i * i * i + 3;
                    orderTime7[i] = "090" + i;
                }
                for (int i = 0; i < orderCount30.length; i++) {
                    orderCount30[i] = 5 + i * i * i;
                    orderMoney30[i] = 1000 + i * i * i + 3;
                    orderTime30[i] = "090" + i;
                }
                onResult(2);
                break;
        }
    }

    protected abstract void onResult(int type);
}
