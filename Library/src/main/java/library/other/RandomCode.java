package library.other;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import library.util.FileUtil;
import library.util.StringUtil;

//获取一次验证码并取值
// iv_showCode = (ImageView) findViewById(R.id.iv_showCode);
// iv_showCode.setImageBitmap(RandomCode.getInstance().createBitmap());
// realCode = RandomCode.getInstance().getCode();
public class RandomCode
{

    private static final char[] CHARS = {
            '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm',
            'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    private static RandomCode bmpCode;

    public static RandomCode getInstance ()
    {
        if ( bmpCode == null )
            bmpCode = new RandomCode ();
        return bmpCode;
    }

    //default settings  
    private static final int DEFAULT_CODE_LENGTH = 4;
    private static final int DEFAULT_FONT_SIZE = 25;
    private static final int DEFAULT_LINE_NUMBER = 2;
    private static final int BASE_PADDING_LEFT = 10, RANGE_PADDING_LEFT = 15, BASE_PADDING_TOP = 15, RANGE_PADDING_TOP = 20;
    private static final int DEFAULT_WIDTH = 100, DEFAULT_HEIGHT = 40;

    //settings decided by the layout xml  
    //canvas width and height  
    private int width = DEFAULT_WIDTH, height = DEFAULT_HEIGHT;

    //random word space and pading_top  
    private int base_padding_left = BASE_PADDING_LEFT, range_padding_left = RANGE_PADDING_LEFT,
            base_padding_top = BASE_PADDING_TOP, range_padding_top = RANGE_PADDING_TOP;

    //number of chars, lines; font size  
    private int codeLength = DEFAULT_CODE_LENGTH, line_number = DEFAULT_LINE_NUMBER, font_size = DEFAULT_FONT_SIZE;

    //variables  
    private String code;
    private int padding_left, padding_top;
    private Random random = new Random ();

    public Bitmap createBitmap ()
    {
        padding_left = 0;

        Bitmap bp = Bitmap.createBitmap (width, height, Config.ARGB_8888);
        Canvas c = new Canvas (bp);

        code = createCode ();

        c.drawColor (Color.WHITE);
        Paint paint = new Paint ();
        paint.setTextSize (font_size);

        for (int i = 0; i < code.length (); i++)
        {
            randomTextStyle (paint);
            randomPadding ();
            c.drawText (code.charAt (i) + "", padding_left, padding_top, paint);
        }

        for (int i = 0; i < line_number; i++)
        {
            drawLine (c, paint);
        }

        c.save (Canvas.ALL_SAVE_FLAG);
        c.restore ();//
        return bp;
    }

    public String getCode ()
    {
        return code;
    }


    private String createCode ()
    {
        StringBuilder buffer = new StringBuilder ();
        for (int i = 0; i < codeLength; i++)
        {
            buffer.append (CHARS[random.nextInt (CHARS.length)]);
        }
        return buffer.toString ();
    }

    private void drawLine (Canvas canvas, Paint paint)
    {
        int color = randomColor ();
        int startX = random.nextInt (width);
        int startY = random.nextInt (height);
        int stopX = random.nextInt (width);
        int stopY = random.nextInt (height);
        paint.setStrokeWidth (1);
        paint.setColor (color);
        canvas.drawLine (startX, startY, stopX, stopY, paint);
    }

    private int randomColor ()
    {
        return randomColor (1);
    }

    private int randomColor (int rate)
    {
        int red = random.nextInt (256) / rate;
        int green = random.nextInt (256) / rate;
        int blue = random.nextInt (256) / rate;
        return Color.rgb (red, green, blue);
    }

    private void randomTextStyle (Paint paint)
    {
        int color = randomColor ();
        paint.setColor (color);
        paint.setFakeBoldText (random.nextBoolean ());
        float skewX = random.nextInt (11) / 10;
        skewX = random.nextBoolean () ? skewX : -skewX;
        paint.setTextSkewX (skewX);
        // paint.setUnderlineText(true);
        // paint.setStrikeThruText(true);
    }

    private void randomPadding ()
    {
        padding_left += base_padding_left + random.nextInt (range_padding_left);
        padding_top = base_padding_top + random.nextInt (range_padding_top);
    }

    public static String geFileFromAssets(Context context, String fileName) {
        if (context == null || StringUtil.isEmpty(fileName)) {
            return null;
        }

        StringBuilder s = new StringBuilder("");
        try {
            InputStreamReader in = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader br = new BufferedReader(in);
            String line;
            while ((line = br.readLine()) != null) {
                s.append(line);
            }
            return s.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String geFileFromRaw(Context context, int resId) {
        if (context == null) {
            return null;
        }

        StringBuilder s = new StringBuilder();
        try {
            InputStreamReader in = new InputStreamReader(context.getResources().openRawResource(resId));
            BufferedReader br = new BufferedReader(in);
            String line;
            while ((line = br.readLine()) != null) {
                s.append(line);
            }
            return s.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> geFileToListFromAssets(Context context, String fileName) {
        if (context == null || StringUtil.isEmpty (fileName)) {
            return null;
        }

        List<String> fileContent = new ArrayList<String>();
        try {
            InputStreamReader in = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader br = new BufferedReader(in);
            String line;
            while ((line = br.readLine()) != null) {
                fileContent.add(line);
            }
            br.close();
            return fileContent;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> geFileToListFromRaw(Context context, int resId) {
        if (context == null) {
            return null;
        }

        List<String> fileContent = new ArrayList<String> ();
        BufferedReader reader = null;
        try {
            InputStreamReader in = new InputStreamReader(context.getResources().openRawResource(resId));
            reader = new BufferedReader(in);
            String line = null;
            while ((line = reader.readLine()) != null) {
                fileContent.add(line);
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // IterateFile(new File(pathString));
    // 文件名和显示不同，需要实体

    public void IterateFile(File file) {
        final List<String> mVoicesList = new ArrayList<String>();
        String filepath;
        String filename;
        FileUtil.isDirExistAndCreat(file);
        mVoicesList.removeAll(mVoicesList);
        for (File filex : file.listFiles()) {
            if (filex.isDirectory()) {
                IterateFile(filex);
            } else {
                filepath = file.getPath();
                filename = filex.getName();
                if (filename.endsWith(".mp3")) {
                    mVoicesList.add(filepath + "/" + filename);
                }
            }
        }
    }
}  
