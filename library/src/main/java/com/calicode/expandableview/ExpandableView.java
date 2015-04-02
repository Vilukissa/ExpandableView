package com.calicode.expandableview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.ViewStub.OnInflateListener;
import android.widget.LinearLayout;

public class ExpandableView extends LinearLayout implements OnClickListener {

    public interface OnExpandableViewInflatedListener {
        void headerInflated(View headerView);
        void expandAreaInflated(View expandView);
    }

    private static final String TAG = "ExpandableView";

    private View mHeaderLayout;
    private View mExpandLayout;

    private int mHeaderHeight;
    private int mExpandHeight;

    private boolean mExpanded;
    private boolean mExpandingOrCollapsing;

    private ViewStub mHeaderStub;
    private ViewStub mExpandStub;

    private OnExpandableViewInflatedListener mListener;

    public ExpandableView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        if (!isInEditMode()) {
            setOrientation(VERTICAL);
            setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            inflate(context, R.layout.expand_view, this);

            mHeaderStub = (ViewStub) findViewById(R.id.headerStub);
            mExpandStub = (ViewStub) findViewById(R.id.expandAreaStub);

            mHeaderStub.setOnInflateListener(new OnInflateListener() {
                @Override
                public void onInflate(ViewStub viewStub, View view) {
                    mHeaderLayout = view;
                    if (mListener != null) {
                        mListener.headerInflated(view);
                    }
                }
            });
            mExpandStub.setOnInflateListener(new OnInflateListener() {
                @Override
                public void onInflate(ViewStub viewStub, View view) {
                    mExpandLayout = view;
                    if (mListener != null) {
                        mListener.expandAreaInflated(view);
                    }
                }
            });

            setOnClickListener(this);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mHeaderLayout != null && mExpandLayout != null && (mHeaderHeight == 0 || mExpandHeight == 0)) {
            // Root view's calculations
            int viewTopPadding = getPaddingTop();
            int viewBottomPadding = getPaddingBottom();

            // Header view's calculations
            int headerTopMargin = ((LayoutParams) mHeaderLayout.getLayoutParams()).topMargin;
            int headerBottomMargin = ((LayoutParams) mHeaderLayout.getLayoutParams()).bottomMargin;


            // Include root view's top and bottom padding, header's top margin and header's bottom margin.
            // (header's padding is already include in height)
            mHeaderHeight = mHeaderLayout.getMeasuredHeight()
                    + viewTopPadding + viewBottomPadding
                    + headerTopMargin + headerBottomMargin;

            // Expand area view's calculations
            int expandAreaTopMargin = ((LayoutParams) mExpandLayout.getLayoutParams()).topMargin;
            int expandAreaBottomMargin = ((LayoutParams) mExpandLayout.getLayoutParams()).bottomMargin;

            // Include only top and bottom margin
            mExpandHeight = mExpandLayout.getMeasuredHeight()
                    + expandAreaTopMargin + expandAreaBottomMargin;

            // Set view's height to match the collapsed height as the default state is collapsed
            getLayoutParams().height = mHeaderHeight;

            Log.e(TAG, "Header h: " + mHeaderHeight + " Expand h: " + mExpandHeight);
        }
    }

    @Override
    public void onClick(View v) {
        if (mExpanded) {
            collapse();
        } else {
            expand();
        }
    }

    public void expand() {
        if (!mExpanded && !mExpandingOrCollapsing) {
            Log.e(TAG, "Expanding");
            startAnimation();
        }
    }

    public void collapse() {
        if (mExpanded && !mExpandingOrCollapsing) {
            Log.e(TAG, "Collapsing");
            startAnimation();
        }
    }

    private void startAnimation() {
        mExpandingOrCollapsing = true;
        final ValueAnimator anim = ValueAnimator.ofFloat(0, mExpandHeight);
        anim.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int animationPosition = ((Float) anim.getAnimatedValue()).intValue();
                getLayoutParams().height = mExpanded
                        ? mExpandHeight + mHeaderHeight - animationPosition
                        : mHeaderHeight + animationPosition;
                requestLayout();
            }
        });

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mExpandingOrCollapsing = false;
                mExpanded = !mExpanded;
            }
        });

        anim.start();
    }

    public void setHeaderLayoutId(int headerLayoutId) {
        mHeaderStub.setLayoutResource(headerLayoutId);
        mHeaderStub.inflate();
    }

    public void setExpandLayoutId(int expandLayoutId) {
        mExpandStub.setLayoutResource(expandLayoutId);
        mExpandStub.inflate();
    }

    public void setListener(OnExpandableViewInflatedListener listener) {
        mListener = listener;
    }
}
