package com.lovely.entity;

/**
 * 音乐歌单关系表 歌曲<->多对多关系
 * @author echo lovely
 * @date 2020/8/15 15:35
 */
public class MusicListRelation {
    private Long musicListId;
    private Long musicSongId;

    public MusicListRelation() {}

    public MusicListRelation(Long musicListId, Long musicSongId) {
        this.musicListId = musicListId;
        this.musicSongId = musicSongId;
    }

    public Long getMusicListId() {
        return musicListId;
    }

    public void setMusicListId(Long musicListId) {
        this.musicListId = musicListId;
    }

    public Long getMusicSongId() {
        return musicSongId;
    }

    public void setMusicSongId(Long musicSongId) {
        this.musicSongId = musicSongId;
    }
}
