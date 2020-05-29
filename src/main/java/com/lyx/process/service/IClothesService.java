package com.lyx.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lyx.common.CommonResult;
import com.lyx.entity.Clothes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 衣物表 服务类
 * </p>
 *
 * @author powered by lyx
 * @since 2020-05-27
 */
public interface IClothesService extends IService<Clothes>
{
	/**
	 * 增加衣物，上传照片
	 */
	CommonResult saveUploadPic(MultipartFile pic, HttpServletResponse response);

	/**
	 * 增加衣物，提交表单
	 */
	CommonResult saveForm(Clothes clothes);

	CommonResult remove(int id);

	/**
	 * 更改衣物的排序
	 * @param up 这个衣物是否是上升
	 */
	CommonResult changeSequence(int id, boolean up);

	CommonResult listKind(int kind);
}