package com.indusnet.util;

import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.indusnet.dto.OtpResponseMessage;

/**
 * This is ResponceUtil class 
 * its have one method response 
 *
 */
@Service
public class ResponeUtil {

	/**
	 * 
	 * @param message: this is responce message
	 * @param otp : it is generated otp 
	 * @return : its return OtpResponseMessage
	 */
	public OtpResponseMessage response(String message, String otp) {
		Long traceId =  Instant.now().toEpochMilli();
		return OtpResponseMessage.builder().message(message)
				.otp(otp)
				.statusCode(HttpStatus.OK.value())
				.traceId(traceId)
				.messageTypeId(1)
				.build();

	}

}
