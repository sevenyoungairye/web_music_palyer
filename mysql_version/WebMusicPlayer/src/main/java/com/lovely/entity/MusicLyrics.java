package com.lovely.entity;

/**
 * 歌词表
 * @author echo lovely
 * @date 2020/8/15 15:33
 */
public class MusicLyrics {
    private Long lyricsId;
    private String lyricsPath;

    public MusicLyrics() {

    }

    public MusicLyrics(Long lyricsId, String lyricsPath) {
        this.lyricsId = lyricsId;
        this.lyricsPath = lyricsPath;
    }

    public Long getLyricsId() {
        return lyricsId;
    }

    public void setLyricsId(Long lyricsId) {
        this.lyricsId = lyricsId;
    }

    public String getLyricsPath() {
        return lyricsPath;
    }

    public void setLyricsPath(String lyricsPath) {
        this.lyricsPath = lyricsPath;
    }
}
