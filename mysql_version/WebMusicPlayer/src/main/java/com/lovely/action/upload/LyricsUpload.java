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
 * 歌词上传/lyrics upload
 * in /music-resource/lyrics/
 * @author echo lovely
 * @date 2020/8/23 14:27
 */
@WebServlet(name = "lyricsUpload", urlPatterns = "/lyricsUpload")
public class LyricsUpload extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        SmartUpload su = new SmartUpload();

        su.initialize(getServletConfig(), req, resp);

        su.setCharset("utf-8");

        try {
            su.upload();

            System.out.println("歌曲名:"+su.getRequest().getParameter("song-lyrics"));

            Files files = su.getFiles();
            com.jspsmart.upload.File f = files.getFile(0);

            // /music-resource/lyrics/ 下
            String filePath = getServletContext().getRealPath("/music-resource/lyrics");

            System.out.println("项目根路径："+filePath);

            File file = new File(filePath);

            if(!file.exists()){
                file.mkdirs();
            }

            // 歌词名
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
