package com.wisdom.jsextendlib.utils;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Administrator
 * @ProjectName project： EHarbinCityTangram
 * @class package：com.wisdom.hrb.ebc.cordova.plugins.utils
 * @class describe：
 * @time 2023/7/11 16:23
 * @change
 */
public class Base64Utils {
    /**
     * 将图片转换成Base64编码的字符串
     */
    public static String imageToBase64(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        InputStream is = null;
        byte[] data = null;
        String result = null;
        try {
            is = new FileInputStream(path);
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
    }



   /**
    * 将Base64编码转换为图片
    * @param base64Str
    * @param path
    * @return true
    */
   public static boolean base64ToFile(String base64Str,String path) {
      byte[] data = Base64.decode(base64Str,Base64.NO_WRAP);
      for (int i = 0; i < data.length; i++) {
         if(data[i] < 0){
            //调整异常数据
            data[i] += 256;
         }
      }
      OutputStream os = null;
      try {
         os = new FileOutputStream(path);
         os.write(data);
         os.flush();
         os.close();
         return true;
      } catch (FileNotFoundException e) {
         e.printStackTrace();
         return false;
      }catch (IOException e){
         e.printStackTrace();
         return false;
      }
   }


    public static String bitmapToBase64(Bitmap bitmap){
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                baos.flush();
                baos.close();
                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.NO_WRAP);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }




}
