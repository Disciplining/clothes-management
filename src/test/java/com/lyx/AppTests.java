package com.lyx;

import cn.hutool.core.io.FileUtil;
import com.lyx.common.Util;
import com.lyx.config.QiniuOSS;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AppTests
{
	@Autowired
	@Qualifier("qiniuOSS")
	private QiniuOSS qiniuOSS;

	@Test
	void contextLoads()
	{
		String url = qiniuOSS.upload(FileUtil.file("/Users/lyx/my-dir/download/激活码/lyx.txt"), "bbll.txt");

		System.out.println(url);
	}

	@Test
	public void test1()
	{
		boolean isPic = Util.isPicFile(FileUtil.file("/Users/lyx/my-dir/download/激活码/lyx.png"));

		System.out.println(isPic);
	}

	@Test
	public void test2()
	{
		boolean delete = qiniuOSS.delete("bbll.txt");
		System.out.println(delete);
	}
}