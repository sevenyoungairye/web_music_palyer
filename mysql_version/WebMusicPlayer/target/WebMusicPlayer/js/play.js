$(function(){
	//歌曲是否已加载
	var loadFlag = false;
	//手指是否在滑动
	var touchFlag = false;
	//滑动过程记录当前播放时间
	var touchCurrTime = 0;
	var au = document.getElementById("au");
	
	$("#btn2").on("click",function(){
		//当前歌曲是否处于暂停状态  暂停返回true
		//au.paused
		if(au.paused){
			//开始播放音乐
			au.play();
		}else{
			//暂停音乐
			au.pause();
		}
	});
	
	//播放事件 歌曲开始播放的时候,执行方法中的代码
	au.addEventListener("play",function(){
		//开始播放动画
		$(".imgNeedle").css("animation-name","imgNeedle1");
		$(".imgNeedle").css("animation-play-state","running");
		$(".imgMusic").css("animation-play-state","running");
		//切换图标
		//移除样式
		$("#btn2").find("span").removeClass("glyphicon-play");
		//添加样式
		$("#btn2").find("span").addClass("glyphicon-pause");
	});
	
	//暂停事件
	au.addEventListener("pause",function(){
		//开始播放动画
		$(".imgNeedle").css("animation-name","imgNeedle2");
		$(".imgNeedle").css("animation-play-state","running");
		$(".imgMusic").css("animation-play-state","paused");
		//切换图标
		//移除样式
		$("#btn2").find("span").removeClass("glyphicon-pause");
		//添加样式
		$("#btn2").find("span").addClass("glyphicon-play");
	});
	
	//加载音乐
	//歌曲:歌曲名称、歌曲图片、歌曲路径、歌词、
	
	//当前歌曲下标
	var index = 0;
	var jsonList = [];
	// 发送Ajax查询歌曲...

	let id = get_id();

	if (!id)
		return;

	let split = id.split("_");
	if (split.length > 1) {
		// 多首歌曲
		let list_id = split[0];
		// 当前歌单歌曲集合
		let song_arr = getSongIdInCurList(list_id);

		jsonList = song_arr;

		//播放当前歌曲
		for (let i = 0; i < song_arr.length; i++) {
			let songId = jsonList[i].id; // from db
			if (songId == split[1]) {
				index = i;
			}
		}

	} else {
		// 获取单首歌曲
		single_song(id);
	}

	/**
	 * 根据歌曲id 播放当前歌曲
	 * @param id 当前歌曲id
	 */
	function single_song(id) {
		$.ajax({
			url: "GetMusicSource.do?method=getSongBySongId",
			type: "get",
			async: false, // 同步线程
			data: {"songId": id},
			success: function (data) {
				// console.log(data);
				jsonList = [];
				jsonList.push(data);
			},
			error: function (e) {
				console.log(e);
			}
		});
	}

	// 获取整个歌单的播放列表 获取歌曲数组
	function getSongIdInCurList(listId) {
		let songIdArr = [];

		$.ajax({
			url: "GetMusicSource.do?method=getAllSongInPlayList",
			type: "get",
			data: {"listId": listId},
			async: false,
			success: function (data) {
				// 歌曲id集合
				songIdArr = data;
				// console.log(data);
			},
			error: function (e) {
				console.log(e);
			}
		});

		return songIdArr;
	}

	// au.setAttribute('crossorigin', 'anonymous');
	au.crossOrigin = 'anonymous';
	au.src = jsonList[index].url;

	//au.src = "mp3/等什么君 - 归寻.flac";

	changeImg(index);
	
	//下一首
	var pausedFlag = "y";
	$("#btn3").on("click",function(){
		index++;
		if(index>=jsonList.length){
			//越界
			index = 0;
		}
		
		//记录当前播放状态
		if(!au.paused){
			pausedFlag = "n";
		}
		
		au.src = jsonList[index].url;
	});


	// 上一首
	$("#btn1").on("click",function(){
		index--;
		if(index<=0){
			index = 0;
		}

		//记录当前播放状态
		if(!au.paused){
			pausedFlag = "n";
		}

		au.src = jsonList[index].url;
	});
	
	function changeImg(index){
		//切换音乐图片
		var img = jsonList[index].img;
		//页面背景
		$(".bgDiv").css("background-image","url("+img+")");
		//黑圈中间的图片
		$(".imgMusic").prop("src",img);

		let name = jsonList[index].name;
		// show song info
		if (name.split(" - ").length == 1) {
			// let str1 =  "Style.mp3-taylor";
			let arr = name.split("-");
			$("#song-name").text(arr[0].split(".")[0]);
			$("#song-singer").text(arr[1]);
		}

		if (name.split(" - ").length == 2) {
			// let str = '周杰伦 - 告白气球.flac-周杰伦'
			let arr = name.split(" - ");
			$("#song-name").text(arr[1].split(".")[0]);
			$("#song-singer").text(arr[0]);
		}
	}
	
	//js绑定事件
	au.addEventListener("loadedmetadata",function(e){
		//加载完成
		loadFlag = true;
		//alert("加载完成");
		//加载完成之后,才可以获得歌曲总时间
		//audio.currentTime //当前时间
		//audio.duration //总时间单位：秒
		var totleTime = au.duration;
		$("#totleTime").text(getTimeStr(totleTime));
		
		//加载歌词
		//根据下标获得歌词
		//[{"time":"00:00.00","lrcStr":"歌词1"},{"time":"00:05.00","lrcStr":"歌词2"}]
		var lrcList = jsonList[index].lrc;
		var lrcTemp = "";
		for(var i=0;i<lrcList.length;i++){
			var time = lrcList[i].time;
			var lrcStr = lrcList[i].lrcStr;
			var flag = "n";
			lrcTemp+="<h3 time='"+time+"' flag='"+flag+"'>"+lrcStr+"</h3>";
		}
		$(".lrc").html(lrcTemp);
		
		//切换图片
		changeImg(index);
		
		//启用播放按钮
		$("#btn2").prop("disabled",false);
		
		//根据之前的播放状态进行相关操作
		if(pausedFlag=="n"){
			//之前在播放中
			au.play();
		}
	});
	
	//播放结束,自动到下一首
	au.addEventListener("ended",function(e){
		pausedFlag="n";
		$("#btn3").click();
	});
	
	//timeupdate 播放位置改变事件
	au.addEventListener("timeupdate",function(e){
		if(touchFlag){
			//手指在滑动,直接结束
			return;
		}
		
		var currTime = au.currentTime;
		$("#currTime").text(getTimeStr(currTime));
		
		//播放进度条
		//当前播放时间/总时间*100+"%"
		var width = currTime/au.duration*100+"%";
		$(".progress-bar").css("width",width);
		
		//设置滑块的位置
		//黄色长度+进度条左边距离屏幕左边的距离-滑块的半径
		var progressBarWidth = $(".progress-bar").css("width");
		progressBarWidth = progressBarWidth.substring(0,progressBarWidth.length-2);
		var progressLeft = $(".progress").offset().left;
		
		var mySpanWidth = $(".mySpan").css("width");
		mySpanWidth = mySpanWidth.substring(0,mySpanWidth.length-2);
		mySpanWidth = mySpanWidth/2;
		$(".mySpan").css("left",parseInt(progressBarWidth)+parseInt(progressLeft)-mySpanWidth+"px");
		
		
		//歌词
		//console.log(au.currentTime);//秒
		//格式化成00:01.65
		
		//满足条件的H3
		var H3Temp = null;
		//循环h3
		var h3List = $("h3");
		// console.log(h3List.length);
		for(var i=0;i<h3List.length;i++){
			var time1 = $("h3:eq("+i+")").attr("time");
			
			var time2 = null;
			if(i<h3List.length-1){
				time2 = $("h3:eq("+(i+1)+")").attr("time");	
			}
			//将时间转换成毫秒
			time1 = parserTime(time1);
			//console.log(time);
			
			if(i<h3List.length-1){
				time2 = parserTime(time2);
				//根据区间判断
				if(au.currentTime>=time1 && au.currentTime<time2){
					H3Temp = $("h3:eq("+i+")");
					break;
				}
			}else{
				H3Temp = $("h3:eq("+i+")");
			}
		}
		//console.log(timeTemp);
		
		//$("h3").css("color","white");
		
		//将满足条件的歌词显示到中间
		//H3Temp在父容器中的纵坐标
		//console.log(H3Temp.css("color"));
		//if(H3Temp.css("color")!="rgb(255, 0, 0)"){

		if (H3Temp) {
			if(H3Temp.attr("flag")=="n"){
				var top = H3Temp.position().top;
				$(".lrcBg").animate({
					"scrollTop":top-35
				},1000);
				//console.log(top);
				H3Temp.attr("flag","y");

				$("h3").css("color","white");
				//将满足条件的歌词变成红色
				H3Temp.css("color","red");
			}
		}
		//}
		
	});
	
	//00:16.65将时间转换成秒
	function parserTime(time){
		var timeList = time.split(":");
		return parseFloat(timeList[0])*60+parseFloat(timeList[1]);
	}
	
	//触摸事件
	function setCurrTime(e,flag){
		//获得所有触摸点
		var touche = e.touches;
		//取得第一个触摸点的横坐标
		var touchX = touche[0].pageX;
		//alert(touchX);
		
		//获得进度条左边距离屏幕左边的距离(进度条的横坐标)
		var proX = $(".progress").offset().left;
		//alert(proX);
		
		//获得进度条的总长度（宽度）
		var proWidth = $(".progress").css("width");
		//截取px前面的数字
		//104.5px
		//5px
		//alert(proWidth);
		proWidth = proWidth.substring(0,proWidth.length-2);
		//alert(proWidth);
		
		//进度条的百分比=黄色长度/总长度*100%;
		//黄色长度 = 手指横坐标-进度条左边距离屏幕的距离
		var pro = (touchX-proX)/proWidth*100;
		
		//当前播放时间=歌曲总时间*pro/100
		var currTime = au.duration*pro/100;
		
		//滑块
		var mySpanWidth = $(".mySpan").css("width");
		mySpanWidth = mySpanWidth.substring(0,mySpanWidth.length-2);
		mySpanWidth = mySpanWidth/2;
		
		//限制范围 touchX 0-100
		if(touchX<proX){
			//进度条赋值
			$(".progress-bar").css("width","0%");
			//页面显示的当前播放时间
			$("#currTime").text("00:00");
			//红点位置
			$(".mySpan").css("left",proX-mySpanWidth);
			//当前播放时间
			currTime = 0;
			if(flag){
				au.currentTime = currTime;
			}
			touchCurrTime = currTime;
			return;
		}
		if(touchX>(parseFloat(proWidth)+parseFloat(proX))){
			//切换到下一首
			//进度条赋值
			$(".progress-bar").css("width","100%");
			//页面显示的当前播放时间
			$("#currTime").text(getTimeStr(au.duration));
			//红点位置
			$(".mySpan").css("left",parseInt(proX)+parseInt(proWidth)-mySpanWidth);
			//当前播放时间=歌曲总时间
			currTime = au.duration;
			if(flag){
				au.currentTime = currTime;
			}
			touchCurrTime = currTime;
			return;
		}
		
		//进度条赋值
		$(".progress-bar").css("width",pro+"%");
		
		//将当前播放时间格式化成mm:ss,赋值到div
		$("#currTime").text(getTimeStr(currTime));
		
		//将当前播放时间赋值到播放器
		//部分浏览器不支持au.currentTime赋值,解决方案,将本地歌曲换成在线歌曲
		
		//处理滑动过程中的杂音问题
		if(flag){
			au.currentTime = currTime;
		}
		touchCurrTime = currTime;
		
		//设置滑块的位置
		//手指横坐标-滑块的半径
		$(".mySpan").css("left",touchX-mySpanWidth);
	}
	
	//小红点触摸开始
	$(".mySpan")[0].addEventListener("touchstart",function(e){
		if(!loadFlag){
			return;
		}
		setCurrTime(e,true);
	});
	
	//小红点手指滑动
	$(".mySpan")[0].addEventListener("touchmove",function(e){
		touchFlag = true;
		if(!loadFlag){
			return;
		}
		setCurrTime(e,false);
	});
	
	//触摸结束
	$(".mySpan")[0].addEventListener("touchend",function(e){
		if(!loadFlag){
			return;
		}
		//触摸结束,将最新的时间赋值给播放器
		au.currentTime = touchCurrTime;
		touchFlag = false;
	});

});

function getTimeStr(time){
	var m = getTime(parseInt(time/60));
	var s = getTime(parseInt(time%60));
	var timeStr = m+":"+s;
	return timeStr;
}

function getTime(time){
	if(time<10){
		time = "0"+time;
	}
	return time;
}

function goddamnIt() {
	//
	console.log("bastard", "hhh...");
}

function go_index() {
	/*
    * 1. 单首歌曲
    * 2. 歌单中的多首歌曲
    * */
	history.back();
}

/**
 * 歌曲或者歌单id
 * @returns {number} id
 */
function get_id() {
	let param = location.search;
	let str = param.substr(param.indexOf('?') + 1);

	let id = 0;
	if (str.indexOf("&") == -1) {
		let arr = str.split("=");
		id = arr[1]; // 单首歌曲id
	} else {
		let arr = str.split("&");
		let list_id = arr[0].split("=")[1];
		let song_id = arr[1].split("=")[1];
		id = list_id + "_" + song_id;
	}

	return id;
}

/**
 * 是歌单还是歌曲id
 * @returns {string: list-id or id}
 */
/*function get_mark() {
	let param = location.search;
	let str = param.substr(param.indexOf('?') + 1);

	let mark = "";

	if (str.indexOf("&") == -1) {
		let arr = str.split("=");
		mark = arr[0];
	}

	return mark;
}*/

function get_song_id() {

}

$(function () {

});

/*http://www.mvpdj.com/media/attachment/track/201704/20170423_179008566358fc3d2c9bfcf.mp3*/
/*
[
                   {"name":"Style-taylor swift","img":"img/list8.jpg","url":"music-resource/Style.mp3", "lrc":[{"time":"00:00.01", "lrcStr":"暂无歌词"}]},
                   {"name":"Spark Fly-taylor swift","img":"img/list8.jpg","url":"music-resource/Sparks Fly.mp3",
                       "lrc":[{"time":"00:00.00","lrcStr":"第二首歌歌词1"},{"time":"00:05.00","lrcStr":"第二首歌歌词2"},{"time":"00:07.00","lrcStr":"第二首歌歌词3"},{"time":"00:10.00","lrcStr":"第二首歌歌词4"},{"time":"00:12.00","lrcStr":"第二首歌歌词5"},{"time":"00:15.00","lrcStr":"第二首歌歌词6"}]}
                   ];*/
/*var jsonList = [{"name":"Wolves-Selena Gomez","img":"img/list8.jpg","url":"http://music.163.com/song/media/outer/url?id=1468003511","lrc":[{"time":"00:00.00","lrcStr":"歌词1"},{"time":"00:05.00","lrcStr":"歌词2"},{"time":"00:07.00","lrcStr":"歌词3"},{"time":"00:10.00","lrcStr":"歌词4"},{"time":"00:12.00","lrcStr":"歌词5"},{"time":"00:15.00","lrcStr":"第一首歌歌词6"},{"time":"00:17.00","lrcStr":"第一首歌歌词7"},{"time":"00:19.00","lrcStr":"第一首歌歌词8"},{"time":"00:21.00","lrcStr":"第一首歌歌词9"},{"time":"00:25.00","lrcStr":"第一首歌歌词6"},{"time":"00:15.00","lrcStr":"第一首歌歌词6"},{"time":"00:15.00","lrcStr":"第一首歌歌词6"},{"time":"00:15.00","lrcStr":"第一首歌歌词6"},{"time":"00:15.00","lrcStr":"第一首歌歌词6"},{"time":"00:15.00","lrcStr":"第一首歌歌词6"},{"time":"00:15.00","lrcStr":"第一首歌歌词6"}]},
                {"name":"尽头-赵方婧","img":"img/music/33.jpg","url":"http://www.ytmp3.cn/down/44145.mp3","lrc":[{"time":"00:00.00","lrcStr":"第二首歌歌词1"},{"time":"00:05.00","lrcStr":"第二首歌歌词2"},{"time":"00:07.00","lrcStr":"第二首歌歌词3"},{"time":"00:10.00","lrcStr":"第二首歌歌词4"},{"time":"00:12.00","lrcStr":"第二首歌歌词5"},{"time":"00:15.00","lrcStr":"第二首歌歌词6"}]},
                {"name":"勋章-鹿晗","img":"img/music/44.jpg","url":"http://www.ytmp3.cn/down/44545.mp3","lrc":[{"time":"00:00.00","lrcStr":"第三首歌歌词1"},{"time":"00:05.00","lrcStr":"第三首歌歌词2"},{"time":"00:07.00","lrcStr":"第三首歌歌词3"},{"time":"00:10.00","lrcStr":"第三首歌歌词4"},{"time":"00:12.00","lrcStr":"第三首歌歌词5"},{"time":"00:15.00","lrcStr":"第三首歌歌词6"}]}];*/
