package com.lyx;

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
	}
}