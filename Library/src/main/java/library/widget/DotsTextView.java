package library.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ReplacementSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import my.library.R;


public class DotsTextView extends TextView {

    private JumpingSpan dotOne;
    private JumpingSpan dotTwo;
    private JumpingSpan dotThree;

    private int showSpeed = 700;

    private int jumpHeight;
    private boolean autoPlay;
    private boolean isPlaying;
    private boolean isHide;
    private int period;
    private long startTime;

    private boolean lockDotOne;
    private boolean lockDotTwo;
    private boolean lockDotThree;

    private Handler handler;
    private AnimatorSet mAnimatorSet = new AnimatorSet();
    private float textWidth;

    public DotsTextView(Context context) {
        super(context);
        init(context, null);
    }

    public DotsTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DotsTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        handler = new Handler(Looper.getMainLooper());
        //自定义属性
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaitingDots);
            period = typedArray.getInt(R.styleable.WaitingDots_period, 6000);
            jumpHeight = typedArray.getInt(R.styleable.WaitingDots_jumpHeight, (int) (getTextSize() / 4));
            autoPlay = typedArray.getBoolean(R.styleable.WaitingDots_autoplay, true);
            typedArray.recycle();
        }
        dotOne = new JumpingSpan();
        dotTwo = new JumpingSpan();
        dotThree = new JumpingSpan();
        //将每个点设置为jumpingSpan类型
        SpannableString spannable = new SpannableString("...");
        spannable.setSpan(dotOne, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(dotTwo, 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(dotThree, 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        setText(spannable, BufferType.SPANNABLE);

        textWidth = getPaint().measureText(".", 0, 1);
        //一下两个是把updateListener加到点1上，通过它来进行刷新动作
        ObjectAnimator dotOneJumpAnimator = createDotJumpAnimator(dotOne, 0);
        dotOneJumpAnimator.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                invalidate();
            }
        });
        //这里通过animationSet来控制三个点的组合动作
        mAnimatorSet.playTogether(dotOneJumpAnimator, createDotJumpAnimator(dotTwo,
                period / 6), createDotJumpAnimator(dotThree, period * 2 / 6));

        isPlaying = autoPlay;
        if (autoPlay) {
            start();
        }
    }

    public void start() {
        isPlaying = true;
        //一旦开始就INFINITE
        setAllAnimationsRepeatCount(ValueAnimator.INFINITE);
        mAnimatorSet.start();
    }

    /*动画的实现核心
    *@param jumpingSpan 传入点，
    * @delay 动画运行延迟，通过这个参数让三个点进行有时差的运动
     */
    private ObjectAnimator createDotJumpAnimator(JumpingSpan jumpingSpan, long delay) {
        ObjectAnimator jumpAnimator = ObjectAnimator.ofFloat(jumpingSpan, "translationY", 0, -jumpHeight);
        /*setEvaluator这个重要，功能是为了通过方程来平滑的实现点运动的“节奏感”，可以试试把这段去掉，
            你会发现点会以默认的速度上下运动，特别生硬，TypeEvaluator中的evaluate可以计算出点的当前位置。
            通过对当前点的计算间接设计了点的轨迹运动，和时间插值TimeInterpolator达到相同的效果，就好比你不知道速度但是你知道每秒所在的位置相当于速度了。
            这个计算方法是这样的：可以参见这个博文
            http://blog.csdn.net/serapme/article/details/47006049
            ValueAnimator还封装了一个TypeAnimator，根据开始、结束值与TimeIniterpolator计算得到的值计算出属性值。
            ValueAnimator根据动画已进行的时间跟动画总时间（duration）的比计算出一个时间因子（0~1），然后根据TimeInterpolator计算出另一个因子，最后TypeAnimator通过这个因子计算出属性值，如上例中10ms时：
            首先计算出时间因子，即经过的时间百分比：t=10ms/40ms=0.25
            经插值计算(inteplator)后的插值因子:大约为0.15，上述例子中用了AccelerateDecelerateInterpolator，计算公式为（input即为时间因子）：
            (Math.cos((input + 1) * Math.PI) / 2.0f) + 0.5f;
            最后根据TypeEvaluator计算出在10ms时的属性值：0.15*（40-0）=6pixel。上例中TypeEvaluator为FloatEvaluator，计算方法为 ：
            public Float evaluate(float fraction, Number startValue, Number endValue) {
                    float startFloat = startValue.floatValue();
                    return startFloat + fraction * (endValue.floatValue() - startFloat);
             }
         */
        jumpAnimator.setEvaluator(new TypeEvaluator<Number>() {

            @Override
            public Number evaluate(float fraction, Number from, Number to) {
                return Math.max(0, Math.sin(fraction * Math.PI * 2)) * (to.floatValue() - from.floatValue());
            }
        });
        jumpAnimator.setDuration(period);
        jumpAnimator.setStartDelay(delay);
        jumpAnimator.setRepeatCount(ValueAnimator.INFINITE);
        jumpAnimator.setRepeatMode(ValueAnimator.RESTART);
        return jumpAnimator;
    }

    public void stop() {
        isPlaying = false;
        setAllAnimationsRepeatCount(0);
    }

    private void setAllAnimationsRepeatCount(int repeatCount) {
        for (Animator animator : mAnimatorSet.getChildAnimations()) {
            if (animator instanceof ObjectAnimator) {
                ((ObjectAnimator) animator).setRepeatCount(repeatCount);
            }
        }
    }

    public void hide() {

        createDotHideAnimator(dotThree, 2).start();

        ObjectAnimator dotTwoMoveRightToLeft = createDotHideAnimator(dotTwo, 1);
        dotTwoMoveRightToLeft.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                invalidate();
            }
        });

        dotTwoMoveRightToLeft.start();
        isHide = true;
    }

    public void show() {
        ObjectAnimator dotThreeMoveRightToLeft = createDotShowAnimator(dotThree, 2);

        dotThreeMoveRightToLeft.start();

        ObjectAnimator dotTwoMoveRightToLeft = createDotShowAnimator(dotTwo, 1);
        dotTwoMoveRightToLeft.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                invalidate();
            }
        });

        dotTwoMoveRightToLeft.start();
        isHide = false;
    }

    private ObjectAnimator createDotHideAnimator(JumpingSpan span, float widthMultiplier) {
        return createDotHorizontalAnimator(span, 0, -textWidth * widthMultiplier);
    }

    private ObjectAnimator createDotShowAnimator(JumpingSpan span, int widthMultiplier) {
        return createDotHorizontalAnimator(span, -textWidth * widthMultiplier, 0);
    }

    private ObjectAnimator createDotHorizontalAnimator(JumpingSpan span, float from, float to) {
        ObjectAnimator dotThreeMoveRightToLeft = ObjectAnimator.ofFloat(span, "translationX", from, to);
        dotThreeMoveRightToLeft.setDuration(showSpeed);
        return dotThreeMoveRightToLeft;
    }

    public void showAndPlay() {
        show();
        start();
    }

    public void hideAndStop() {
        hide();
        stop();
    }

    public boolean isHide() {
        return isHide;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setJumpHeight(int jumpHeight) {
        this.jumpHeight = jumpHeight;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    /* ReplacementSpan真是一个挺神奇的东西，在官方api上介绍甚少
     *几个主要功能函数也是do nothing.
     * 本例中自定义的translationX,translationY没有发挥作用。如果给两个变量赋值
     * 那么第一个JumpingSpan距离前面元素距离增大，这里这样使用是为了让每个"."为一个单独的单元进行独立操作
     */
    class JumpingSpan extends ReplacementSpan {

        private float translationX = 0;
        private float translationY = 0;

        @Override
        public int getSize(Paint paint, CharSequence text, int start, int end, FontMetricsInt fontMetricsInt) {
            return (int) paint.measureText(text, start, end);
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
            canvas.drawText(text, start, end, x + translationX, y + translationY, paint);
        }

        public void setTranslationX(float translationX) {
            this.translationX = translationX;
        }

        public void setTranslationY(float translationY) {
            this.translationY = translationY;
        }
    }
}