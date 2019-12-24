package com.congren.littlevideo.widget;

import android.content.Context;
import android.util.AttributeSet;

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
    }

    public LittleVideoView(Context context) {
        super(context);
    }

    public LittleVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.empty_control_video;
    }
}
