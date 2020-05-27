package com.lyx.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lyx.common.CommonResult;
import com.lyx.dto.ClotheSaveDto;
import com.lyx.entity.Clothes;

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
	CommonResult save(ClotheSaveDto dto);

	CommonResult remove(int id);

	CommonResult listByKind(int kind);
}