package com.lyx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 衣物表
 * </p>
 *
 * @author powered by lyx
 * @since 2020-05-27
 */
@Data
@Accessors(chain = true)
@TableName("clothes")
@ApiModel(value = "Clothes对象", description = "衣物表")
public class Clothes implements Serializable
{
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键")
	@TableId(value = "c_id", type = IdType.AUTO)
	private Integer cId;

	@ApiModelProperty(value = "衣物名称")
	@TableField("c_name")
	private String cName;

	@ApiModelProperty(value = "衣物种类：0-上衣 1-下衣 2-鞋子")
	@TableField("kind")
	private Integer kind;

	@ApiModelProperty(value = "衣物照片")
	@TableField("url")
	private String url;

	@ApiModelProperty(value = "衣物排序，越小排在越前边")
	@TableField("sequence")
	private Integer sequence;
}