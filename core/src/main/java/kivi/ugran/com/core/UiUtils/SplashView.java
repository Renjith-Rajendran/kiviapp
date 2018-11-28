package kivi.ugran.com.core.UiUtils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import java.lang.annotation.Retention;

import kivi.ugran.com.core.R;
import kivi.ugran.com.core.callbacks.SimpleCallback;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class SplashView extends ConstraintLayout {
    private ImageView splashLogo;
    private AnimatorSet splashLogoInAnimatorSet = new AnimatorSet();
    private AnimatorSet splashLogoOutAnimatorSet = new AnimatorSet();

    protected @AnimationState int currentAnimationState = ANIMATION_NOT_STARTED;
    protected AnimationStateChangeCallback animationStateChangeCallback;

    public SplashView(@NonNull Context context) {
        this(context, null);
    }

    public SplashView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SplashView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        setupAnimations();
    }

    private void initView(Context context) {
        inflate(context, R.layout.kivi_splash_view, this);
        splashLogo = findViewById(R.id.splash_logo);
    }

    private void setupAnimations() {
        final int animateInDurationInMillis = 2000;
        final int animateOutDurationInMillis =1500;

        final ObjectAnimator fadeLogoInAlphaAnimator = ObjectAnimator.ofFloat(splashLogo, View.ALPHA, 0f, 1f);
        final ObjectAnimator moveLogoUpAnimator = ObjectAnimator.ofFloat(splashLogo, View.TRANSLATION_Y, 50f, 0f);

        splashLogoInAnimatorSet.setInterpolator(new AccelerateInterpolator());
        splashLogoInAnimatorSet.setDuration(animateInDurationInMillis);
        splashLogoInAnimatorSet.play(fadeLogoInAlphaAnimator).with(moveLogoUpAnimator);

        final ObjectAnimator fadeLogoOutAlphaAnimator = ObjectAnimator.ofFloat(splashLogo, View.ALPHA, 1f, 0f);
        final ObjectAnimator moveLogoOutAnimator = ObjectAnimator.ofFloat(splashLogo, View.TRANSLATION_Y, 0f, 50f);
        final ObjectAnimator fadeSplashScreenOutAlphaAnimator = ObjectAnimator.ofFloat(this, View.ALPHA, 1f, 0f);

        splashLogoOutAnimatorSet.setDuration(animateOutDurationInMillis);
        splashLogoOutAnimatorSet.setInterpolator(new AccelerateInterpolator());
        splashLogoOutAnimatorSet.play(fadeLogoOutAlphaAnimator)
                .with(moveLogoOutAnimator)
                .with(fadeSplashScreenOutAlphaAnimator);
        //final ObjectAnimator fadeSplashScreenInAlphaAnimator =
        //        ObjectAnimator.ofFloat(this, View.ALPHA, 0f, 1f).setDuration(animateOutDurationInMillis);
    }

    /**
     * Kivi logo 'in' animation starting.
     */

    public void animateLogoIn(@NonNull final SimpleCallback callback) {
        splashLogoInAnimatorSet.addListener(new AnimatorListenerAdapter() {

            @Override public void onAnimationCancel(Animator animation) {
                int oldState = currentAnimationState;
                currentAnimationState = ANIMATION_INTERRUPTED;
                //animationStateChangeCallback.ifPresent(c -> c.animationStateChanged(oldState, currentAnimationState));
                animationStateChangeCallback.animationStateChanged(oldState, currentAnimationState);
            }

            @Override public void onAnimationPause(Animator animation) {
                int oldState = currentAnimationState;
                currentAnimationState = ANIMATION_INTERRUPTED;
                animationStateChangeCallback.animationStateChanged(oldState, currentAnimationState);
            }

            @Override public void onAnimationResume(Animator animation) {
                int oldState = currentAnimationState;
                currentAnimationState = ANIMATION_RUNNING;
                animationStateChangeCallback.animationStateChanged(oldState, currentAnimationState);
            }

            @Override public void onAnimationStart(Animator animation) {
                int oldState = currentAnimationState;
                currentAnimationState = ANIMATION_RUNNING;
                animationStateChangeCallback.animationStateChanged(oldState, currentAnimationState);
            }

            @Override public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                int oldState = currentAnimationState;
                currentAnimationState = ANIMATION_COMPLETE_IN;
                animationStateChangeCallback.animationStateChanged(oldState, currentAnimationState);
                callback.callback();
            }
        });

        splashLogoInAnimatorSet.start();
    }

    /**
     * Kivi logo 'out' animation starting.
     */
    public void animateLogoOut(@NonNull final SimpleCallback callback) {
        splashLogoOutAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationCancel(Animator animation) {
                int oldState = currentAnimationState;
                currentAnimationState = ANIMATION_INTERRUPTED;
                animationStateChangeCallback.animationStateChanged(oldState, currentAnimationState);
            }

            @Override public void onAnimationPause(Animator animation) {
                int oldState = currentAnimationState;
                currentAnimationState = ANIMATION_INTERRUPTED;
                animationStateChangeCallback.animationStateChanged(oldState, currentAnimationState);
            }

            @Override public void onAnimationResume(Animator animation) {
                int oldState = currentAnimationState;
                currentAnimationState = ANIMATION_RUNNING;
                animationStateChangeCallback.animationStateChanged(oldState, currentAnimationState);
            }

            @Override public void onAnimationStart(Animator animation) {
                int oldState = currentAnimationState;
                currentAnimationState = ANIMATION_RUNNING;
                animationStateChangeCallback.animationStateChanged(oldState, currentAnimationState);
            }

            @Override public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                int oldState = currentAnimationState;
                currentAnimationState = ANIMATION_COMPLETE_OUT;
                animationStateChangeCallback.animationStateChanged(oldState, currentAnimationState);
                callback.callback();
            }
        });

        splashLogoOutAnimatorSet.start();
    }

    public void removeCallbacks() {
        animationStateChangeCallback = null;
        splashLogoInAnimatorSet.removeAllListeners();
        splashLogoOutAnimatorSet.removeAllListeners();
    }

    public interface AnimationStateChangeCallback {
        void animationStateChanged(@AnimationState int oldState, @AnimationState int newState);
    }

    public void setAnimationStateChangeCallback(@NonNull AnimationStateChangeCallback callback) {
        animationStateChangeCallback = callback;
    }

    public @AnimationState int animationState() {
        return currentAnimationState;
    }

    @Retention(SOURCE) @IntDef({
            ANIMATION_NOT_STARTED, ANIMATION_RUNNING, ANIMATION_INTERRUPTED, ANIMATION_COMPLETE_IN,
            ANIMATION_COMPLETE_OUT
    }) public @interface AnimationState {
    }

    public static final int ANIMATION_NOT_STARTED = 0;
    public static final int ANIMATION_RUNNING = 1;
    public static final int ANIMATION_INTERRUPTED = 2;
    public static final int ANIMATION_COMPLETE_IN = 3;
    public static final int ANIMATION_COMPLETE_OUT = 4;
}
