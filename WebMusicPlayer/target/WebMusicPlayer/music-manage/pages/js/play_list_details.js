/**
 * play list details
 * -- load play list and song in list, add or remove song in list
 * -- query song by multiple pages ..
 * @author echo lovely
 * @Date 2020/8/22 16:39
 */

function get_play_list_id() {
    let param = location.search;
    let str = param.substr(param.indexOf('?') + 1);

    let id = 0;

    if (str.indexOf("&") == -1) {
        let arr = str.split("=");
        id = arr[1];
    }
    // console.log('song_list_id', id);
    return id;
}

$(function () {

    let id = get_play_list_id();

    if (!id)
        return;

    // load play list info
    $.ajax({
        url: "MusicListAction.do?method=queryOneMusicList",
        data: {"musicList.musicListId": id},
        type: "get",
        success: function (data) {
            $("#list-img").prop("src", "../../img/" + data.musicListImg);
            $("#list-title").html(data.musicListTitle);
            $("#list-creator").html(data.musicListCreator);
            $("#list-details").html(data.musicListDetails);
            $("#list-playing-volume").html("播放 " + data.musicListListeningVolume + "次");

            $("#list-date").html(data.musicListCreateDate);

            $("#list-update").prop("href", "song-list-edit.html?id=" + id);

            let tag_str = "";
            // <span class="song-tag">轻音乐</span>
            let tag_arr = data.musicListTag.split(",");

            for (let i = 0; i < tag_arr.length; i++) {
                tag_str += "<span class='song-tag'>" + tag_arr[i] + "</span>";
            }

            $("#list-tag").html(tag_str);
        },
        error: function (e) {
            console.log(e);
        }
    });

    load_song(id);

    $("#previous_page").on("click", function () {
        let list_page_no = parseInt($("#list_page_no").val());

        if (list_page_no > 1) {
            load_play_list(list_page_no - 1, id);
        }
    });

    $("#next-page").on("click", function () {
        let list_page_no = parseInt($("#list_page_no").val());
        let totalPage = parseInt($("#totalPage").val());

        if (list_page_no < totalPage) {
            load_play_list(list_page_no + 1, id);
        }
    });
});

// load song in play list
function load_song(id) {
    $.ajax({
        url: "MusicListAction.do?method=querySongInPlayList",
        data: {"musicList.musicListId": id, "listPageNo": $("#list_page_no").val()},
        type: "get",
        success: function (data) {

            // console.log('song', data);
            let totalPage = data.totalPage;
            $("#totalPage").val(totalPage);
            let playList = data.playList;
            let song_list_str = "";

            for (let i = 0; i < playList.length; i++) {
                song_list_str += "<tr>" +
                    "    <td>"+playList[i].songId+"</td>" +
                    "    <td>"+playList[i].title+"</td>" +
                    "    <td>"+playList[i].singer+"</td>" +
                    "    <td>"+playList[i].createDate+"</td>" +
                    "    <td>" +
                    "        <a href='javascript:remove_song("+id+","+playList[i].songId+");'>移除</a>" +
                    "    </td>" +
                    "</tr>";
            }

            $("#song-list-body tr").remove();
            $("#song-list-body").append(song_list_str);

            // pages..
            let page_str = "";
            for (let i = 1; i <= totalPage; i++) {
                page_str += "<li><a href='javascript:load_play_list(" + i + "," + id + ");'>"+i+"</a></li>";
            }
            $("#group-page li:gt(0):not(:last)").remove();
            $("#group-page li:eq(0)").after(page_str);

        },
        error: function (e) {
            console.log(e);
        }
    });
}

/**
 * multiple query by page
 * @param list_page_no
 * @param id play_list_id
 */
function load_play_list(list_page_no, id) {
    $("#list_page_no").val(parseInt(list_page_no));
    // search
    load_song(id);
}

function remove_song(song_list_id, song_id) {
    // 提示是否删除
    if (window.confirm("是否从该歌单移除该歌曲？")) {
        $.get
        (
            "MusicListAction.do?method=removeSongInPlayList",
            {"musicListRelation.musicListId": song_list_id,
                "musicListRelation.musicSongId": song_id
            },
            function (data) {
                if (data > 0) {
                    load_song(song_list_id);
                }
            }
        );
    }
}

$(function () {

    // 下拉框改变事件 搜索歌曲
    // onchange 失去焦点触发 oninput 文本框改变事件
    /*$("#search-song").on("onchange", function () {
        search_song_to_add();
    });*/

    $("#add-song-to-list").click(function () {
        // add song to current play list!
        let selected_val = $("#search-song").val();
        let option_len = $("#myList option").length;

        let song_id = "";
        for (let i = 0; i < option_len; i++) {
            let option_val = $("#myList option").eq(i).prop("value");

            if (option_val == selected_val) {
                // get my define attribute song-id
                song_id = $("#myList option").eq(i).attr("song-id");
                break;
            }
        }
        if (!song_id) {
            alert("歌曲不存在");
            return;
        }
        // add song!
        let play_list_id = get_play_list_id();
        $.ajax({
            url: "MusicListAction.do?method=addSongInPlayList",
            type: "get",
            data: {"musicListRelation.musicListId": play_list_id,
                "musicListRelation.musicSongId": song_id},
            success: function (data) {
                if (data > 0) {
                    $("#add-song-to-list-modal").modal('hide');
                    // refresh
                    load_song(play_list_id);
                }
            },
            error: function (e) {
                console.log(e);
            }
        });
    });

});

function search_song_to_add() {
    let song_title = $("#search-song").val();
    $.ajax({
        url: "MusicListAction.do?method=querySongByName",
        type: "get",
        data: {"song.title": song_title},
        success: function (data) {
            $("#myList option").remove();

            console.log('data', data);
            // data is a song list(id, title, singer)
            let option = "";
            for (let i = 0; i < data.length; i++) {
                // <option label="xxxHub" value="github.com"></option>
                option += "<option label='"+data[i].singer+"' " +
                    "value='"+data[i].title+"' song-id='"+data[i].songId+"'></option>";
            }
            $("#myList").append(option);
        },
        error: function (e) {
            console.log(e);
        }
    });
}
