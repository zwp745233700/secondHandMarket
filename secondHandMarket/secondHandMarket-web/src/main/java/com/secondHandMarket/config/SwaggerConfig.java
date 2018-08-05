package com.secondHandMarket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/** 
* 类说明 :swagger测试相关配置
*/

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	@Bean
	 public Docket creatApi(){
	 return new Docket(DocumentationType.SWAGGER_2)
	  .apiInfo(apiInfo())
	  .select() //选择哪些路径和api会生成document
	  .apis(RequestHandlerSelectors.basePackage("com.secondHandMarket.controller"))//controller路径
	  //.apis(RequestHandlerSelectors.any())   //对所有api进行监控
	  .paths(PathSelectors.any())  //对所有路径进行监控
	  .build();
	 }
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("二手淘金商城")//文档主标题
				.description("二手淘金商城后端接口文档")//文档描述
				.version("1.0.0")//API的版本
				.termsOfServiceUrl("https://coding.net/u/zhangweipeng/p/secondHandMarket/git")
				.license("LICENSE")
				.licenseUrl("https://coding.net/u/zhangweipeng/p/secondHandMarket/git")
				.build();
	}
}