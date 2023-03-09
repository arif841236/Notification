package com.template.model.common;

import java.util.List;

import com.template.model.TemplateModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FetchAllTemplateResponce {

	private Integer status;
	private String message;
	private List<TemplateModel> templateList;
}
