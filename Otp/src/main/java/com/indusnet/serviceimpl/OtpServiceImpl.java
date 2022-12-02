package com.indusnet.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.indusnet.dto.RequestUserModel;
import com.indusnet.dto.UserDetail;
import com.indusnet.exception.OtpException;
import com.indusnet.model.OtpModel;
import com.indusnet.repository.IOtpRepository;
import com.indusnet.service.IOtpService;
import com.indusnet.util.OtpSend;
import com.indusnet.util.OtpSendToMail;
import com.indusnet.util.Util;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * This is Implementation class of IOtpService and override all method.
 * and its also Service layer of project.
 *This class have many service logics.
 */

@Service
public class OtpServiceImpl implements IOtpService {

	public OtpServiceImpl() {
		super();
	}

	@Autowired
	private OtpSend otpSend;

	@Autowired
	OtpSendToMail otpMail;

	int secKeyValue = LocalDateTime.now().getNano();

	LocalDateTime validationTime = LocalDateTime.now();
	
	String otp = null;
	RequestUserModel reqModel = null;
	int resendCount = 0;
	int generateCount = 0;
	int validateCount = 0;
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

	/**
	 * this method is generate the otp and 
	 * return otp.
	 */

	@Override
	public String generateOtp(RequestUserModel user) throws OtpException {
		generateCount++;

		validateCount = 0;

		if(generateCount > 5) {
			CompletableFuture.delayedExecutor(300, TimeUnit.SECONDS).execute(() -> generateCount = 0 );

			throw new OtpException("you exceed the maximum number of attempt.");
		}
		// store user details
		reqModel = user;

		//Secret key
		String secKey =user.getMobile() +secKeyValue;

		// OTP generate 
		otp = Util.generateTOTP256(secKey,secKeyValue , "6");

		if(validationTime.plusSeconds(60).isBefore(LocalDateTime.now())) {
			secKeyValue++;
			validationTime = LocalDateTime.now().plusSeconds(60);
		}
		
		
		String mobile = "";
		if(user.getCountryCode() == null) {
			mobile = "+91".concat(user.getMobile());
		}
		else
			mobile = user.getCountryCode().concat(user.getMobile());

		String message = "Dear customer, use this One Time Password (Number)"
				+ " to log in to your (Company name) account."
				+ " This OTP will be valid for the next 1 mins.Your OTP is "+otp;

		// send otp to mobile
		otpSend.send(new UserDetail(mobile, message));

		// send email
		otpMail.sendEmail(user.getEmail(), message);



		Optional<OtpModel> existedUser = userRepository.findByEmail(user.getEmail());
		existedUser.ifPresentOrElse(x -> {
			OtpModel otpModel = existedUser.get();
			OtpModel newUserModel = OtpModel.builder()
					.id(otpModel.getId())
					.name(user.getName())
					.email(user.getEmail())
					.mobile(user.getMobile())
					.secret(secKey)
					.build();
			userRepository.save(newUserModel);
		}
		,() -> {
			OtpModel newUserModel = OtpModel.builder()
					.name(user.getName())
					.email(user.getEmail())
					.mobile(user.getMobile())
					.secret(secKey)
					.build();
			userRepository.save(newUserModel);
		});

		return otp;
	}





	/**
	 * This method validate otp and 
	 * return success message if validate successfully or
	 * throw OtpException
	 */
	@Override
	public String validate(String totp) throws OtpException {

		validateCount++;
		if(validateCount > 3) 
			throw new OtpException("you crossed maximum attempt for validation. Please resend the OTP for further validation");

		if(Objects.equals(totp, otp)) 
			return "OTP validated successfully";

		else if(totp.length() != 6) 
			throw new OtpException("Invalid OTP Please enter 6 digit OTP.");

		else 
			throw new OtpException("Invalid OTP");


	}

	/**
	 * This method resend the otp
	 * if user resend otp 5 times 
	 * and after again resend otp then throws OtpException 
	 */
	@Override
	public String resend() throws OtpException {
		resendCount++;

		validateCount = 0;

		if(resendCount == 4)
			CompletableFuture.delayedExecutor(120, TimeUnit.SECONDS).execute(() -> resendCount = 0 );

		if(resendCount > 3) 
			throw new OtpException("your service is block for 2mins and resend after 2mins");


		if(reqModel != null) { 
			generateCount = 0;
			return generateOtp(reqModel);
		}
		else 
			throw new OtpException("User not stored please send User details.");


	}

}
