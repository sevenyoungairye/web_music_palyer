package com.lovely.entity;

import java.sql.Date;

/**
 * 系统歌单列表
 * @author echo lovely
 * @date 2020/8/15 15:20
 */
public class MusicList {
    private Long musicListId;
    private String musicListTitle;
    private String musicListCreator;
    private String musicListDetails;
    private Long musicListListeningVolume ;
    private String musicListTag;
    private String musicListImg;

    private Date musicListCreateDate;

    private Integer deleteStatus;

    public MusicList() {
    }

    public MusicList(Long musicListId, String musicListTitle, String musicListCreator, String musicListDetails,
                     Long musicListListeningVolume, String musicListTag, String musicListImg,
                     Date musicListCreateDate, int deleteStatus) {
        this.musicListId = musicListId;
        this.musicListTitle = musicListTitle;
        this.musicListCreator = musicListCreator;
        this.musicListDetails = musicListDetails;
        this.musicListListeningVolume = musicListListeningVolume;
        this.musicListTag = musicListTag;
        this.musicListImg = musicListImg;
        this.musicListCreateDate = musicListCreateDate;
        this.deleteStatus = deleteStatus;
    }

    public Long getMusicListId() {
        return musicListId;
    }

    public void setMusicListId(Long musicListId) {
        this.musicListId = musicListId;
    }

    public String getMusicListTitle() {
        return musicListTitle;
    }

    public void setMusicListTitle(String musicListTitle) {
        this.musicListTitle = musicListTitle;
    }

    public String getMusicListCreator() {
        return musicListCreator;
    }

    public void setMusicListCreator(String musicListCreator) {
        this.musicListCreator = musicListCreator;
    }

    public String getMusicListDetails() {
        return musicListDetails;
    }

    public void setMusicListDetails(String musicListDetails) {
        this.musicListDetails = musicListDetails;
    }

    public Long getMusicListListeningVolume() {
        return musicListListeningVolume;
    }

    public void setMusicListListeningVolume(Long musicListListeningVolume) {
        this.musicListListeningVolume = musicListListeningVolume;
    }

    public String getMusicListTag() {
        return musicListTag;
    }

    public void setMusicListTag(String musicListTag) {
        this.musicListTag = musicListTag;
    }

    public String getMusicListImg() {
        return musicListImg;
    }

    public void setMusicListImg(String musicListImg) {
        this.musicListImg = musicListImg;
    }

    public Date getMusicListCreateDate() {
        return musicListCreateDate;
    }

    public void setMusicListCreateDate(Date musicListCreateDate) {
        this.musicListCreateDate = musicListCreateDate;
    }

    public int getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(int deleteStatus) {
        this.deleteStatus = deleteStatus;
    }
}
