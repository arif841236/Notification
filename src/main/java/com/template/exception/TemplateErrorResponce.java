package com.template.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class for showing otp error exception
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TemplateErrorResponce {
	private Integer status;
	private String message;
	private String path;
}
