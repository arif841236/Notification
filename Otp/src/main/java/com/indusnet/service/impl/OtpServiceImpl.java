package com.indusnet.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.indusnet.exception.OtpException;
import com.indusnet.model.OtpData;
import com.indusnet.model.ResendRequest;
import com.indusnet.model.SendOtpRequest;
import com.indusnet.model.UserModel;
import com.indusnet.model.ValidateRequest;
import com.indusnet.model.common.Type;
import com.indusnet.model.common.ValidationResponce;
import com.indusnet.repository.IOtpRepository;
import com.indusnet.repository.IUserModelRepository;
import com.indusnet.service.IOtpService;
import com.indusnet.util.OtpUtil;
import com.indusnet.util.OtpSend;
import com.indusnet.util.OtpSendToMail;
import com.indusnet.util.Util;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * This is Implementation class of IOtpService and override all method.
 * and its also Service layer of project.
 *This class have many service logics.
 */

@Service
@Slf4j
public class OtpServiceImpl implements IOtpService {

	@Autowired
	IUserModelRepository userRepository2;

	@Autowired
	OtpUtil otpUtil;
	Type type = null;
	@Autowired
	private OtpSend otpSend;

	@Autowired
	OtpSendToMail otpMail;

	int secKeyValue = LocalDateTime.now().getNano();

	LocalDateTime validationTime = LocalDateTime.now();

	String totp = null;
	int resendCount = 0;
	int generateCount = 0;
	int validateCount = 0;
	boolean isValidate = false;
	@Autowired
	private IOtpRepository userRepository;

	/**
	 * This Constructor initialize the userRepository
	 * @param userRepository 
	 */


	public OtpServiceImpl(OtpSend otpSend, IOtpRepository userRepository, OtpSendToMail otpMail) {
		super();
		this.otpSend = otpSend;
		this.userRepository = userRepository;
		this.otpMail = otpMail;
	}

	public OtpServiceImpl(IOtpRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}
	
	public OtpServiceImpl() {
		super();
	}


	/**
	 * this method is generate the otp and 
	 * return otp.
	 */

	OtpData otpData = new OtpData();

	@Override
	public OtpData generateOtp(SendOtpRequest user) throws OtpException {
		if(user.getType().equals("sms") && user.getTypeValue().contains("@gmail.com")) {
			throw new OtpException();
		}
		if(user.getType().equals("email") && user.getTypeValue().length() == 10) {
			throw new OtpException();
		}
		generateCount++;
		validateCount = 0;
		if(generateCount > 5) {
			CompletableFuture.delayedExecutor(300, TimeUnit.SECONDS).execute(() -> generateCount = 0 );
			throw new OtpException("you exceed the maximum number of attempt.");
		}
	
		
		if(user.getType().equals("sms")) {
			type = Type.SMS;
		}
		else {
			type = Type.EMAIL;
		}
		Optional<OtpData> existedUser = userRepository.findByTypeValue(user.getTypeValue());
		existedUser.ifPresentOrElse(x -> {

			//Secret key
			String secKey = user.getRequeston().toString();

			log.info("secKey "+secKey);
			// OTP generate 
			String otp = Util.generateTOTP256(secKey,secKeyValue , "6");
			log.info("OTP is :"+otp);
			totp =otpUtil.base64encode(Integer.parseInt(otp));
			CompletableFuture.delayedExecutor(600, TimeUnit.SECONDS).execute(() -> totp = null );
			Integer messageId = otpUtil.otpIdGenrate();
			log.info("otpId is "+messageId);
			
			OtpData newUserModel = OtpData.builder()
					.id(x.getId())
					.messageId(messageId)
					.requestdevice(x.getRequestdevice())
					.type(Type.valueOf(user.getType().toUpperCase()))
					.typeValue(x.getTypeValue())
					.user(user.getUser())
					.validUpto(LocalDateTime.now().plusMinutes(10))
					.requeston(LocalDateTime.now())
					.build();
			otpData=userRepository.save(newUserModel);
		}
		,() -> {

			//Secret key
			String secKey = user.getRequeston().toString();

			// OTP generate 
			String otp = Util.generateTOTP256(secKey,secKeyValue , "6");
			log.info("OTP is "+otp);
			
			totp =otpUtil.base64encode(Integer.parseInt(otp));
			CompletableFuture.delayedExecutor(600, TimeUnit.SECONDS).execute(() -> totp = null );

			Integer messageId = otpUtil.otpIdGenrate();
			log.info("otpId is "+messageId);
			
			OtpData newUserModel = OtpData.builder()
					.requestdevice(user.getRequestdevice())
					.type(Type.valueOf(user.getType().toUpperCase()))
					.messageId(messageId)
					.typeValue(user.getTypeValue())
					.user(user.getUser())
					.validUpto(LocalDateTime.now().plusMinutes(10))
					.requeston(LocalDateTime.now())
					.build();
			otpData=userRepository.save(newUserModel); 

		});

		return otpData;
	}





	/**
	 * This method validate otp and 
	 * return success message if validate successfully or
	 * throw OtpException
	 */
	@Override
	public ValidationResponce validate(ValidateRequest valiRequest) throws OtpException {

		validateCount++;
		if(validateCount > 3) 
			throw new OtpException("you crossed maximum attempt for validation. Please resend the OTP for further validation");

		OtpData otpDataDetails=userRepository.getOtpDataByMessageId(valiRequest.getOtpId());
		String otp =otpUtil.base64encode(valiRequest.getOtp());

		if(Objects.equals(otp, totp) 
				&& Objects.equals(otpDataDetails.getMessageId(), valiRequest.getOtpId())) 
		{	
			isValidate = true;
			totp = null;
			return ValidationResponce.builder()
					.status(HttpStatus.OK.value())
					.message("OTP validated successfully")
					.build();
		}
		else if(isValidate) 
		{
			return ValidationResponce.builder()
					.status(HttpStatus.ACCEPTED.value())
					.message("OTP is already being used for validation")
					.build();
		}
		else if(otpDataDetails.getValidUpto().isBefore(LocalDateTime.now())) {
			return ValidationResponce.builder()
					.status(HttpStatus.ACCEPTED.value())
					.message("OTP is expired")
					.build();
		}
		else if(!Objects.equals(totp, otp) || totp == null) {
			return ValidationResponce.builder()
					.status(HttpStatus.BAD_REQUEST.value())
					.message("The Submitted OTP is Wrong")
					.build();
		}
		else {
			throw new OtpException();
		}

	}

	/**
	 * This method resend the otp
	 * if user resend otp 5 times 
	 * and after again resend otp then throws OtpException 
	 */
	@Override
	public OtpData resend(ResendRequest resendRequest) throws OtpException {
		resendCount++;
		validateCount = 0;
		if(resendCount > 3) {
			CompletableFuture.delayedExecutor(120, TimeUnit.SECONDS).execute(() -> resendCount = 0 );
			throw new OtpException("your service is block for 2mins and resend after 2mins");
		}

		userRepository.findByMessageId(resendRequest.getLastOtpId()).ifPresentOrElse(x -> {
			UserModel user = new UserModel();
			validationTime = LocalDateTime.now();
			if(resendRequest.getUser().getLoggedIn() == 1) {
				user = UserModel.builder().userId(x.getUser().getUserId()).build();

			}

			//Secret key
			String secKey = resendRequest.getRequeston().toString();

			// OTP generate 
			String otp = Util.generateTOTP256(secKey,secKeyValue , "6");
			log.info("OTP is "+otp);
			totp =otpUtil.base64encode(Integer.parseInt(otp));
			CompletableFuture.delayedExecutor(600, TimeUnit.SECONDS).execute(() -> totp = null );
			
			OtpData newUserModel = OtpData.builder()
					.requestdevice(resendRequest.getRequestdevice())
					.type(Type.valueOf(resendRequest.getType().toUpperCase()))
					.typeValue(resendRequest.getTypeValue())
					.user(user)
					.messageId(resendRequest.getLastOtpId())
					.validUpto(LocalDateTime.now().plusMinutes(10))
					.requeston(LocalDateTime.now())
					.build();
			otpData=userRepository.save(newUserModel);

		}, () -> {
			throw new OtpException();
		});

		return otpData;
	}

}
