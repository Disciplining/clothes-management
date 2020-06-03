package com.lyx.common;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.web.multipart.MultipartFile;

public class CommonUtil
{
	/**
	 * 根据文件名获得文件类型
	 */
	public static String getType(String fileName)
	{
		int index = fileName.lastIndexOf('.');
		if (index == -1)
		{
			return "";
		}
		return fileName.substring(index+1);
	}

	/**
	 * 根据文件名，判断文件是否是一个图片
	 * @return true-是图片 false-不是图片
	 */
	public static boolean isPicFile(String fileName)
	{
		String type = getType(fileName);
		if (StrUtil.isBlank(type))
			return false;

		if( StrUtil.equalsIgnoreCase(type, ImgUtil.IMAGE_TYPE_GIF))
			return true;
		if( StrUtil.equalsIgnoreCase(type, ImgUtil.IMAGE_TYPE_JPG))
			return true;
		if( StrUtil.equalsIgnoreCase(type, ImgUtil.IMAGE_TYPE_JPEG))
			return true;
		if( StrUtil.equalsIgnoreCase(type, ImgUtil.IMAGE_TYPE_BMP))
			return true;
		if( StrUtil.equalsIgnoreCase(type, ImgUtil.IMAGE_TYPE_PNG))
			return true;

		return false;
	}

	/**
	 * 计算 MultipartFile 类型文件的大小
	 * @return 文件大小，单位是 MB
	 */
	public static double fileSize(MultipartFile file)
	{
		try
		{
			byte[] bytes = file.getBytes();
			double size = bytes.length / (1024.0D*1024.0D);

			return size;
		}
		catch (Exception e)
		{
			System.out.println("出现异常：" + e.getMessage());

			return 0D;
		}
	}

	/**
	 * 判断类型的数字是否合法
	 * true-合法 false-不合法
	 */
	public static boolean kindIsOk(int kind)
	{
		if ( (kind!=Constant.ClotheSKind.DOWN_CLOTHES) &&
				(kind!=Constant.ClotheSKind.UP_CLOTHES) &&
				(kind!=Constant.ClotheSKind.SHOES) )
		{
			return false;
		}

		return true;
	}
}