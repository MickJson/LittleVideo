package com.congren.littlevideo;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.congren.littlevideo.adapter.LittleVideoAdapter;
import com.congren.littlevideo.widget.LittleVideoView;
import com.congren.littlevideo.widget.PagerLayoutManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;

/**
 * @author 几圈年轮
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView mRvLittleVideo;
    private LittleVideoAdapter mLittleVideoAdapter;
    private PagerLayoutManager mPagerLayoutManager;
    private int mCurrentPosition;
    private GSYVideoOptionBuilder mGsySmallVideoHelperBuilder;
    private LittleVideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initVideo();
    }

    private void initView() {
        mRvLittleVideo = findViewById(R.id.rv_little_video);
    }


    private void initData() {
        mPagerLayoutManager = new PagerLayoutManager(this);
        mPagerLayoutManager.setOnPageChangedListener(new PagerLayoutManager.OnPageChangedListener() {
            @Override
            public void onPageInitComplete() {
                int position = mPagerLayoutManager.findFirstVisibleItemPosition();
                if (position != -1) {
                    mCurrentPosition = position;
                }
                startPlay(mCurrentPosition);
            }

            @Override
            public void onPageRelease(int position, boolean isNext) {
                if (mCurrentPosition == position) {
                    stopPlay();
                    BaseViewHolder viewHolder = (BaseViewHolder) mRvLittleVideo.findViewHolderForLayoutPosition(mCurrentPosition);
                    if (viewHolder != null) {
                        ImageView mVideoThumb = viewHolder.getView(R.id.iv_thumb_item);
                        if (mVideoThumb != null) {
                            mVideoThumb.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onPageSelected(int position, boolean isLast) {
                if (mCurrentPosition == position) {
                    return;
                }
                startPlay(position);
                mCurrentPosition = position;
            }
        });

        mRvLittleVideo.setLayoutManager(mPagerLayoutManager);

        mLittleVideoAdapter = new LittleVideoAdapter();
        mRvLittleVideo.setAdapter(mLittleVideoAdapter);
    }

    /**
     * 初始化播放器内容，采用了GSY播放器
     */
    private void initVideo() {

        mVideoView = new LittleVideoView(this);
        GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL);
        mGsySmallVideoHelperBuilder = new GSYVideoOptionBuilder();
        mGsySmallVideoHelperBuilder
                .setLooping(true)
                .setCacheWithPlay(true)
                .setIsTouchWiget(false)
                .setVideoAllCallBack(new GSYSampleCallBack() {
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        super.onPrepared(url, objects);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                BaseViewHolder viewHolder = (BaseViewHolder) mRvLittleVideo.findViewHolderForLayoutPosition(mCurrentPosition);
                                if (viewHolder != null) {
                                    ImageView mVideoThumb = viewHolder.getView(R.id.iv_thumb_item);
                                    if (mVideoThumb != null) {
                                        mVideoThumb.setVisibility(View.INVISIBLE);
                                    }
                                }
                            }
                        }, 100);


                    }

                });
    }

    /**
     * 停止播放，移除视图
     */
    private void stopPlay() {
        mVideoView.release();
        ViewParent parent = mVideoView.getParent();
        if (parent instanceof FrameLayout) {
            ((FrameLayout) parent).removeView(mVideoView);
        }
    }

    /**
     * 开始播放视频内容，进行播放器视图加载
     */
    private void startPlay(int position) {
        if (position < 0 || position >= mLittleVideoAdapter.getData().size()) {
            return;
        }
        BaseViewHolder holder = (BaseViewHolder) mRvLittleVideo.findViewHolderForLayoutPosition(position);
        ViewParent parent = mVideoView.getParent();
        if (parent instanceof FrameLayout) {
            ((ViewGroup) parent).removeView(mVideoView);
        }
        if (holder != null) {
            FrameLayout mVideoContent = holder.getView(R.id.fl_content_item);
            mVideoContent.addView(mVideoView, 0);
            mGsySmallVideoHelperBuilder.setUrl(mLittleVideoAdapter.getData().get(position).getUrl());
            mGsySmallVideoHelperBuilder.build(mVideoView);
            mVideoView.startPlayLogic();

        }
    }


}
