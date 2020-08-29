-- music_user
select * from music_user
-- insert 0 δע��, 1�Ѿ�ע��
insert into music_user values (seq_music_user.nextval, ?, ?, sysdate, 0);
-- update 
update music_user set userpwd = ? where userid = ?
-- delete
update music_user set islogout = ? where userid = ?

-- select ��ѯδע�� ��ƴ���û���ģ���� id
select * from (
       select tab1.*, rownum as rn from 
              (select * from music_user where 1=1 and islogout = 0)tab1
) where rn between 0 and 1

-- ��ҳ��ѯ ����������������
select count(*) from (
       select tab1.*, rownum as rn from 
              (select * from music_user where 1=1 and islogout = 0)tab1
)

-- ���и赥
select * from music_list

-- insert
insert into music_list values (seq_music_list.nextval, '����Ĵ�����', 'spotify����', 
' �����Ѿ���ô�࣬��������������������
��Щ������ÿһ�׶����� ר����Ĳ�����Ŷ 
��������ѧϰ ���� ���� ڤ�� ���������� 
����Ҳ����ů������', 666, '������, ѧϰ, ����', 'sweet.jpg', to_date('2020-8-17', 'yyyy-mm-dd'), 0);

insert into music_list values (seq_music_list.nextval, '��ҥС������������ʱ�̵����', 'admin', 
'��һ������ŶԼ���ľ������԰��˵����⣬��������������������ĸ��򡭡�
����û�м������ȵ��ݳ��������ḧ������ǳ��������һ��һ��
��ҥ���������ļ�ֱ�ף�ȴֱ�����ң��������ղ���', 100, 'ŷ��, ��ҥ, ����', '1.jpg', to_date('2020-8-18', 'yyyy-mm-dd'), 0);

insert into music_list values (seq_music_list.nextval, 'ֱ������50��ŷ������', 'admin', 
'ҹ���˾�ʱ�������ڴ��ϡ����ǻ�����������ã���������������˼��ʹ����
������ɢ�����Ĺ¶�������Ȼ����Щ����˭��������ߣ�Ҳû���˸��������ʲô��', 100, 'ŷ��, ��ҥ, ����', '2.jpg', to_date('2020-8-18', 'yyyy-mm-dd'), 0);

insert into music_list values (seq_music_list.nextval, '���ǿ�ͬ��', 'admin', 
'24Сʱѭ�����ָ赥�赥Ϊ���ǹ���~��ӭ��������Ƽ����������bgm', 100, '������', '3.jpg', to_date('2020-8-18', 'yyyy-mm-dd'), 0);

insert into music_list values (seq_music_list.nextval, '��Զ���û�������ǰ�С����', 'admin', 
'һ�Ѽ�������������
��˵���Ͱ���ᵯ������Թ�', 100, '����, ����, ��ҥ', '4.jpg', to_date('2020-8-18', 'yyyy-mm-dd'), 0);

insert into music_list values (seq_music_list.nextval, '������Ǻӣ��ǺӲ��ɼ���', 'admin', 
'���Ǻӹ��̣������˼����롣��', 300, '����, ������, ����', '5.jpg', to_date('2020-8-18', 'yyyy-mm-dd'), 0);

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

-- ��ҳ ��ѯ��ǰ�赥�µĸ���
select * from (select tab1.*, rownum as rn from (
       select song.* from music_list list, music_song song, music_list_relation rel 
       where list.music_list_id = rel.music_list_id and song.music_song_id = rel.music_song_id
       and list.music_list_id = 7
)tab1) where rn between ? and ?

-- �Ƴ�����
select * from music_list
select * from music_song
select * from music_list_relation

-- delete from music_list_relation
where music_list_id = ? and music_song_id = ?

-- Ϊ�赥��Ӹ���
-- 1. ��ѯ���� ���ݸ�����ѯ 2. ���
select music_song_id, music_title, music_singer from music_song where music_title like ?
insert into music_list_relation values ('music_list_id', 'music_song_id')

select music_song_id, music_title, music_singer from music_song where music_title like 'spark'

-- �赥ģ��
select * from (select tab1.*, rownum as rn from (
       select * from music_song where 1=1 
       and music_song_id = ? and music_title like ? and music_singer like ?
)tab1) where rn between 0 and 1

insert into music_song values 
(seq_music_song.nextval, 'title', 'img', 'singer', 'details', 'listen 0', 'lyrics_id 0', sysdate)


-- ���
select * from music_lyrics
insert into music_lyrics values (seq_music_lyrics.nextval, ?)

select seq_music_lyrics.currval from dual

-- ���׸���
select * from music_song where music_song_id = ?
-- delete from music_lyrics 
-- delete from music_song where music_song_id >= 5

select * from music_lyrics where lyrics_id = ?

-- �޸�lyrics
update music_lyrics where lyrics_id = ?

-- �޸ĸ���
update music_song set music_cover_img = ?, music_singer = ?, music_details = ?
where music_song_id = ?

-- ǰ̨�������ֲ�
select * from 
(select song.*, rownum rn from music_song song order by music_listening_volume desc)
where rn between 1 and 3

-- �Ƽ��赥 ���������
select * from 
(select list.*, rownum rn from music_list list order by music_list_listening_volume desc)
where rn between 1 and 6

-- ��������
select * from 
(select song.*, rownum rn from music_song song order by music_create_date desc)
where rn between 1 and 3

-- ǰ̨ҳ��赥���� �������4��
select tab1.* from (
       select song.*, rownum rn from music_list list, music_song song, music_list_relation rel 
       where list.music_list_id = rel.music_list_id and song.music_song_id = rel.music_song_id
       and list.music_list_id = 7
)tab1 where rn between 1 and 4

-- �û���¼
select * from music_user where username = ? and userpwd = ?

-- ��ѯ�赥�����и���
select song.*, rownum rn from music_list list, music_song song, music_list_relation rel 
       where list.music_list_id = rel.music_list_id and song.music_song_id = rel.music_song_id
       and list.music_list_id = ?
       
-- ���Ѹ��� ���� ���� ר��
select * from music_song 
where music_title like '%��%' or music_singer like '%��%' or music_details like '%��%'
       
 




