package com.indusnet.dto;


import lombok.Builder;
import lombok.Getter;

/**
 * This class for success response message;
 */
@Builder
@Getter
public class OtpResponseMessage {

	private Integer statusCode;
	private Long traceId;
	private String otp;
	private String message ;
	private Integer messageTypeId;
}
