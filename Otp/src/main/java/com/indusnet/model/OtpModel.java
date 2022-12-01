package com.indusnet.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class OtpModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "register_seq")
	@SequenceGenerator(name = "register_seq", sequenceName = "regis_seq", initialValue = 1001)
	private Integer id;
	
	private String name;
	private String email;
	private String mobile;
	private String countryCode;
	
	@Column(unique = true)
	private String secret;
	
		
}
