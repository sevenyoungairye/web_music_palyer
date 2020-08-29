/*
* user.js music user curd..
* @author echo lovely
* @date 2020/8/17 14:27
* */

function load_page_user(pageNo) {
    // 改变当前页数
    $("#pageNo").val(parseInt(pageNo));
    // 加载当前页数的用户
    load_music_user();
}

function load_music_user() {
    // console.log($("#search-user-form").serialize());
    $.ajax({
        url: "MusicUserAction.do?method=queryMusicUser&rd=" + Math.random(),
        data: $("#search-user-form").serialize(),
        type: "get",
        dataType: "json",
        async: true,
        success: function (data) {

            let total_page = data.totalPage;
            $("#totalPage").val(total_page);

            let tab_str = "";
            let user_list = data.userList;

            for (let i = 0; i < user_list.length; i++) {
                tab_str += "<tr>" +
                    " <td>" + user_list[i].userId + "</td>" +
                    " <td>"+ user_list[i].userName + "</td>" +
                    " <td>" + (user_list[i].isLogout == 0 ? "否" : "是") + "</td>" +
                    " <td>" + user_list[i].createDate + "</td>" +
                    " <td> <a href= 'javascript:query_user(" + user_list[i].userId + ");'>编辑</a> <a href='javascript:delete_user("+user_list[i].userId+");'>删除</a> </td>" +
                    "</tr>";
            }

            $("#tab_user tr").remove();

            $("#tab_user").append(tab_str);

            // console.log(total_page, user_list)

            // 分页
            $("#group-page li:gt(0):not(:last)").remove();
            let page_arr = "";
            for (let i = 1; i <= total_page; i++) {
                page_arr += "<li><a href='javascript:load_page_user("+ i +");'>" + i + "</a></li>";
            }

            /*第一个箭头后面添加*/
            $("#group-page li:eq(0)").after(page_arr);
        },
        error: function (e) {
            console.log(e);
        }
    });
}

load_music_user();

$(function() {

    $("#add_user").on("click", function () {
        // js form validate

        $.ajax({
            url: "MusicUserAction.do?method=addMusicUser",
            type: "post",
            data: $("#add_user_form").serialize(),
            success: function (data) {
                if (data > 0) {
                    // 刷新页面 / 查询
                    $("#search-user").click();
                    // 关闭modal
                    $("#addUser").modal("hide");
                }
            }
        });
    });

    $("#previous_page").on("click", function () {
        let pageNo = parseInt($("#pageNo").val());

        if (pageNo > 1) {
            pageNo -= 1;
            load_page_user(pageNo);
        }
    });

    $("#next-page").on("click", function () {
        let pageNo = parseInt($("#pageNo").val());
        let totalPage = parseInt($("#totalPage").val());

        console.log('pageNo', pageNo, 'totalPage', totalPage)
        if (pageNo < totalPage) {
            pageNo += 1;
            load_page_user(pageNo);
        }
    });

    // search music user
    $("#search-user").on("click", function () {
        // console.log($("#search-user-form").serialize());
        load_music_user();
    });

});

function query_user(user_id) {

    /*$.ajax({
        url: "MusicUserAction.do?method=queryOneMusicUser",
        data: {"musicUser.userId": user_id},
        success: function (data) {
            $("#user-id").val(user_id);
            $("#user-name").val(data.userName);
            $("#user-pwd").val(data.userPwd);
        }
    });*/

    $.get("MusicUserAction.do?method=queryOneMusicUser",
        {
            "musicUser.userId": user_id
        },
        function (data) {
            // console.log('data', data)

            $("#user-id").val(user_id);
            $("#user-name").val(data.userName);
            $("#user-pwd").val(data.userPwd);

            $("#updateUser").modal("show");
        });
}

// update music user
function update_user() {
    $.post("MusicUserAction.do?method=updateMusicUser", $("#update-user-form").serialize(), function (data) {
        if (data > 0) {
            $("#updateUser").modal("hide");
            // refresh data
            $("#search-user").click();
        }
    });
}

function delete_user(user_id) {
    if (window.confirm("是否要删除改用户?")) {
        $.ajax({
            url: "MusicUserAction.do?method=deleteMusicUser",
            data: {"musicUser.userId":user_id},
            success: function (data) {
                if (data > 0) {
                    $("#search-user").click();
                }
            }
        });
    }
}
