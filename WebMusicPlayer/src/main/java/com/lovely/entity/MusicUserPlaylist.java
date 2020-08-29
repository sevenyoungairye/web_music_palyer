package com.lovely.entity;

import java.sql.Timestamp;

/**
 * 用户歌单表 用户的歌单 播放次数 创建或者收藏
 * @author echo lovely
 * @date 2020/8/15 15:41
 */
public class MusicUserPlaylist {

    private Long userPlayListId;
    private String userId;
    private Long musicListId;
    private Long musicPlayListCount; // change
    private Integer musicListStatus;

    private Timestamp userListCreateDate; // 用户歌单创建日期

    public MusicUserPlaylist() {}

    public MusicUserPlaylist(Long userPlayListId, String userId, Long musicListId,
                             Long musicPlayListCount, Integer musicListStatus, Timestamp userListCreateDate) {
        this.userPlayListId = userPlayListId;
        this.userId = userId;
        this.musicListId = musicListId;
        this.musicPlayListCount = musicPlayListCount;
        this.musicListStatus = musicListStatus;
        this.userListCreateDate = userListCreateDate;
    }

    public Long getUserPlayListId() {
        return userPlayListId;
    }

    public void setUserPlayListId(Long userPlayListId) {
        this.userPlayListId = userPlayListId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getMusicListId() {
        return musicListId;
    }

    public void setMusicListId(Long musicListId) {
        this.musicListId = musicListId;
    }

    public Long getMusicPlayListCount() {
        return musicPlayListCount;
    }

    public void setMusicPlayListCount(Long musicPlayListCount) {
        this.musicPlayListCount = musicPlayListCount;
    }

    public Integer getMusicListStatus() {
        return musicListStatus;
    }

    public void setMusicListStatus(Integer musicListStatus) {
        this.musicListStatus = musicListStatus;
    }

    public Timestamp getUserListCreateDate() {
        return userListCreateDate;
    }

    public void setUserListCreateDate(Timestamp userListCreateDate) {
        this.userListCreateDate = userListCreateDate;
    }
}
