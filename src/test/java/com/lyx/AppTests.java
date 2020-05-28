package com.lyx;

import com.lyx.config.QiniuOSS;
import com.lyx.process.service.impl.ClothesServiceImpl;
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

	@Autowired
	@Qualifier("clothesServiceImpl")
	private ClothesServiceImpl service;

	@Test
	void contextLoads()
	{
		String url = "http://pic.liyanxing.top/clothes/209431758fa04d36b8b807b2007907e2";

		int index = url.lastIndexOf("clothes");

		System.out.println(url.substring(index));
	}

	@Test
	public void test1()
	{
		System.out.println(service.remove(2));
	}

	@Test
	public void test2()
	{
	}
}