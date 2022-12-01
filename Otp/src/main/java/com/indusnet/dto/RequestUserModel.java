package com.indusnet.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * This class for dto of OtpModel for request body in post Api.
 */

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestUserModel {

	@NotNull
	@Size(min = 4, max = 16, message = "Name length should be 4 to 16 letters")
	private String name;

	@NotNull
	@Email
	@Pattern(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", message="Email Not Valid.")
	private String email;

	@NotNull
	@Size(min=10, max=10, message="Mobile number should be 10 digit")
	@Pattern(regexp = "^\\d{10}$")
	private String mobile;
	
	//@Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$")
	private String countryCode;

	public RequestUserModel(
			@NotNull @Size(min = 4, max = 16, message = "Name length should be 4 to 16 letters") String name,
			@NotNull @Email @Pattern(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", message = "Email Not Valid.") String email,
			@NotNull @Size(min = 10, max = 10, message = "Mobile number should be 10 digit") @Pattern(regexp = "^\\d{10}$") String mobile) {
		super();
		this.name = name;
		this.email = email;
		this.mobile = mobile;
	}
}
