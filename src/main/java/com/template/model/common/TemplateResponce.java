package com.template.model.common;

import com.template.model.TemplateModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TemplateResponce {

	private Integer status;
	private String message;
	private TemplateModel template;
}
