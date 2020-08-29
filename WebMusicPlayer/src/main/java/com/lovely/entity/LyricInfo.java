package com.lovely.entity;

/**
 * 歌词时间，歌词时间对应的歌词
 * @author echo lovely
 * @date 2020/8/25 16:13
 */
public class LyricInfo {
    private String time;
    private String lrcStr;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLrcStr() {
        return lrcStr;
    }

    public void setLrcStr(String lrcStr) {
        this.lrcStr = lrcStr;
    }

    public LyricInfo(){}

    public LyricInfo(String lrcStr, String time) {
        this.lrcStr = lrcStr;
        this.time = time;
    }
}
