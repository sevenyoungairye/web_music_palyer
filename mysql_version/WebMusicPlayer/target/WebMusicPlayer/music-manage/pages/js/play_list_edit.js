/**
 * play list edit
 * @description some jq and js.
 * @author echo lovely
 * @date  2020/8/21 20:22
 */

$(function () {

    let param = location.search;
    let str = param.substr(param.indexOf('?') + 1);

    let id = 0;

    if (str.indexOf("&") == -1) {
        let arr = str.split("=");
        id = arr[1];

        if (!id)
            return;
    } else {
        return;
    }
    // console.log('song_list_id', id);

    let span_tag_arr = "";

    // load song list to update
    function load_list_info(id) {
        $.ajax({
            url: "MusicListAction.do?method=queryOneMusicList",
            data: {"musicList.musicListId": id},
            type: "get",
            success: function (data) {
                //console.log('edit data', data);

                $("#list-img").prop("src", "../../img/" + data.musicListImg);
                $("#list-title").val(data.musicListTitle);
                $("#list-details").val(data.musicListDetails);
                $("#list-playing-volume").val(data.musicListListeningVolume);
                $("#music-list-img").val(data.musicListImg);

                $("#list-id").val(data.musicListId);

                let tag_arr = data.musicListTag.split(",");

                $("#music-list-tag").val(tag_arr.toString());

                span_tag_arr = tag_arr;
                // show tag in span
                show_tag(tag_arr);
                // change color in modal
                $(".list-tag").each(function () {
                    for (let i = 0; i < tag_arr.length; i++) {
                        if ($(this).html() == tag_arr[i]) {
                            $(this).css('color', 'red');
                        }
                    }
                });
            },
            error: function (e) {
                console.log(e);
            }
        });
    }

    // load tag in span
    function show_tag(tag_arr) {
        if (!tag_arr) {
            return;
        }

        let tag_str = "";
        for (let i = 0; i < tag_arr.length; i++) {
            tag_str += "<span class='song-tag'>" + tag_arr[i] + "</span>";
        }

        $("#show-tag").html(tag_str);
        return tag_str;
    }

    load_list_info(id);

    //tag modal load event
    $('#myModal').on('show.bs.modal', function () {

        //console.log($("#show-tag").html());
    })

    // 标签点击事件 make color red
    $(document).on("click", ".list-tag", function () {

        if ($(this).css('color')=='rgb(255, 0, 0)') {
            $(this).css('color', '');
            return;
        }

        // 获得当前页面 变色的个数
        let count = getCount();
        if (count ==  3) {
            alert("只可选择三个标签");
            return;
        }

        $(this).css('color', 'red');

    });

    /**
     * get change tags
     * @returns {number}
     */
    function getCount() {
        let count = 0;
        $(".list-tag").each(function () {
            // 统计红色的个数
            if ($(this).css('color')=='rgb(255, 0, 0)') {
                count ++;
            }
        });

        // console.log(count);
        return count;
    }

    // change tag when close tag-modal
    $("#get_tag").on("click", function () {
        show_span();
        $("#myModal").modal('hide');
    });

    // 关闭 保存到span
    $("#myModal").on("hide.bs.modal", function () {
        show_span();
    });

    function show_span() {
        let tag = get_tag();

        if (!tag)
            return;

        let tag_str = "";
        let tag_arr = tag.split(",");

        for (let i = 0; i < tag_arr.length; i++) {
            tag_str += "<span class='song-tag'>" + tag_arr[i] + "</span>";
        }

        // keep tag in music list tag input
        $("#music-list-tag").val(tag);

        // show change music tag
        $("#show-tag").html(tag_str);
    }

    // get image
    $("#upload-input").on("change", function () {
        // console.log('img', $(this).val());
        let img = $(this).val();
        let img_url = getObjectURL(this.files[0]);
        if (img) {
            $("#show-img").css('width', '200px')
            $("#show-img").css('height', '200px')
            $("#show-img").prop("src", img_url);
            $("#upload-img").removeClass("disabled");
            $("#upload-img").prop("disabled", false);
        }
    });

    // upload image to server
    $("#upload-img").click(function () {

        if (!$("#show-img").prop("src")) {
            return;
        }

        //let imgSize = $("#upload-input")[0].files[0].size;
        let imgSize = findSize("upload-input");
        // console.log(imgSize);

        if (imgSize >= 2) {
            //$("#myModal1").modal('show');
            alert('请选择2MB以下的图片');
            return;
        }

        let formData = new FormData($("#img-form")[0]);
        $.ajax({
            url: "/WebMusicPlayer/imgUpload",
            data: formData,
            type: "post",
            cache: false,
            processData: false,/*无需数据处理*/
            contentType: false,
            success: function (img_name) {
                // img_name in server
                $("#music-list-img").val(img_name);
                // change current page image
                $("#list-img").prop('src', $("#show-img").prop('src'));
                // close modal
                $("#upd-img").modal('hide');
            },
            error: function (e) {
                console.log(e);
            }
        });

    });

    // update play list !
    $("#update-play-list").click(function () {
        $.ajax({
            url: "MusicListAction.do?method=updateMusicList",
            data: $("#play-list-form").serialize(),
            type: "get",
            success: function (data) {
                if (data > 0) {
                    location.href = "song-list-details.html?id=" + id;
                }
            },
            error: function (e) {
                console.log(e);
            }
        });
    });
});

/**
 * get tag string to add db
 * @returns {string} play list tag
 */
function get_tag() {
    let tag_str = "";

    $(".list-tag").each(function () {
        if ($(this).css('color')=='rgb(255, 0, 0)') {
            tag_str += $(this).text() + "_";
        }
    });

    let tag_arr = [];
    if (tag_str) {
        let arr = tag_str.split("_");
        for (let i = 0; i < arr.length - 1; i++) {
            tag_arr.push(arr[i]);
        }
    }
    // console.log(tag_arr.toString(), typeof(tag_arr.toString()));
    return tag_arr.toString();
}

/**
 * get file path, show image
 * @param file
 * @returns {null}
 */
function getObjectURL(file) {
    let url = null;
    if (window.createObjcectURL != undefined) {
        url = window.createOjcectURL(file);
    } else if (window.URL != undefined) {
        url = window.URL.createObjectURL(file);
    } else if (window.webkitURL != undefined) {
        url = window.webkitURL.createObjectURL(file);
    }
    return url;
}

// find file size
function findSize(field_id) {
    let fileInput = $("#"+field_id)[0];
    let byteSize  = fileInput.files[0].size;
    return ( Math.ceil(byteSize / 1024 / 1024) ); // Size returned in MB.
}