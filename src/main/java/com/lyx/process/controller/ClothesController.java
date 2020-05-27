package com.lyx.process.controller;


import com.lyx.common.CommonResult;
import com.lyx.dto.ClotheSaveDto;
import com.lyx.process.service.IClothesService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 衣物表 前端控制器
 * </p>
 *
 * @author powered by lyx
 * @since 2020-05-27
 */
@RestController
@RequestMapping("/clothes")
public class ClothesController
{
	@Autowired
	@Qualifier("clothesServiceImpl")
	private IClothesService service;

	@ApiOperation("添加一件衣物")
	@PostMapping("/save")
	public CommonResult save(ClotheSaveDto dto)
	{
		return service.save(dto);
	}
}