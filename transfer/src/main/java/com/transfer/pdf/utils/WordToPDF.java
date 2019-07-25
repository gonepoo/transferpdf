package com.transfer.pdf.utils;

import com.aspose.slides.p2cbca448.bos;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class WordToPDF {

    HttpServletResponse response = null;

    public static boolean getLicense() {
        boolean result = false;
        try {
            InputStream is = WordToPDF.class.getClassLoader().getResourceAsStream("\\license.xml");
            License aposeLic = new License();
            aposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void word2pdf(InputStream inputStream, OutputStream outputStream) {
        String pdfPath = "E:/pdf.pdf";
        if (!getLicense()) {
            return ;
        }
        try {
            long old = System.currentTimeMillis();

            com.aspose.words.Document document = new com.aspose.words.Document(inputStream);
            document.save(new FileOutputStream(new File(pdfPath)), SaveFormat.PDF);

            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(pdfPath));
            byte[] readBytes = new byte[1024];
            int readResult = 0;
            while((readResult = bis.read(readBytes)) != -1){
                outputStream.write(readBytes, 0, readResult); //高效字节流一次读写一个字节数组
            }
            outputStream.flush();

            long now = System.currentTimeMillis();
            System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
