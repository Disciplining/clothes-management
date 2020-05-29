package com.lyx.process.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyx.common.CommonResult;
import com.lyx.common.CommonUtil;
import com.lyx.config.QiniuOSS;
import com.lyx.entity.Clothes;
import com.lyx.process.mapper.ClothesMapper;
import com.lyx.process.service.IClothesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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

	@Override
	public CommonResult saveUploadPic(MultipartFile pic, HttpServletResponse response)
	{
		try
		{
			// 检查参数
			if (Objects.isNull(pic))
			{
				response.setStatus(HttpStatus.HTTP_INTERNAL_ERROR);
				return CommonResult.errorMsg("请上传文件");
			}
			if (!CommonUtil.isPicFile(pic.getOriginalFilename()))
			{
				response.setStatus(HttpStatus.HTTP_INTERNAL_ERROR);
				return CommonResult.errorMsg("上传的不是图片");
			}

			// 上传文件
			String url = qiniuOSS.uploadClothesPic(pic);
			if (StrUtil.isBlank(url))
			{
				response.setStatus(HttpStatus.HTTP_INTERNAL_ERROR);
				return CommonResult.errorMsg("图片上传七牛云失败");
			}

			// 在数据库中生成记录
			Clothes clothes = new Clothes();
			clothes.setUrl(url);
			clothes.setCName("temp");
			clothes.setKind(-1);
			clothes.setSequence(-1);
			boolean saveResult = this.save(clothes);
			if (saveResult)
			{
				return CommonResult.successMsgData("图片上传成功", clothes.getCId());
			}
			else
			{
				response.setStatus(HttpStatus.HTTP_INTERNAL_ERROR);
				return CommonResult.errorMsg("数据库中生成记录失败");
			}
		}
		catch (Exception e)
		{
			response.setStatus(HttpStatus.HTTP_INTERNAL_ERROR);
			System.out.println("添加衣物失败，错误信息：" + e.getMessage());
			return CommonResult.errorMsg("添加衣物失败");
		}
	}

	@Override
	public CommonResult saveForm(Clothes clothes)
	{
		int sequence = this.getLastSequence(clothes.getKind()) + 1;
		clothes.setSequence(sequence);

		return this.updateById(clothes) ? CommonResult.success() : CommonResult.errorMsg("添加数据失败");
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
	 * 获得某一类的最大排序编号
	 */
	private int getLastSequence(int kind)
	{
		List<Clothes> list = new LambdaQueryChainWrapper<>(this.baseMapper)
								.eq(Clothes::getKind, kind)
								.orderBy(true, true, Clothes::getSequence)
								.list();
		if (list.isEmpty())
		{
			return 0;
		}
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