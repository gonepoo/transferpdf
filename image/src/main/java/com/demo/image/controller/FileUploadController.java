package com.demo.image.controller;
import com.demo.image.convert.ImageToChar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * 图片上传的几种方式
 */
@Controller
@RequestMapping("/uploads")
public class FileUploadController {

    private static final Logger log = LoggerFactory.getLogger(FileUploadController.class);
    public String suffix = "";
    public String uuid = "";
    String path1 = winOrLin()?"/own/upload/" : "E:/upload/";
    String path2 = winOrLin()?"/own/download/" : "E:/download/";

    public boolean winOrLin(){
        Properties prop = System.getProperties();
        String os = prop.getProperty("os.name");
        if (os != null && os.toLowerCase().indexOf("linux") > -1) {
            return true;
        } else {
            return false;
        }
    }

    @GetMapping
    public String index() {
        return "index";
    }


    @PostMapping("/upload1")
    @ResponseBody
    public void upload1(@RequestParam("file") MultipartFile file, String text, int number, int color) throws IOException {
        log.info("[文件类型] - [{}]", file.getContentType());
        log.info("[文件名称] - [{}]", file.getOriginalFilename());
        log.info("[文件大小] - [{}]", file.getSize());
        // TODO 将文件写入到指定目录（具体开发中有可能是将文件写入到云存储/或者指定目录通过 Nginx 进行 gzip 压缩和反向代理，此处只是为了演示故将地址写成本地电脑指定目录）
        String[] strArr = file.getOriginalFilename().split("\\.");
        suffix = strArr[strArr.length - 1];
        uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();

        if(!"gif".equals(suffix)){
            String imagePath = "", imageOutPath = "";
            imagePath = path1 + uuid + "." + suffix;
            imageOutPath = path2 + uuid + "." + suffix;
            file.transferTo(new File(imagePath));
            ImageToChar.loadIMage(imagePath, imageOutPath, text, number, color);//动图转为动态的字符图片
        }else if("gif".equals(suffix)){
            String gifPath = "", gifOutPath = "";
            gifPath = path1 + uuid + "." + suffix;
            gifOutPath = path2 + uuid + "." + suffix;
            file.transferTo(new File(gifPath));
            ImageToChar.loadGif(gifPath, gifOutPath, text, number, color);//动图转为动态的字符图片
        }else{
            log.info("格式不正确，必须是图片格式", file.getOriginalFilename());
        }
        Map<String, String> result = new HashMap<>(16);
        result.put("contentType", file.getContentType());
        result.put("fileName", file.getOriginalFilename());
        result.put("fileSize", file.getSize() + "");
    }

    @PostMapping("/upload2")
    @ResponseBody
    public List<Map<String, String>> upload2(@RequestParam("file") MultipartFile[] files) throws IOException {
        if (files == null || files.length == 0) {
            return null;
        }
        List<Map<String, String>> results = new ArrayList<>();
        for (MultipartFile file : files) {
            // TODO Spring Mvc 提供的写入方式
            file.transferTo(new File("E:\\upload\\" + file.getOriginalFilename()));
            Map<String, String> map = new HashMap<>(16);
            map.put("contentType", file.getContentType());
            map.put("fileName", file.getOriginalFilename());
            map.put("fileSize", file.getSize() + "");
            results.add(map);
        }
        return results;
    }

    @PostMapping("/upload3")
    @ResponseBody
    public void upload2(String base64) throws IOException {
        // TODO BASE64 方式的 格式和名字需要自己控制（如 png 图片编码后前缀就会是 data:image/png;base64,）
        final File tempFile = new File("E:\\upload\\test.jpg");
        // TODO 防止有的传了 data:image/png;base64, 有的没传的情况
        String[] d = base64.split("base64,");
        final byte[] bytes = Base64Utils.decodeFromString(d.length > 1 ? d[1] : d[0]);
        FileCopyUtils.copy(bytes, tempFile);

    }

    @GetMapping("/download")
    @ResponseBody
    public String downloadFile(HttpServletResponse response) {
       String fileName = uuid + "." + suffix;
        if (fileName != null && !"".equals(fileName)) {
            //设置文件路径
            File file = new File(path2 + fileName);
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                OutputStream os = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    os.close();
                    bis.close();
                    fis.close();
                    return "下载成功";
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (os != null) {
                        try {
                            os.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return "下载失败";
    }
}
