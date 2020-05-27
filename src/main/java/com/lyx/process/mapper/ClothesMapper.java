package com.lyx.process.mapper;

import com.lyx.entity.Clothes;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 衣物表 Mapper 接口
 * </p>
 *
 * @author powered by lyx
 * @since 2020-05-27
 */
@Mapper
@Repository("clothesMapper")
public interface ClothesMapper extends BaseMapper<Clothes>
{
}