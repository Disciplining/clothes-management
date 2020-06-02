package com.lyx.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ClothesSaveDto
{
	private String cName;
	private int kind;
	private MultipartFile picFile;
}