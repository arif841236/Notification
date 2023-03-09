package com.template;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.template.model.common.TemplateRequest;
import com.template.model.common.TemplateResponce;
import com.template.repository.ITemplateRepository;
import com.template.service.ITemplateService;
import com.template.service.impl.TemplateServiceImpl;

@SpringBootTest
class NotificationApplicationTests {

	@Autowired
	ITemplateRepository iTemplateRepository;
	@Autowired
	ITemplateService iTemplateService;

	@BeforeEach
	void setUp() {
		this.iTemplateService = new TemplateServiceImpl(iTemplateRepository);
	}

	@Test
	void contextLoads() {

		TemplateRequest templateModel = TemplateRequest.builder().createdBy("System")
				.notificationChannel(new String[] { "email", "sms" }).notificationType("otp").processName("ONBOARDING")
				.templateBody(
						" {$$otp$$} is your OTP for {$$processName$$}, generated at {$$created_time$$} and valid for next 3 mins.")
				.build();
		TemplateResponce responce = iTemplateService.saveTemplateModel(templateModel);
		assertEquals(200, responce.getStatus());

	}

}
