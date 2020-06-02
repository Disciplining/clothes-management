package com.lyx.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lyx.common.CommonResult;
import com.lyx.dto.ClothesSaveDto;
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
	CommonResult uploadClothes(ClothesSaveDto dto);

	CommonResult remove(int id);

	/**
	 * 更改衣物的排序
	 * @param up 这个衣物是否是上升
	 */
	CommonResult changeSequence(int id, boolean up);

	/**
	 * 将衣物移到最前或最后
	 * @param isFirst true-移到最前 false-移到最后
	 */
	CommonResult changeSequenceFirstOrLast(int id, boolean isFirst);

	CommonResult listKind(int kind);
}