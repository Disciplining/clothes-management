package com.lyx.config;

import cn.hutool.core.io.file.FileReader;
		import cn.hutool.core.util.IdUtil;
		import com.alibaba.fastjson.JSONObject;
		import com.qiniu.http.Response;
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
			UploadManager UPLOAD_MANAGER = new UploadManager(new Configuration(Region.region0()));
			Response res = UPLOAD_MANAGER.put(new FileReader(file).readBytes(), key, this.getUploadToken());
			return domain + JSONObject.parseObject(res.bodyString()).get("key");
		}
		catch (Exception e)
		{
			System.out.println("上传文件失败，错误信息：" + e.getMessage());
			return null;
		}
	}

	/**
	 * 获取token
	 */
	private String getUploadToken()
	{
		return Auth.create(accessKey, secretKey).uploadToken(bucketName);
	}
}