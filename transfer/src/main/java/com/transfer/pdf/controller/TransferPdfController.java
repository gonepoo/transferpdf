package com.transfer.pdf.controller;


import com.transfer.pdf.utils.ExcelToPDF;
import com.transfer.pdf.utils.PptToPDF;
import com.transfer.pdf.utils.WordToPDF;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@RestController
public class TransferPdfController {

    String path1 = "D:/pdftestfile/word.docx";
    String path2 = "D:/pdftestfile/word2pdf.pdf";
    String path3 = "D:/pdftestfile/excel.xlsx";
    String path4 = "D:/pdftestfile/excel2pdf.pdf";
    String path5 = "D:/pdftestfile/ppt.pptx";
    String path6 = "D:/pdftestfile/ppt2pdf.pdf";

    @RequestMapping("/word2pdf")
    public void word2pdf(HttpServletRequest request, HttpServletResponse response) {
        exportPdf(request, response, path1, path2);
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            WordToPDF.word2pdf(new FileInputStream(path1), os);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @RequestMapping("/excel2pdf")
    public void excel2pdf(HttpServletRequest request, HttpServletResponse response) {
        exportPdf(request, response, path3, path4);
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            ExcelToPDF.excel2pdf(new FileInputStream(path3), os);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @RequestMapping("/ppt2pdf")
    public void ppt2pdf(HttpServletRequest request, HttpServletResponse response) {
        exportPdf(request, response, path5, path6);
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            PptToPDF.ppt2pdf(new FileInputStream(path5), os);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void exportPdf(HttpServletRequest request, HttpServletResponse response, String sourcePath, String targetPath){
        File file = new File(targetPath);
        String fileName = file.getName();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        String agent = (String) request.getHeader("USER-AGENT"); //判断浏览器类型
        try {
            if (agent != null && agent.indexOf("Fireforx") != -1) {
                fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");   //UTF-8编码，防止输出文件名乱码
            } else {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.reset();
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/pdf"); // word格式
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
    }
}
