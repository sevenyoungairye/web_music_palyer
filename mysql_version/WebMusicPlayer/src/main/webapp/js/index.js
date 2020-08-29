/**
 * @author echo lovely
 * @date 2020/8/26 14:40
 * 加载首页歌曲列表
 */
function songCarousel() {
    $.ajax({
        url: "MusicSongAction.do?method=songCarousel",
        type: "get",
        async: true,
        success: function (data) {
            let carousel_str = "";
            for (let i = 0; i < data.length; i++) {
                if (i == 0) {
                    carousel_str += "<div class='item active'>";
                } else {
                    carousel_str += "<div class='item'>";
                }
                carousel_str += "<a href='song-play.html?id="+data[i].songId+"'> " +
                    "<img class='top-img' src='music-manage/img/"+data[i].coverImg+"' alt='...'> </a>" +
                    " <div class='carousel-caption'></div>" +
                    " </div>";
            }
            $("#carouselDiv div").remove();
            $("#carouselDiv").append(carousel_str);

        },
        error: function (e) {
            console.log(e);
        }
    });
}

function queryRecommendPlayList() {
    $.ajax({
        url: "MusicListAction.do?method=queryRecommendPlayList",
        type: "get",
        async: true,
        success: function (data) {
            let playListStr = "<div class=\"col-xs-12\">";

            for (let i = 0; i < data.length; i++) {

                if (i % 3 == 0) {
                    playListStr += "</div>";
                }

                playListStr +=
                    "<div>" +
                    "    <div class=\"col-xs-3 song-list-size\">" +
                    "        <div>" +
                    "            <a href='song-list.html?id=" + data[i].musicListId + "'>" +
                    "                <img class=\"song-list-img\" src='music-manage/img/" + data[i].musicListImg + "' alt=\"...\">" +
                    "            </a>" +
                    "        </div>" +
                    "        <a href='song-list.html?id=" + data[i].musicListId + "'>" +
                    "       <div class=\"song-list-word-size\">" + data[i].musicListTitle + "</div> </a>" +
                    "    </div>" +
                    "</div>";
            }
            $("#recommendPlayList div").remove();
            $("#recommendPlayList").append(playListStr);
        },
        error: function (e) {
            console.log(e);
        }
    });
}

function queryLatestSong() {
    $.ajax({
        url: "MusicSongAction.do?method=queryLatestSong",
        type: "get",
        async: true,
        success: function (data) {

            let song_str = "";
            for (let i = 0; i < data.length; i++) {
                song_str +=
                    "<div class=\"row\">" +
                    "     <table class=\"table\">" +
                    "         <tr>" +
                    "             <td width=\"80%\">" +
                    "             <span class=\"song-name\">" + data[i].title + "</span> <br/>" +
                    "             <img class=\"song-logo\" src=\"img/spotify-logo.png\" alt=\"...\">" +
                    "             <span class=\"song-singer\">" + data[i].singer + "</span> " +
                    "             <span class=\"song-info\">" + data[i].details + "</span>" +
                    "             </td>" +
                    "             <td>" +
                    "               <a href='song-play.html?id=" + data[i].songId + "'><img src=\"img/play.png\" style=\"width: 40%;\"></a>" +
                    "             </td>" +
                    "         </tr>" +
                    "     </table>" +
                    " </div>";
            }
            $("#latest-song div").remove();
            $("#latest-song").append(song_str);
        },
        error: function (e) {
            console.log(e);
        }
    });
}

$(function () {

    songCarousel();

    queryRecommendPlayList();

    queryLatestSong();

    $("#hot-song-href").click(function () {

        // 查询收听量最高的三首歌曲！
        $.ajax({
            url: "MusicSongAction.do?method=songCarousel",
            type: "get",
            async: true,
            success: function (data) {
                let hot_song_str = "";
                for (let i = 0; i < data.length; i++) {
                    hot_song_str +=
                        "<div class=\"row\">" +
                        "    <a href='song-play.html?id=" + data[i].songId + "'>" +
                        "        <div class=\"song-seq\">" +
                        (i + 1) +
                        "        </div>" +
                        "        <div style=\"float: left\">" +
                        "            <div class=\"song-name\">" + data[i].title + "</div>" +
                        "            <div class=\"song-creator\">" + data[i].singer + "</div>" +
                        "        </div>" +
                        "        <div style=\"float: right\">" +
                        "            <img src=\"img/right-play.png\" class=\"right-play\"" +
                        "               style=\"background-color: transparent;\">" +
                        "        </div>" +
                        "    </a>" +
                        "</div>";

                }
                $("#hot-song-list div").remove();
                $("#hot-song-list").append(hot_song_str);

            },
            error: function (e) {
                console.log(e);
            }
        });
    });

    $("#login-btn").click(function () {

        let userName = $("#userName").val();
        let userPwd = $("#userPwd").val();

        if (!userName) {
            $("#login-tips").text("填写账号");
            return;
        }

        if (!userPwd) {
            $("#login-tips").text("填写密码");
            return;
        }
        $.ajax({
            url: "MusicUserAction.do?method=userLogin",
            type: "post",
            data: {"musicUser.userName": userName, "musicUser.userPwd": userPwd},
            success: function (data) {
                if (data == 1) {
                    // a
                    location.href = "music-manage/index.html";
                    $("#myModal").modal('hide');
                } else if (data == 2) {
                    $("#myModal").modal('hide');
                } else {
                    $("#login-tips").text("账号密码错误！");
                }
            },
            error: function (e) {
                console.log(e);
            }
        });
    });


});

$(function () {

    // jq 绑定无效。。
    /*$("#search-song").oninput(function () {
        console.log('oninput..');
    });*/

    /*let search_song = document.getElementById("search-song");
    search_song.addEventListener("oninput", function () {
        console.log(666);
    });*/

   /* $("#search-song").on("oninput", function () {
        search_song();
    });*/

});

function search_song() {
    let condition = $("#search-song").val();

    // 隐藏标签
    if (condition) {
        $("#tag-list").css('display', 'none');
    } else {
        $("#tag-list").css('display', 'block');
    }

    $.ajax({
        url: "MusicSongAction.do?method=searchSongInPage",
        type: "get",
        data: {"condition": condition},
        success: function (data) {

            // console.log('data', data);
            let song = "";
            for (let i = 0; i < data.length; i++) {
                // 重新拼接
                song += "<div class='row song-link'>" +
                    "    <a href='song-play.html?id=" + data[i].songId + "'>" +
                    "        <span class=\"search glyphicon glyphicon-search\"></span>" +
                    "        <span>" + data[i].title.split(".")[0] + "</span>" +
                    "    </a>" +
                    "</div>";
            }

            if (song) {
                $("#song-ser-list").css('display', 'block');
                // 显示歌曲
                $("#song-ser-list div").remove();
                $("#song-ser-list").append(song);
            } else {
                // 隐藏歌曲列表
                $("#song-ser-list div").remove();
            }

        },
        error: function (e) {
            console.log(e);
        }
    });

}