package com.lyx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig
{
	@Bean("getDocket")
	public Docket getDocket()
	{
		Docket docket = new Docket(DocumentationType.SWAGGER_2)
							.groupName("默认分组")
							.apiInfo(getApiInfo()) // 配置Swagger信息
							.enable(true); //  是否启用Swagger，不配置，默认就是启用的

		return docket;
	}

	private ApiInfo getApiInfo()
	{
		Contact contact = new Contact("李艳兴", "https://blog.csdn.net/Sacredness", "name_Lyx@163.com");

		return new ApiInfoBuilder()
				.title("衣物管理系统").version("1.0")
				.description("李艳兴的衣物管理系统")
				.contact(contact)
				.build();
	}
}