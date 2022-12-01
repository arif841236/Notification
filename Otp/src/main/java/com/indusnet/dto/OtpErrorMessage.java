package com.indusnet.dto;

import java.sql.Timestamp;
import lombok.Builder;
import lombok.Getter;

/**
 * This class for error message;
 */
@Builder
@Getter
public class OtpErrorMessage {
	
	private Timestamp timestamp;
    private Integer statusCode;
    private Integer errorCode;
    private String message; 
    private String error;
    private Long traceID;
    private String trace;
    private String path;

}
