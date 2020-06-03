package com.lyx.process.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyx.common.CommonResult;
import com.lyx.common.CommonUtil;
import com.lyx.config.QiniuOSS;
import com.lyx.dto.ClothesSaveDto;
import com.lyx.entity.Clothes;
import com.lyx.process.mapper.ClothesMapper;
import com.lyx.process.service.IClothesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 衣物表 服务实现类
 * </p>
 *
 * @author powered by lyx
 * @since 2020-05-27
 */
@Service("clothesServiceImpl")
public class ClothesServiceImpl extends ServiceImpl<ClothesMapper, Clothes> implements IClothesService
{
	@Autowired
	@Qualifier("qiniuOSS")
	private QiniuOSS qiniuOSS;

	@Value("${clothes-pic-max-size-mb}")
	private double picMaxSize;

	@Override
	public CommonResult uploadClothes(ClothesSaveDto dto)
	{
		try
		{
			// 检查参数
			if (StrUtil.isBlank(dto.getCName()))
				return CommonResult.errorMsg("衣物名称不能为空");
			if (Objects.isNull(dto.getKind()) || !CommonUtil.kindIsOk(dto.getKind()))
				return CommonResult.errorMsg("衣物类型不正确");
			if (Objects.isNull(dto.getPicFile()))
				return CommonResult.errorMsg("必须上传衣物图片");
			if (!CommonUtil.isPicFile(dto.getPicFile().getOriginalFilename()))
				return CommonResult.errorMsg("上传的不是图片");
			BufferedImage bufferedImage = ImageIO.read(dto.getPicFile().getInputStream());
			if (bufferedImage.getWidth()!=430 || bufferedImage.getWidth()!=430)
				return CommonResult.errorMsg("衣物图片的尺寸必须为 430px*430px");
			if (CommonUtil.fileSize(dto.getPicFile()) > this.picMaxSize)
				return CommonResult.errorMsg("衣物图片的大小最大为 " + this.picMaxSize + "MB");

			// 上传图片到OSS
			String url = qiniuOSS.uploadClothesPic(dto.getPicFile(), dto.getKind());
			if (StrUtil.isBlank(url))
				return CommonResult.errorMsg("图片上传失败");

			// 在数据库中生成记录
			Clothes clothes = new Clothes();
			clothes.setCName(dto.getCName());
			clothes.setKind(dto.getKind());
			clothes.setUrl(url);
			clothes.setSequence(this.getFirstOrSequence(clothes.getKind(), false) + 1);

			return this.save(clothes) ? CommonResult.success() : CommonResult.errorMsg("添加数据失败");
		}
		catch (Exception e)
		{
			return CommonResult.errorMsg("发生异常：" + e.getMessage());
		}
	}

	@Override
	public CommonResult remove(int id)
	{
		// 获得key
		Clothes clothes = this.getById(id);
		if (Objects.isNull(clothes))
		{
			return CommonResult.errorMsg("衣物不存在");
		}
		String url = clothes.getUrl();
		String key = url.substring(url.lastIndexOf("clothes"));

		// 删除文件
		if (!qiniuOSS.delete(key))
		{
			return CommonResult.errorMsg("删除文件失败");
		}

		// 删除记录
		this.removeById(id);

		return CommonResult.successMsg("删除成功");
	}

	@Override
	public CommonResult changeSequence(int id, boolean up)
	{
		if (up)
		{
			this.upOneClothes(id);
		}
		else
		{
			this.downOneClothes(id);
		}

		return CommonResult.successMsg("排序已更新");
	}

	public CommonResult changeSequenceFirstOrLast(int id, boolean isFirst)
	{
		Clothes clothes = this.getById(id);
		int sequence = this.getFirstOrSequence(clothes.getKind(),isFirst);

		if (isFirst)
			clothes.setSequence(sequence-1);
		else
			clothes.setSequence(sequence+1);

		this.updateById(clothes);
		return CommonResult.success();
	}

	@Override
	public CommonResult listKind(int kind)
	{
		if (!CommonUtil.kindIsOk(kind))
		{
			return CommonResult.errorMsg("种类不正确");
		}
		return CommonResult.successData(this.listKindOrderBySequence(kind));
	}

	// ------------------------------------------------------------------------------------------------------------

	/**
	 * 获得某一类的最小或最大排序编号
	 * @param isFirst 是否获得第一个 true-获得第一个 false-获得最后一个
	 */
	private int getFirstOrSequence(int kind, boolean isFirst)
	{
		List<Clothes> list = new LambdaQueryChainWrapper<>(this.baseMapper)
								.eq(Clothes::getKind, kind)
								.orderBy(true, true, Clothes::getSequence)
								.list();
		if (list.isEmpty())
			return 0;

		if (isFirst)
			return list.get(0).getSequence();
		else
			return list.get(list.size()-1).getSequence();
	}

	/**
	 * 分类查询所有，按排序编号排序
	 * @param kind
	 * @return
	 */
	private List<Clothes> listKindOrderBySequence(int kind)
	{
		List<Clothes> list = new LambdaQueryChainWrapper<>(this.baseMapper)
								.eq(Clothes::getKind, kind)
								.orderByAsc(Clothes::getSequence)
								.list();
		return list;
	}

	/**
	 * 上升一个衣物的排序
	 */
	private void upOneClothes(int id)
	{
		Clothes clothesInDown = this.getById(id); // 在下边的衣物 要上升排序的衣物
		List<Clothes> allClotheKind = this.listKindOrderBySequence(clothesInDown.getKind());
		int index = allClotheKind.indexOf(clothesInDown);
		if (index == 0)
		{
			return;
		}
		Clothes clothesInUp = allClotheKind.get(index-1); // 在上边的衣物 要下降排序的衣物

		clothesInDown.setSequence(clothesInUp.getSequence());
		clothesInUp.setSequence(clothesInDown.getSequence()+1);

		this.updateById(clothesInUp);
		this.updateById(clothesInDown);
	}

	/**
	 * 下降一个衣物的排序
	 */
	private void downOneClothes(int id)
	{
		Clothes clothesInUp = this.getById(id);
		List<Clothes> allClotheKind = this.listKindOrderBySequence(clothesInUp.getKind());
		int index = allClotheKind.indexOf(clothesInUp);
		if (index == allClotheKind.size()-1)
		{
			return;
		}
		Clothes clothesInDown = allClotheKind.get(index + 1);

		this.upOneClothes(clothesInDown.getCId());
	}
}