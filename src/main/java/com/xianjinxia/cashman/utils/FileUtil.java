package com.xianjinxia.cashman.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 文件帮助类
 * @author zzc
 *
 */
public class FileUtil {
	/**
	 * 读取文件内容
	 *
	 * @param
	 * @return void
	 * @throws Exception
	 * @throws
	 */
	public static String readFileContent(File file) throws Exception {
		//读取文件内容
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			byte[] temp = new byte[500];
			ByteArrayOutputStream baos = new ByteArrayOutputStream();// 写内容到baos中
			if (bis.available() < 500) {
				temp = new byte[bis.available()];
			}
			while (bis.available() > 0 && (bis.read(temp)) != -1) {
				baos.write(temp);
				if (bis.available() < 500) {
					temp = new byte[bis.available()];
				} else {
					temp = new byte[500];
				}
			}
			fis.close();
			bis.close();
			byte[] bb = baos.toByteArray();
			return new String(bb, "utf-8");
		} catch (FileNotFoundException e) {
			throw new Exception("找不到文件模板！");
		} catch (IOException e) {
			throw new Exception("找不到文件模板！");
		}
	}

	/***
	 * 根据图片路径将图片转成Base64数据
	 * @return Base64数据
	 */
	public static String GetImageStr(String imgFilePath) {//将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		//String imgFile = "E://0010001//2014-04-23//jk602745.jpg";//待处理的图片
		InputStream in = null;
		byte[] data = null;
		//读取图片字节数组
		try {
			in = new FileInputStream(imgFilePath);
			data = new byte[in.available()];
			//System.out.println(in.available());
			in.read(data);
			in.close();
		} catch (IOException e) {
			System.out.println("上传的印章图片转sealData错误：" + e.getMessage());
			e.printStackTrace();
		}
		//对字节数组Base64编码
		byte[] en = Base64.encodeBase64(data);
		return new String(en);//返回Base64编码过的字节数组字符串
	}

	/***
	 * 根据图片Base64数据生成图片文件
	 * @param imgStr
	 * @param imgFilePath
	 * @return
	 */
	public static boolean GenerateImage(String imgStr, String imgFilePath) {//对字节数组字符串进行Base64解码并生成图片
		if (imgStr == null) //图像数据为空  
			return false;
		try {
			//Base64解码

			byte[] b = Base64.decodeBase64(new String(imgStr).getBytes());

			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {//调整异常数据
					b[i] += 256;
				}
			}
			//生成jpeg图片
			//String imgFilePath = "d://221.jpg";//新生成的图片
			OutputStream out = new FileOutputStream(imgFilePath);
			out.write(b);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			System.out.println("sealData转印章图片错误：" + e.getMessage());
			//e.printStackTrace();
			return false;
		}
	}

	/***
	 * PDF转图片
	 */
	public static boolean pdf2Image(String PdfFilePath, String imgFilePath) {

		File file = new File(PdfFilePath);
		File dstFile = new File(imgFilePath);
		PDDocument pdDocument;
		try {
			pdDocument = PDDocument.load(file);
			PDFRenderer renderer = new PDFRenderer(pdDocument);
			/* 0表示第一页，300表示转换dpi，dpi越大转换后越清晰，相对转换速度越慢 */
			BufferedImage image = renderer.renderImageWithDPI(0, 300);
			ImageIO.write(image, "png", dstFile);
			return true;
		} catch (IOException e) {
			System.out.println("PDF转图片错误：" + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}


	public static void getFile(byte[] bfile, String path, String fileName) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			File dir = new File(path);
			//判断文件目录是否存在
			if (!dir.exists() && dir.isDirectory()) {
				dir.mkdirs();
			}
			file = new File(fileName);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(bfile);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}


	public static byte[] getBytes(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}
}
