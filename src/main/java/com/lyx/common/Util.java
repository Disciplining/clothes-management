package com.lyx.common;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import java.io.File;

public class Util
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
	 * 判断文件是否是一个图片
	 * @return true-是图片 false-不是图片
	 */
	public static boolean isPicFile(File file)
	{
		String type = getType(FileUtil.getName(file));
		if (StrUtil.isBlank(type))
		{
			return false;
		}

		switch (type)
		{
			case ImgUtil.IMAGE_TYPE_GIF:
				return true;
			case ImgUtil.IMAGE_TYPE_JPG:
				return true;
			case ImgUtil.IMAGE_TYPE_JPEG:
				return true;
			case ImgUtil.IMAGE_TYPE_BMP:
				return true;
			case ImgUtil.IMAGE_TYPE_PNG:
				return true;
			case ImgUtil.IMAGE_TYPE_PSD:
				return true;
			default:
				return false;
		}
	}
}