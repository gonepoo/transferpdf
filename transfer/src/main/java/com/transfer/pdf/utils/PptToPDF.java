package com.transfer.pdf.utils;

import com.aspose.slides.License;
import com.aspose.slides.Presentation;
import com.aspose.slides.SaveFormat;

import java.io.*;

public class PptToPDF {

    public static boolean getLicense() {
        boolean result = false;
        try {
            InputStream is = PptToPDF.class.getClassLoader().getResourceAsStream("\\license.xml");
            License aposeLic = new License();
            aposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void ppt2pdf(InputStream inputStream, OutputStream outputStream){
        String pdfPath = "E:/pdf.pdf";
        if (!getLicense()) {
            return ;
        }
        try {
            long old = System.currentTimeMillis();

            Presentation pres = new Presentation(inputStream);
            pres.save(new FileOutputStream(new File(pdfPath)),SaveFormat.Pdf);

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
            e.printStackTrace();
        }finally {
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
