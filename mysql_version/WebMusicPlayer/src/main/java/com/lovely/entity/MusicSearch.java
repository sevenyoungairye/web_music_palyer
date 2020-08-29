package com.lovely.entity;

/**
 * 音乐搜索表 关键词 搜索次数
 * @author echo lovely
 * @date 2020/8/15 15:38
 */
public class MusicSearch {

    private Long musicSearchId;
    private String musicSearchKeywords;
    private Long musicSearchCount;

    public MusicSearch() {}

    public MusicSearch(Long musicSearchId, String musicSearchKeywords, Long musicSearchCount) {
        this.musicSearchId = musicSearchId;
        this.musicSearchKeywords = musicSearchKeywords;
        this.musicSearchCount = musicSearchCount;
    }

    public Long getMusicSearchId() {
        return musicSearchId;
    }

    public void setMusicSearchId(Long musicSearchId) {
        this.musicSearchId = musicSearchId;
    }

    public String getMusicSearchKeywords() {
        return musicSearchKeywords;
    }

    public void setMusicSearchKeywords(String musicSearchKeywords) {
        this.musicSearchKeywords = musicSearchKeywords;
    }

    public Long getMusicSearchCount() {
        return musicSearchCount;
    }

    public void setMusicSearchCount(Long musicSearchCount) {
        this.musicSearchCount = musicSearchCount;
    }
}
