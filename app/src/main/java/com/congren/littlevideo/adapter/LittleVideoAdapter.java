package com.congren.littlevideo.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.congren.littlevideo.R;
import com.congren.littlevideo.bean.VideoBean;

import java.util.ArrayList;

/**
 * @author 几圈年轮
 * @date 2019/12/18.
 * description：适配器内容
 */
public class LittleVideoAdapter extends BaseQuickAdapter<VideoBean, BaseViewHolder> {

    public LittleVideoAdapter() {
        super(R.layout.littlevideo_recyclerview_item, new ArrayList<>());
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoBean item) {
        Glide.with(mContext).load(item.getThumb()).into((ImageView) helper.getView(R.id.iv_thumb_item));
    }


}
