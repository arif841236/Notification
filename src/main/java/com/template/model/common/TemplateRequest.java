package com.template.model.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class TemplateRequest {
	
	@ApiModelProperty(notes = "Name of process")
	private String processName;

	@ApiModelProperty(notes = "Template body for message")
	private String templateBody;

	@ApiModelProperty(notes = "Type of notification")
	private String notificationType;

	@ApiModelProperty(notes = "Array of notification channel")
	private String[] notificationChannel;

	@ApiModelProperty(notes = "Name of creator")
	private String createdBy;
}
