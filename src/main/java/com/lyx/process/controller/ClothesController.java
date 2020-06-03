package com.lyx.process.controller;


import com.lyx.common.CommonResult;
import com.lyx.dto.ClothesSaveDto;
import com.lyx.process.service.IClothesService;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

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

	@PostMapping("/save")
	@ApiModelProperty("增加衣物")
	public CommonResult uploadClothes(ClothesSaveDto dto)
	{
		return service.saveClothes(dto);
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

	@PutMapping("/sequence-fol/{id}")
	@ApiModelProperty("修改衣物排序")
	public CommonResult changeSequenceFirstOrLast(@PathVariable("id") int id, @RequestParam boolean isFirst)
	{
		return service.changeSequenceFirstOrLast(id, isFirst);
	}

	@GetMapping("/list/{kind}")
	@ApiOperation("获得某类衣物")
	public CommonResult listKind(@PathVariable("kind") int kind)
	{
		return service.listKind(kind);
	}
}