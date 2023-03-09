package com.template.model.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FetchTableAtributeRequest {

	private String processName;
	private String notificationType;
	private String[] notificationChannel;
	private Integer latestCount;
}
