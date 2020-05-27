package com.lyx.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ClotheSaveDto
{
	private String cName;
	private int kind;
	private MultipartFile file;
}