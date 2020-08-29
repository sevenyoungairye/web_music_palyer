-- music_user
select * from music_user
-- insert 0 未注销, 1已经注销
insert into music_user values (seq_music_user.nextval, ?, ?, sysdate, 0);
-- update 
update music_user set userpwd = ? where userid = ?
-- delete
update music_user set islogout = ? where userid = ?

-- select 查询未注销 可拼接用户名模糊， id
select * from (
       select tab1.*, rownum as rn from 
              (select * from music_user where 1=1 and islogout = 0)tab1
) where rn between 0 and 1

-- 分页查询 满足条件的总行数
select count(*) from (
       select tab1.*, rownum as rn from 
              (select * from music_user where 1=1 and islogout = 0)tab1
)

-- 所有歌单
select * from music_list

-- insert
insert into music_list values (seq_music_list.nextval, '甜甜的纯音乐', 'spotify音乐', 
' 生活已经这么苦，就让甜甜的旋律治愈你吧
这些纯音呢每一首都超甜 专治你的不开心哦 
不管你是学习 看书 工作 冥想 听听甜甜纯音 
心里也会变得暖乎乎哒', 666, '轻音乐, 学习, 快乐', 'sweet.jpg', to_date('2020-8-17', 'yyyy-mm-dd'), 0);

insert into music_list values (seq_music_list.nextval, '民谣小调｜纵享休闲时刻的惬意', 'admin', 
'有一类歌表达着对家乡的眷恋，对爱人的蜜意，对万物的赞美，对世道的感悟……
它们没有激情澎湃的演唱，而是轻抚吉他，浅唱低吟着一言一语
民谣就是这样的简单直白，却直击心弦，让人欲罢不能', 100, '欧美, 民谣, 放松', '1.jpg', to_date('2020-8-18', 'yyyy-mm-dd'), 0);

insert into music_list values (seq_music_list.nextval, '直击灵魂的50首欧美精曲', 'admin', 
'夜深人静时，你躺在床上。总是会有无数的迷茫不安，有数不清的思念痛楚，
还有驱散不开的孤独无助。然而这些，你谁都不会告诉，也没有人告诉你答案是什么。', 100, '欧美, 民谣, 放松', '2.jpg', to_date('2020-8-18', 'yyyy-mm-dd'), 0);

insert into music_list values (seq_music_list.nextval, '与星空同眠', 'admin', 
'24小时循环音乐歌单歌单为你们共建~欢迎大家向我推荐更多好听的bgm', 100, '轻音乐', '3.jpg', to_date('2020-8-18', 'yyyy-mm-dd'), 0);

insert into music_list values (seq_music_list.nextval, '永远是敲击心灵的那把小锤子', 'admin', 
'一把吉他，漫步天涯
诉说离别和哀愁，轻弹白云与苍狗', 100, '华语, 流行, 民谣', '4.jpg', to_date('2020-8-18', 'yyyy-mm-dd'), 0);

insert into music_list values (seq_music_list.nextval, '所念皆星河，星河不可及。', 'admin', 
'“星河滚烫，你是人间理想。”', 300, '钢琴, 轻音乐, 清新', '5.jpg', to_date('2020-8-18', 'yyyy-mm-dd'), 0);

-- select all
select * from (select tab1.*, rownum as rn from (
      select * from music_list where 1=1 and music_list_id = ? and music_list_title like ? and music_list_tag like ?
             )tab1) where rn between ? and ?
             
select count(*) from (select tab1.*, rownum as rn from (
      select * from music_list where 1=1 and music_list_id = ? and music_list_title like ? and music_list_tag like ?
             )tab1)

-- select single list
select * from music_list where music_list_id = ?
             
delete from music_list where music_list_id = ? -- user
update music_list set delete_status = ? where music_list_id = ? -- admin

update music_list set music_list_title = ?, music_list_details = ?, music_list_listening_volume = ?, music_list_tag = ?, music_list_img = ?
where music_list_id = ?

delete from music_list where music_list_id = 11

-- song 
select * from music_song
insert into music_song values (seq_music_song.nextval, 'Style.mp3', 
'style.jpg', 'Taylor Swift', 'Big Machine #1s, Volume 4', 10, 0, sysdate);

insert into music_song values (seq_music_song.nextval, 'Spark Fly.mp3', 
'spark fly.jpg', 'Taylor Swift', 'Speak Now (Extended Version)', 3, 0, sysdate);

-- relation 
select * from music_list_relation
insert into music_list_relation values (7, 2);
insert into music_list_relation values (7, 3);

-- 分页 查询当前歌单下的歌曲
select * from (select tab1.*, rownum as rn from (
       select song.* from music_list list, music_song song, music_list_relation rel 
       where list.music_list_id = rel.music_list_id and song.music_song_id = rel.music_song_id
       and list.music_list_id = 7
)tab1) where rn between ? and ?

-- 移除歌曲
select * from music_list
select * from music_song
select * from music_list_relation

-- delete from music_list_relation
where music_list_id = ? and music_song_id = ?

-- 为歌单添加歌曲
-- 1. 查询歌曲 根据歌名查询 2. 添加
select music_song_id, music_title, music_singer from music_song where music_title like ?
insert into music_list_relation values ('music_list_id', 'music_song_id')

select music_song_id, music_title, music_singer from music_song where music_title like 'spark'

-- 歌单模块
select * from (select tab1.*, rownum as rn from (
       select * from music_song where 1=1 
       and music_song_id = ? and music_title like ? and music_singer like ?
)tab1) where rn between 0 and 1

insert into music_song values 
(seq_music_song.nextval, 'title', 'img', 'singer', 'details', 'listen 0', 'lyrics_id 0', sysdate)


-- 歌词
select * from music_lyrics
insert into music_lyrics values (seq_music_lyrics.nextval, ?)

select seq_music_lyrics.currval from dual

-- 单首歌曲
select * from music_song where music_song_id = ?
-- delete from music_lyrics 
-- delete from music_song where music_song_id >= 5

select * from music_lyrics where lyrics_id = ?

-- 修改lyrics
update music_lyrics where lyrics_id = ?

-- 修改歌曲
update music_song set music_cover_img = ?, music_singer = ?, music_details = ?
where music_song_id = ?

-- 前台，歌曲轮播
select * from 
(select song.*, rownum rn from music_song song order by music_listening_volume desc)
where rn between 1 and 3

-- 推荐歌单 播发量最高
select * from 
(select list.*, rownum rn from music_list list order by music_list_listening_volume desc)
where rn between 1 and 6

-- 最新音乐
select * from 
(select song.*, rownum rn from music_song song order by music_create_date desc)
where rn between 1 and 3

-- 前台页面歌单歌曲 播放最多4条
select tab1.* from (
       select song.*, rownum rn from music_list list, music_song song, music_list_relation rel 
       where list.music_list_id = rel.music_list_id and song.music_song_id = rel.music_song_id
       and list.music_list_id = 7
)tab1 where rn between 1 and 4

-- 用户登录
select * from music_user where username = ? and userpwd = ?

-- 查询歌单中所有歌曲
select song.*, rownum rn from music_list list, music_song song, music_list_relation rel 
       where list.music_list_id = rel.music_list_id and song.music_song_id = rel.music_song_id
       and list.music_list_id = ?
       
-- 热搜歌曲 歌曲 歌手 专辑
select * from music_song 
where music_title like '%周%' or music_singer like '%周%' or music_details like '%周%'
       
 




