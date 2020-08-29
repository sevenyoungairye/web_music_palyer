  -- 用户表
  -- music_user  userid username userpwd
  create table music_user (
         userid number primary key,
         username varchar(30) not null,
         userpwd varchar(30) not null,
         createdate date not null,
         islogout number not null, -- 是否删除    
         constraint music_user unique(username)
  ) -- drop table music_user
  -- alter table music_user add(islogout number not null)
  create sequence seq_music_user
  select * from music_user


  -- 音乐表(编号，标题，歌手，封面图片，详细信息，收听量，歌词ID) 歌曲列表
  -- music_song 
  -- music_song_id, music_title, music_cover_img, music_singer, music_details, music_listenling_volume, lyrics_id
  create table music_song (
           music_song_id            number primary key,
           music_title              varchar(50) not null,
           music_cover_img          varchar(30) not null,
           music_singer             varchar(50) not null,
           music_details            varchar(100) not null,
           music_listening_volume  number not null,
           lyrics_id                number,
           music_create_date        date not null
  ) -- drop table music_song
  create sequence seq_music_song
  
  -- 歌词表（歌词ID、歌词文件路径）
  -- music_lyrics lyrics_id, lyrics_path
  create table music_lyrics (
         lyrics_id number primary key,
         lyrics_path varchar(40) not null
  )
  create sequence seq_music_lyrics
  
  -- 歌词表（歌词ID、歌词时间、中文简体歌词、英文歌词、中文繁体歌词）

  -- 音乐歌单表(编号，标题，创建人，详细信息、收听量，标签，封面图片)
  -- music_list 
  -- music_list_id, music_list_title, music_list_creator, music_list_details, music_list_listenling_volume, music_list_tag, music_list_img
  create table music_list (
         music_list_id                  number primary key,
         music_list_title               varchar(50) not null,
         music_list_creator             varchar(30) not null,
         music_list_details             varchar(400) not null,
         music_list_listening_volume    number not null,
         music_list_tag                 varchar(20) not null,
         music_list_img                 varchar(20) not null,
         music_list_create_date                date not null,
         delete_status                  number not null -- 0 existence, 1 delete
  ) -- drop table music_list
  
  create sequence seq_music_list 


  -- 歌单音乐关系表(歌单编号，音乐编号) 多对多关系
  -- music_list_relation
  -- music_list_id, music_song_id
  create table music_list_relation (
         music_list_id number not null,
         music_song_id number not null
  )
  
  -- 搜索表（ID、搜索关键字、搜索次数）
  -- music_search  
  -- music_search_id, music_search_keywords, music_search_count
  create table music_search (
         music_search_id        number primary key,
         music_search_keywords  varchar(80) not null,
         music_search_count     number not null
  )
  create sequence seq_music_search
  
    
  -- 用户的歌单。。。 用户收藏/用户创建的歌单
  -- music_user_songlist   用户的歌单信息
  -- user_playlist_id, userid, music_list_id, music_list_status
  create table music_user_playlist (
         user_playlist_id           number primary key,
         userid                     number not null, -- 用户id
         music_list_id              number not null,-- 歌单id
         music_playlist_count       number not null, -- 用户歌单播放量
         music_list_status          number not null, -- 收藏/创建/我的喜欢歌单
         user_list_create_date     date not null
  ) -- drop table music_user_playlist
  
  create sequence seq_music_user_playlist
  
  
  
