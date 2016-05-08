package org.loader.mdtab;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * Created by qibin on 16-5-7.
 */
public class TabBehavior extends CoordinatorLayout.Behavior<View> {
    private TranslateAnimation mAnimation;

    public TabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,
                                       View child, View directTargetChild,
                                       View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout,
                                  View child, View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        if(dy > 0) {
            if(child.getVisibility() == View.GONE) return;
            startAnim(child, 0, child.getMeasuredHeight());
        } else {
            if(child.getVisibility() == View.VISIBLE) return;
            startAnim(child, child.getMeasuredHeight(), 0);
        }
    }

    private void startAnim(final View child, final int startY, int endY) {
        child.clearAnimation();
        mAnimation = new TranslateAnimation(0.f, 0.f, startY, endY);
        mAnimation.setDuration(500);
        mAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                if(startY == 0) child.setVisibility(View.GONE);
                else child.setVisibility(View.VISIBLE);
                mAnimation = null;
            }

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        child.startAnimation(mAnimation);
    }
}
