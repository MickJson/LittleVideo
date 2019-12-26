package com.congren.littlevideo.bean;


import java.util.ArrayList;
import java.util.List;

/**
 * @author 几圈年轮
 */
public class VideoBean {

    private String title;
    private String url;
    private String thumb;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    private VideoBean(String title, String thumb, String url) {
        this.title = title;
        this.url = url;
        this.thumb = thumb;

    }

    /**
     * 抖音演示数据
     */
    public static List<VideoBean> getTikTokVideoList() {
        {
            List<VideoBean> videoList = new ArrayList<>();

            videoList.add(new VideoBean("",
                    "http://xp.qpic.cn/oscar_pic/0/1047_33356432363063372d633237642pict/480.jpg",
                    "http://123.125.244.84/v.weishi.qq.com/shg_1043539964_1047_eae031f17be34dfea6038db252a8vide.f20.mp4"));

            videoList.add(new VideoBean("",
                    "http://xp.qpic.cn/oscar_pic/0/1047_63323637383439642d636634302pict/480",
                    "http://123.125.244.84/v.weishi.qq.com/tjg_660467533_1047_88ae7ef740f14486872229d22dd5vide.f20.mp4"));

            videoList.add(new VideoBean("",
                    "http://xp.qpic.cn/oscar_pic/0/1047_30646239313130382d306330392pict/480",
                    "http://123.125.10.236/v.weishi.qq.com/shg_193622498_1047_65b2a00f51c14956b602b7275757vide.f20.mp4"));

            videoList.add(new VideoBean("",
                    "http://xp.qpic.cn/oscar_pic/0/1047_35653531343037612d653235392pict/480",
                    "http://123.125.244.84/v.weishi.qq.com/shg_1638682606_1047_36ff81db2b394c8faa295f40e04dvide.f20.mp4"));


            videoList.add(new VideoBean("",
                    "http://xp.qpic.cn/oscar_pic/0/1047_33356432363063372d633237642pict/480.jpg",
                    "http://123.125.244.84/v.weishi.qq.com/shg_1043539964_1047_eae031f17be34dfea6038db252a8vide.f20.mp4"));

            videoList.add(new VideoBean("",
                    "http://xp.qpic.cn/oscar_pic/0/1047_63323637383439642d636634302pict/480",
                    "http://123.125.244.84/v.weishi.qq.com/tjg_660467533_1047_88ae7ef740f14486872229d22dd5vide.f20.mp4"));

            videoList.add(new VideoBean("",
                    "http://xp.qpic.cn/oscar_pic/0/1047_30646239313130382d306330392pict/480",
                    "http://123.125.10.236/v.weishi.qq.com/shg_193622498_1047_65b2a00f51c14956b602b7275757vide.f20.mp4"));

            videoList.add(new VideoBean("",
                    "http://xp.qpic.cn/oscar_pic/0/1047_35653531343037612d653235392pict/480",
                    "http://123.125.244.84/v.weishi.qq.com/shg_1638682606_1047_36ff81db2b394c8faa295f40e04dvide.f20.mp4"));


            return videoList;
        }
    }
}
