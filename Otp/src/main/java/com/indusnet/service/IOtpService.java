package com.indusnet.service;

import com.indusnet.dto.RequestUserModel;
import com.indusnet.exception.OtpException;

/**
 * This Interface have 3 method generateOtp,validateOtp, and resendOtp
 * and also throws an OtpException.
 *
 */

public interface IOtpService {

	public String generateOtp(RequestUserModel user) throws OtpException;
	
	public String validate(String otp) throws OtpException;
	
	public String resend() throws OtpException;
	
}
