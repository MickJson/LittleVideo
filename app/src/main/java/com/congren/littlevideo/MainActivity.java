package com.congren.littlevideo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseViewHolder;
import com.congren.littlevideo.adapter.LittleVideoAdapter;
import com.congren.littlevideo.bean.VideoBean;
import com.congren.littlevideo.widget.LittleVideoView;
import com.congren.littlevideo.widget.PagerLayoutManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;

import java.util.List;

/**
 * @author 几圈年轮
 */
public class MainActivity extends AppCompatActivity implements PagerLayoutManager.OnPageChangedListener {

    /**
     * 预加载条目数
     */
    private static final int DEFAULT_PRELOAD_NUMBER = 5;

    private SwipeRefreshLayout mRefreshView;
    private RecyclerView mRvLittleVideo;

    private LittleVideoAdapter mLittleVideoAdapter;
    private PagerLayoutManager mPagerLayoutManager;
    private int mCurrentPosition;
    private GSYVideoOptionBuilder mGsySmallVideoHelperBuilder;
    private LittleVideoView mVideoView;

    /**
     * 是否正在加载数据
     */
    private boolean isLoadingData = false;
    /**
     * 是否加载完毕
     */
    private boolean isEnd;
    /**
     * 是否是最后视频位置
     */
    private int mLastStopPosition;
    /**
     * 数据请求是否为加载更多数据
     */
    private boolean isLoadMoreData = false;
    /**
     * 请求视频内容页下标
     */
    private int mLastProductIndex;

    /**
     * 模拟网络请求完毕，数据更新
     */
    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            onVideoListUpdate(VideoBean.getTikTokVideoList());
            return false;
        }
    });
    private boolean isLoopPlay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initVideo();
        initListener();
    }

    private void initView() {
        mRvLittleVideo = findViewById(R.id.rv_little_video);
        mRefreshView = findViewById(R.id.srf_video_list);
    }


    private void initData() {
        mPagerLayoutManager = new PagerLayoutManager(this);
        mPagerLayoutManager.setOnPageChangedListener(this);

        mRvLittleVideo.setLayoutManager(mPagerLayoutManager);

        mLittleVideoAdapter = new LittleVideoAdapter();
        mRvLittleVideo.setAdapter(mLittleVideoAdapter);

        requestNewData();
    }

    /**
     * 初始化播放器内容，采用了GSY播放器
     */
    private void initVideo() {

        mVideoView = new LittleVideoView(this);
        GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL);
        mGsySmallVideoHelperBuilder = new GSYVideoOptionBuilder();
        mGsySmallVideoHelperBuilder
                .setLooping(isLoopPlay)
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

                    @Override
                    public void onAutoComplete(String url, Object... objects) {
                        super.onAutoComplete(url, objects);

                        if (!isLoopPlay) {
                            if (mCurrentPosition + 1 < mLittleVideoAdapter.getItemCount() ) {
                                mRvLittleVideo.smoothScrollToPosition(mCurrentPosition + 1);
                            }
                        }

                    }
                });
    }

    /**
     * 设置数据刷新监听,回归初始状态
     */
    private void initListener() {
        mRefreshView.setOnRefreshListener(() -> {
            isLoadMoreData = false;
            mLastProductIndex = 0;
            requestNewData();
        });
    }

    /**
     * 数据请求
     */
    private void requestNewData() {
        if (!isLoadMoreData) {
            mRefreshView.setRefreshing(true);
        }

        isLoadingData = true;

        // 模拟网络请求，2秒后进行数据返回
        Log.e("PageIndex", String.valueOf(mLastProductIndex));
        mHandler.sendEmptyMessageDelayed(0, 2000);

    }

    /**
     * 数据更新
     *
     * @param videoList 网络回调获取数据
     */
    public void onVideoListUpdate(List<VideoBean> videoList) {
        isEnd = videoList == null || videoList.size() < 10;
        isLoadingData = false;
        if (mRefreshView != null && mRefreshView.isRefreshing()) {
            mRefreshView.setRefreshing(false);
        }
        if (videoList == null) {
            return;
        }
        mLastProductIndex += videoList.size();
        if (isLoadMoreData) {
            // 加载更多数据
            if (mLittleVideoAdapter != null) {
                mLittleVideoAdapter.addData(videoList);
            }
        } else {
            // 刷新数据
            isEnd = false;
            mLittleVideoAdapter.setNewData(videoList);
        }
    }

    /**
     * 初始化加载完成,进行视频播放
     */
    @Override
    public void onPageInitComplete() {
        int position = mPagerLayoutManager.findFirstVisibleItemPosition();
        if (position != -1) {
            mCurrentPosition = position;
        }

        // 预加载，请求数据内容
        int itemCount = mLittleVideoAdapter.getItemCount();
        if (itemCount - position < DEFAULT_PRELOAD_NUMBER && !isLoadingData && !isEnd) {
            requestNewData();
        }

        startPlay(mCurrentPosition);

        mLastStopPosition = -1;
    }

    /**
     * 页面脱离，内容释放
     *
     * @param position 子布局在RecyclerView位置
     * @param isNext   是否有下一个
     */
    @Override
    public void onPageRelease(int position, boolean isNext) {
        if (mCurrentPosition == position) {
            mLastStopPosition = position;
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

    /**
     * 页面附着，内容展示
     *
     * @param position 子布局在RecyclerView位置
     * @param isLast   是否最后一个
     */
    @Override
    public void onPageSelected(int position, boolean isLast) {
        if (mCurrentPosition == position && mLastStopPosition != position) {
            return;
        }

        // 预加载，请求数据内容
        int itemCount = mLittleVideoAdapter.getItemCount();
        if (itemCount - position < DEFAULT_PRELOAD_NUMBER && !isLoadingData && !isEnd) {
            // 正在加载中, 防止网络太慢或其他情况造成重复请求列表
            isLoadMoreData = true;
            isLoadingData = true;
            requestNewData();
        }
        if (itemCount == position + 1 && isEnd) {
            Toast.makeText(MainActivity.this, "No more video.", Toast.LENGTH_SHORT).show();
        }
        startPlay(position);
        mCurrentPosition = position;
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
