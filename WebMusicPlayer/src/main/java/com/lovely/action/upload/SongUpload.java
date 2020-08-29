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

/**
 * 歌曲上传 song upload
 * in /music-resource/
 * @author echo lovely
 * @date 2020/8/23 14:16
 */
@WebServlet(name = "songUpload", value = "/songUpload")
public class SongUpload extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        SmartUpload su = new SmartUpload();

        su.initialize(getServletConfig(), req, resp);

        su.setCharset("utf-8");

        try {

            su.upload();

            System.out.println("歌曲名:"+su.getRequest().getParameter("song-title"));

            Files files = su.getFiles();
            com.jspsmart.upload.File f = files.getFile(0); // get form input[0]

            String filePath = getServletContext().getRealPath("/music-resource");
            System.out.println("项目根路径："+filePath);

            File file = new File(filePath);
            //判断文件夹是否存在
            if(!file.exists()){
                file.mkdirs();
            }

            // 歌曲名
            String fileName = su.getRequest().getParameter("song-title")+"."+f.getFileExt();
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
