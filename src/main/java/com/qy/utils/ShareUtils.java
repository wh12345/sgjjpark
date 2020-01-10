package com.qy.utils;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import java.io.*;
import java.net.MalformedURLException;

/**
 * 共享方式
 * Created by Because of you on 2020/1/1.
 */
public class ShareUtils {
    /**
     * 下载共享的图片
     * @param oldpath
     * @param newpath
     * @param filename
     */
    public static void downloadFile(String oldpath,String newpath,String filename) {
        SmbFile remoteFile = null;
        InputStream in = null;
        OutputStream out = null;
        try {
            remoteFile = new SmbFile(oldpath+filename);
            remoteFile.connect();
            if(remoteFile!=null) {
                in = new BufferedInputStream(new SmbFileInputStream(remoteFile));
                out = new BufferedOutputStream(new FileOutputStream(newpath+ File.separator+filename));
                byte[] buffer = new byte[1024];
                while (in.read(buffer) != -1)
                {
                    out.write(buffer);
                    buffer = new byte[1024];
                }
            }
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(out!=null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(in!=null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }

}
