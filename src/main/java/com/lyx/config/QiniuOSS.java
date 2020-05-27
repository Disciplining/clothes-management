package com.lyx.config;

import cn.hutool.core.io.file.FileReader;
		import cn.hutool.core.util.IdUtil;
		import com.alibaba.fastjson.JSONObject;
import com.lyx.common.Util;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
		import com.qiniu.storage.Region;
		import com.qiniu.storage.UploadManager;
		import com.qiniu.util.Auth;
		import org.springframework.beans.factory.annotation.Value;
		import org.springframework.stereotype.Component;

		import java.io.File;

@Component("qiniuOSS")
public class QiniuOSS
{
	@Value("${qiuniuOSS.access_key}")
	private String accessKey;

	@Value("${qiuniuOSS.secret_key}")
	private String secretKey;

	@Value("${qiuniuOSS.bucket_name}")
	private String bucketName;

	@Value("${qiuniuOSS.domain}")
	private String domain;

	/**
	 * 上传衣服图片
	 * @param pic 图片文件
	 * @return 外链
	 */
	public String uploadClothesPic(File pic)
	{
		if (!Util.isPicFile(pic))
		{
			throw new RuntimeException("文件不是图片");
		}

		String key = "clothes/" + IdUtil.simpleUUID();

		return this.upload(pic, key);
	}

	/**
	 * @param file 要上传的文件
	 * @param key 文件在OSS中的key
	 * @return 文件外链
	 */
	public String upload(File file, String key)
	{
		try
		{
			UploadManager uploadManager = new UploadManager(this.getCfg());
			Response res = uploadManager.put(new FileReader(file).readBytes(), key, this.getUploadToken());
			return domain + JSONObject.parseObject(res.bodyString()).get("key");
		}
		catch (Exception e)
		{
			System.out.println("上传文件失败，错误信息：" + e.getMessage());
			return null;
		}
	}

	/**
	 * 删除七牛云上的一个文件
	 */
	public boolean delete(String key)
	{
		try
		{
			BucketManager bucketManager = new BucketManager(this.getAuth(), this.getCfg());
			bucketManager.delete(bucketName, key);
		}
		catch (QiniuException ex)
		{
			System.err.println("删除失败：" + ex.response.toString());
			return false;
		}

		return true;
	}

	private String getUploadToken()
	{
		return this.getAuth().uploadToken(bucketName);
	}

	private Auth getAuth()
	{
		return Auth.create(accessKey, secretKey);
	}

	private Configuration getCfg()
	{
		return new Configuration(Region.region0());
	}
}