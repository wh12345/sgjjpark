package com.qy.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;

/**
 * ftp方式获取
 */
@Slf4j
public class FtpUtils {
	
	/**
	 * 初始化ftp服务器
	 * @param ip
	 * @param port
	 * @param username
	 * @param password
	 */
	public static FTPClient initFtp(String ip,Integer port,String username,String password) {
		FTPClient ftp = new FTPClient();
		ftp.setControlEncoding("utf-8");
		try {
			ftp.connect(ip, port);
			ftp.login(username, password);
			int replyCode = ftp.getReplyCode();
			if(!FTPReply.isPositiveCompletion(replyCode)) {
				log.info("连接ftp服务器失败： 用户名"+username+" : 密码"+password);
				return null;
			}
		} catch (SocketException e) {
			e.printStackTrace();
			ftp =null;
		} catch (IOException e) {
			e.printStackTrace();
			ftp =null;
		}finally {
			return ftp;
		}
	}

	/**
	 * 精确下载图片
	 * @param ftp
	 * @param ftppath /test/
	 * @param filename
	 * @param path
	 */
	public static void downloadImg(FTPClient ftp,String ftppath,String filename,String path) {
		OutputStream os = null;
		try {
			ftp.changeWorkingDirectory(ftppath);
			FTPFile[] ftpFiles = ftp.listFiles();
			for (FTPFile file : ftpFiles) {
				if(filename.equalsIgnoreCase(file.getName())) {
					log.info("ftp开始下载文件"+path+ File.separator+filename);
					os = new FileOutputStream(path+ File.separator+filename);
					ftp.retrieveFile(file.getName(), os);
					os.flush();
				}
			}
		} catch (IOException e) {
			log.info("ftp下载文件出现异常"+e.getMessage());
			e.printStackTrace();
		}finally {
			try {
				if(os!=null) {
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(ftp!=null) {
					ftp.logout();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 模糊下载图片
	 * @param ftp
	 * @param ftppath /test/
	 * @param dateStr
	 * @param imgpath
	 */
	public static void downloadImgByDate(FTPClient ftp,String ftppath,String dateStr,String imgpath) {
		OutputStream os = null;
		try {
			ftp.changeWorkingDirectory(ftppath);
			FTPFile[] ftpFiles = ftp.listFiles();
			for (FTPFile file : ftpFiles) {
				if(file.getName().contains(dateStr)) {
					log.info("ftp开始下载文件:ftppath-"+ftppath+"  dateStr-"+dateStr+" imgpath:"+imgpath);
					os = new FileOutputStream(imgpath);
					ftp.retrieveFile(file.getName(), os);
					os.flush();
				}
			}
		} catch (IOException e) {
			log.info("ftp下载文件出现异常"+e.getMessage());
			e.printStackTrace();
		}finally {
			try {
				if(os!=null) {
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(ftp!=null) {
					ftp.logout();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
