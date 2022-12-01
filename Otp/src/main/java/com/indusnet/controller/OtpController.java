package com.indusnet.controller;


import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.indusnet.dto.OtpResponseMessage;
import com.indusnet.dto.RequestUserModel;
import com.indusnet.service.IOtpService;
import com.indusnet.util.ResponeUtil;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1.1.2/otp")
@RequiredArgsConstructor
public class OtpController {

	@Autowired
	private IOtpService userService;

	@Autowired
	ResponeUtil responseUtil;

	/*
	 * This Api generate take RequestUserModel
	 * and generate time based otp.
	 * return otp with response OtpResponseMessage
	 */
	/**
	 * 
	 * @param user: This is user store the name , email , and mobile number
	 * @return :its return responceEntity of OtpResponseMessage.
	 */
 
	@PostMapping("/generate")
	public ResponseEntity<OtpResponseMessage> generateOtpHandler(@RequestBody @Valid RequestUserModel user){
		String otp = userService.generateOtp(user);
		OtpResponseMessage responce = responseUtil.response("OTP generated successfully", otp);
		return ResponseEntity.ok().body(responce);
	}

	/*
	 * This Api validate otp
	 * and return success response OtpResponseMessage
	 */
	/**
	 * 
	 * @param otp : otp is 6 digit string and its validate the user 
	 * @return : if otp is match then its return success response if not match then throws exception.
	 */
	@GetMapping("/validate/{otp}")
	public ResponseEntity<OtpResponseMessage> validateOtpHandler(@PathVariable("otp")  String otp){
		String message = userService.validate(otp);
		return ResponseEntity.ok().body(responseUtil.response(message, otp));
	}

	/*
	 * This Api resend request for otp generate and 
	 * this api use previous RequestUserModel data which call 
	 * in last time of generateOtpHandler api.
	 * and generate time based otp.
	 * return otp with response OtpResponseMessage
	 */
	/**
	 * This Api resend the otp maximum 3 times.
	 * @return : its return success responce then otp generate succesfully 
	 */
	@GetMapping("/resend")
	public ResponseEntity<OtpResponseMessage> resendOtpHandler(){
		String otp = userService.resend();
		OtpResponseMessage responce = responseUtil.response("OTP generated successfully", otp);
		return ResponseEntity.ok().body(responce);
	}
 
	/*
	 * This is constructor to initialize the userService and responseUtil
	 */
	/**
	 * 
	 * @param userService :It is service layer and hold all service method.
	 * @param responseUtil :It is util layer and it have otp creation logic and method.
	 */
	public OtpController(IOtpService userService, ResponeUtil responseUtil) {
		super();
		this.userService = userService;
		this.responseUtil = responseUtil;
	}

}
