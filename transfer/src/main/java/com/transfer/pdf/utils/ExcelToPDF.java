package com.transfer.pdf.utils;

import com.aspose.cells.License;
import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;

import java.io.*;

public class ExcelToPDF {

    public static boolean getLicense() {
        boolean result = false;
        try {
            InputStream is = ExcelToPDF.class.getClassLoader().getResourceAsStream("\\license.xml");
            License aposeLic = new License();
            aposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void excel2pdf(InputStream inputStream, OutputStream outputStream){
        String pdfPath = "E:/pdf.pdf";
        if (!getLicense()) {
            return ;
        }
        try {
            long old = System.currentTimeMillis();

            Workbook wb = new Workbook(inputStream);// 原始excel路径
            wb.save(new FileOutputStream(new File(pdfPath)),SaveFormat.PDF);

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
