package com.lovely.entity;

import java.sql.Date;

/**
 * 歌曲 表
 * @author echo lovely
 * @date 2020/8/15 11:59
 */
public class MusicSong {
    private Long songId;
    private String title;
    private String coverImg;
    private String singer;
    private String details;
    private Long listeningVolume;// 收听量
    private Long lyricsId;

    private Date createDate;

    public MusicSong() {}

    public MusicSong(Long songId, String title, String coverImg,
                     String singer, String details, Long listeningVolume,
                     Long lyricsId, Date createDate) {
        this.songId = songId;
        this.title = title;
        this.coverImg = coverImg;
        this.singer = singer;
        this.details = details;
        this.listeningVolume = listeningVolume;
        this.lyricsId = lyricsId;
        this.createDate = createDate;
    }

    public Long getSongId() {
        return songId;
    }

    public void setSongId(Long songId) {
        this.songId = songId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Long getListeningVolume() {
        return listeningVolume;
    }

    public void setListeningVolume(Long listeningVolume) {
        this.listeningVolume = listeningVolume;
    }

    public Long getLyricsId() {
        return lyricsId;
    }

    public void setLyricsId(Long lyricsId) {
        this.lyricsId = lyricsId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "MusicSong{" +
                "songId=" + songId +
                ", title='" + title + '\'' +
                ", coverImg='" + coverImg + '\'' +
                ", singer='" + singer + '\'' +
                ", details='" + details + '\'' +
                ", listeningVolume=" + listeningVolume +
                ", lyricsId=" + lyricsId +
                ", createDate=" + createDate +
                '}';
    }
}
