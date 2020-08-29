package com.lovely.action.upload;


import com.jspsmart.upload.Files;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

/**
 * 歌单图片上传！ 歌曲图片上传！ in /music-manage/img
 * use smart_upload upload (song_list/song cover image) files..
 */
@WebServlet(name = "imgUpload", urlPatterns = "/imgUpload")
public class ImgUpload extends HttpServlet {

    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        //1.创建文件上传对象
        SmartUpload su = new SmartUpload();
        //2.初始化文件上传对象
        su.initialize(getServletConfig(), req, resp);
        //3.设置相关属性（限制条件）

        su.setCharset("utf-8");
        //设置允许上传的文件类型
        su.setAllowedFilesList("jpg,png,bmp");
        //su.setAllowedFilesList("txt");
        //设置允许上传的文件的最大值（单位字节）
        //设置 2MB
        su.setMaxFileSize(1024*1024*2);
        //设置不允许上传的文件类型
        //try {
        //	su.setDeniedFilesList("zip,7z");
        //} catch (SQLException e) {
        //	e.printStackTrace();
        //}
        try {
            //4.文件上传
            su.upload();

            //取其他数据
            //必须在upload()方法之后
            //使用su.getRequest()
            System.out.println("用户名:"+su.getRequest().getParameter("userName"));

            //5.获得上传文件
            Files files = su.getFiles();
            com.jspsmart.upload.File f = files.getFile(0);

            //6.保存文件到服务器指定目录
            //使用Servlet上下文对象获得项目的根路径（真实路径）
            String filePath = getServletContext().getRealPath("/music-manage/img");
            System.out.println("项目根路径："+filePath);

            File file = new File(filePath);
            //判断文件夹是否存在
            if(!file.exists()){
                file.mkdirs();
            }

            //保存文件
            //生成全球唯一字符串UUID.randomUUID())
            //获得文件后缀 f.getFileExt()
            String fileName = UUID.randomUUID()+"."+f.getFileExt();
            filePath = filePath+"/"+ fileName;

            f.saveAs(filePath);

            System.out.println("上传成功");

            resp.setContentType("text/plain;charset=utf-8");
            PrintWriter out = resp.getWriter();
            out.print(fileName);
            out.close();
        } catch (SmartUploadException e) {
            e.printStackTrace();
            System.out.println("文件上传失败");
        }

    }
}
