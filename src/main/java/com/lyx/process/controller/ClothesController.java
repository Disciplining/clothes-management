package com.lyx.process.controller;


import com.lyx.common.CommonResult;
import com.lyx.dto.ClotheSaveDto;
import com.lyx.entity.Clothes;
import com.lyx.process.service.IClothesService;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

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

	@PostMapping("/save/uploadpic")
	@ApiModelProperty("增加衣物，上传图片")
	public CommonResult saveUploadPic(@RequestParam("file") MultipartFile pic, HttpServletResponse response)
	{
		return service.saveUploadPic(pic, response);
	}

	@PostMapping("/save/form")
	@ApiModelProperty("增加衣物，提交表单")
	public CommonResult saveForm(@RequestBody Clothes clothes)
	{
		return service.saveForm(clothes);
	}
























	@PostMapping("/save")
	@ApiOperation("添加一件衣物")
	public CommonResult save(ClotheSaveDto dto)
	{
		return service.save(dto);
	}

	@DeleteMapping("/remove/{id}")
	@ApiModelProperty("删除某个衣物")
	public CommonResult remove(@PathVariable("id") int id)
	{
		return service.remove(id);
	}

	@PutMapping("/sequence/{id}")
	@ApiModelProperty("修改衣物排序")
	public CommonResult changeSequence(@PathVariable("id") int id, @RequestParam boolean up)
	{
		return service.changeSequence(id, up);
	}

	@GetMapping("/list/{kind}")
	@ApiOperation("获得某类衣物")
	public CommonResult listKind(@PathVariable("kind") int kind)
	{
		return service.listKind(kind);
	}
}