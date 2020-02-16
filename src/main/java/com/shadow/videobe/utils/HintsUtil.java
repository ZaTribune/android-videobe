package com.shadow.videobe.utils;

import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.joanfuentes.hintcase.HintCase;
import com.joanfuentes.hintcase.ShapeAnimator;
import com.joanfuentes.hintcaseassets.contentholderanimators.FadeInContentHolderAnimator;
import com.joanfuentes.hintcaseassets.contentholderanimators.FadeOutContentHolderAnimator;
import com.joanfuentes.hintcaseassets.hintcontentholders.SimpleHintContentHolder;
import com.joanfuentes.hintcaseassets.shapeanimators.RevealCircleShapeAnimator;
import com.joanfuentes.hintcaseassets.shapes.CircularShape;
import com.shadow.videobe.R;

public abstract class HintsUtil {

    public static void createFragmentHint(Fragment fragment,View rootView, int targetView, String title, String content){
        SimpleHintContentHolder hintBlock = new SimpleHintContentHolder.Builder(rootView.getContext())
                .setContentTitle(title)
                .setTitleStyle(R.style.HintStyleTitle)
                .setContentStyle(R.style.HintStyleContent)
                .setContentText(content)
                .build();
        HintCase hintCase=new HintCase(rootView)
                .setTarget(rootView.findViewById(targetView), new CircularShape(), HintCase.TARGET_IS_CLICKABLE,R.dimen.five)
                .setCloseOnTouchView(true)
                .setBackgroundColor(rootView.getContext().getResources().getColor(R.color.black_faded0))
                .setShapeAnimators(new RevealCircleShapeAnimator(), ShapeAnimator.NO_ANIMATOR)
                .setHintBlock(hintBlock, new FadeInContentHolderAnimator(), new FadeOutContentHolderAnimator());
        OnBackPressedCallback onBackPressedCallback=new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                    hintCase.hide();
            }
        };
        hintCase.setOnClosedListener(onBackPressedCallback::remove);
        fragment.getActivity().getOnBackPressedDispatcher().addCallback(fragment.getViewLifecycleOwner(),onBackPressedCallback);
        hintCase.show();
    }
}
