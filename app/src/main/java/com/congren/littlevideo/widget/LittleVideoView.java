package com.congren.littlevideo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.congren.littlevideo.R;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

/**
 * @author 几圈年轮
 * @date 2019/12/18.
 * description：
 */
public class LittleVideoView extends StandardGSYVideoPlayer {

    public LittleVideoView(Context context, Boolean fullFlag) {
        super(context, fullFlag);

        init();
    }

    public LittleVideoView(Context context) {
        super(context);

        init();
    }

    public LittleVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    @Override
    public int getLayoutId() {
        return R.layout.empty_control_video;
    }

    @Override
    protected void changeUiToNormal() {
        
    }

    @Override
    protected void changeUiToPlayingShow() {

    }

    private void init() {
        mTextureViewContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getGSYVideoManager().isPlaying()) {
                    getGSYVideoManager().pause();
                    mStartButton.setVisibility(View.VISIBLE);
                } else {
                    getGSYVideoManager().start();
                    mStartButton.setVisibility(View.GONE);
                }
            }
        });
    }
}
