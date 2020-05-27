package com.lyx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
public class App
{
	public static void main(String[] args)
	{
		SpringApplication.run(App.class, args);
	}

	/**
	 * 设置上传文件的大小限制
	 */
	@Bean("getMultipartConfigElement")
	public MultipartConfigElement getMultipartConfigElement()
	{
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize(DataSize.ofMegabytes(20L)); // 单个文件最大20M
		factory.setMaxRequestSize(DataSize.ofGigabytes(1L)); // 设置总上传数据总大小1G

		return factory.createMultipartConfig();
	}

}