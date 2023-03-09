package com.template.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.template.model.common.TemplateResponce;
import com.google.gson.Gson;
import com.template.model.common.FetchAllTemplateResponce;
import com.template.model.common.FetchTableAtributeRequest;
import com.template.model.common.LoggingResponseMessage;
import com.template.model.common.MessageTypeConst;
import com.template.model.common.TemplateRequest;
import com.template.service.ITemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

/**
 * This is controller layer here all endpoints are available.
 * @author Md Arif
 *
 */
@RestController
@Api(tags = "Notification",description = "Notification Template API")
@Slf4j
public class TemplateController {

	@Autowired
	ITemplateService iTemplateService;

	@Autowired
	Gson gson;

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Notification template is successfully added to the system"),
			@ApiResponse(code = 401, message = "You are not authorized to save the notification template"),
			@ApiResponse(code = 403, message = "Accessing the notification template you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The notification template you were trying to reach is not found") })
	@ApiOperation(value = "Create notification Template", notes = "Return a created notification template", response = TemplateResponce.class)
	@ApiImplicitParam(name = "notificationParameter", value = "Fill the template variables",required = true,dataType = "TemplateRequest")
	@PostMapping(value = "/" ,produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TemplateResponce> savetempEntity(@RequestBody TemplateRequest notificationParameter) {
		LoggingResponseMessage msgStart = LoggingResponseMessage.builder()
				.message("Template save method started.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.data(notificationParameter)
				.build();

		log.info(gson.toJson(msgStart));

		TemplateResponce templateModel = iTemplateService.saveTemplateModel(notificationParameter);

		LoggingResponseMessage msgEnd = LoggingResponseMessage.builder()
				.message("Template save method end.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.data(templateModel)
				.build();

		log.info(gson.toJson(msgEnd));

		return ResponseEntity.ok(templateModel);
	}

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Notification template list is fetched successfully"),
			@ApiResponse(code = 401, message = "You are not authorized to fetched the all notification template"),
			@ApiResponse(code = 403, message = "Fetching the notification template you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The notification template you were trying to reach is not found") })
	@ApiOperation(value = "Show notification templates", notes = "Return all notification template", response = FetchAllTemplateResponce.class)
	@GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FetchAllTemplateResponce> getAlltempEntity() {
		LoggingResponseMessage msgStart = LoggingResponseMessage.builder()
				.message("Template fetch method started.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.build();

		log.info(gson.toJson(msgStart));

		FetchAllTemplateResponce templateModel = iTemplateService.getAllTemplate();

		LoggingResponseMessage msgEnd = LoggingResponseMessage.builder()
				.message("Template fetch method end.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.build();

		log.info(gson.toJson(msgEnd));

		return ResponseEntity.ok(templateModel);
	}

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Notification template is fetched Successfully"),
			@ApiResponse(code = 401, message = "You are not authorized to fetch the notification template"),
			@ApiResponse(code = 403, message = "Accessing the notification template you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The notification template you were trying to reach is not found") })
	@ApiOperation(value = "Show notification template with id", notes = "Return notification template", response = TemplateResponce.class)
	@GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TemplateResponce> getTemplateByIds(@PathVariable("id") Integer notificationTemplateId) {
		LoggingResponseMessage msgStart = LoggingResponseMessage.builder()
				.message("Template get by id  method start.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.data(notificationTemplateId)
				.build();

		log.info(gson.toJson(msgStart));

		TemplateResponce response = iTemplateService.getTemplateById(notificationTemplateId);

		LoggingResponseMessage msgEnd = LoggingResponseMessage.builder()
				.message("Template get by id  method end.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.data(response)
				.build();

		log.info(gson.toJson(msgEnd));

		return ResponseEntity.ok(response);
	}

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Notification template is successfully deleted by the system"),
			@ApiResponse(code = 401, message = "You are not authorized to delete the notification template"),
			@ApiResponse(code = 403, message = "Deleting the notification template you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The notification template you were trying to reach is not found")})
	@ApiOperation(value = "Delete notification template with id", notes = "Return deleted notification template", response = TemplateResponce.class)
	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TemplateResponce> deleteTemplateById(@PathVariable("id") Integer notificationTemplateId) {
		LoggingResponseMessage msgStart = LoggingResponseMessage.builder()
				.message("Template delete by id  method start.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.data(notificationTemplateId)
				.build();

		log.info(gson.toJson(msgStart));

		TemplateResponce response = iTemplateService.deleteTemplate(notificationTemplateId);

		LoggingResponseMessage msgEnd = LoggingResponseMessage.builder()
				.message("Template delete by id  method end.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.data(response)
				.build();

		log.info(gson.toJson(msgEnd));

		return ResponseEntity.ok(response);
	}

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Notification Template is successfully fetched by the attribute."),
			@ApiResponse(code = 401, message = "You are not authorized to fetch the notification template"),
			@ApiResponse(code = 403, message = "Fetching the notification template you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The notification template you were trying to reach is not found")
	})
	@ApiOperation(value = "Fetch notification template with different attribute.", notes = "Return list of fetched notification template", response = FetchAllTemplateResponce.class)
	@ApiImplicitParam(name = "atributeRequest", value = "Fill the request body",required = true,dataType = "FetchTableAtributeRequest")
	@PostMapping(value="/searchByAttributes",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FetchAllTemplateResponce> fetchTemplateByAtribute(@RequestBody FetchTableAtributeRequest atributeRequest) {

		LoggingResponseMessage msgStart = LoggingResponseMessage.builder()
				.message("Template fetch by attribute  method start.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.data(atributeRequest)
				.build();

		log.info(gson.toJson(msgStart));

		FetchAllTemplateResponce response = iTemplateService.fetchTemplateByAtribute(atributeRequest);

		LoggingResponseMessage msgEnd = LoggingResponseMessage.builder()
				.message("Template fetch by attribute  method end.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.data(response)
				.build();

		log.info(gson.toJson(msgEnd));

		return ResponseEntity.ok(response);
	}
}
