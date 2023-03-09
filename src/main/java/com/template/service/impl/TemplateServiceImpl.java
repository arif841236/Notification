package com.template.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.template.exception.NotificationException;
import com.template.exception.TemplateException;
import com.template.model.TemplateModel;
import com.template.model.common.FetchAllTemplateResponce;
import com.template.model.common.FetchTableAtributeRequest;
import com.template.model.common.LocalDateTimeConverter;
import com.template.model.common.LoggingResponseMessage;
import com.template.model.common.MessageTypeConst;
import com.template.model.common.NotificationType;
import com.template.model.common.ProcessName;
import com.template.model.common.TemplateResponce;
import com.template.model.common.TemplateRequest;
import com.template.repository.ITemplateRepository;
import com.template.service.ITemplateService;
import lombok.extern.slf4j.Slf4j;

/**
 * This is service layer and here all logics are written.
 * @author Md Arif
 *
 */
@Service
@Slf4j
public class TemplateServiceImpl implements ITemplateService {

	@Autowired
	ITemplateRepository iTemplateRepository;

	Gson gson = new GsonBuilder()
			.registerTypeAdapter(Timestamp.class, new LocalDateTimeConverter())
			.create();

	public TemplateServiceImpl(ITemplateRepository iTemplateRepository) {
		super();
		this.iTemplateRepository = iTemplateRepository;
	}

	/**
	 * This method for save template and
	 * return template with success message.
	 */
	@Override
	public TemplateResponce saveTemplateModel(TemplateRequest tempRequest)  {
		LoggingResponseMessage msgStart = LoggingResponseMessage.builder()
				.message("saveTemplateModel method statrt.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.data(tempRequest)
				.build();

		log.info(gson.toJson(msgStart));

		String tempId = UUID.randomUUID().toString().substring(0, 8);

		TemplateModel templateModel = TemplateModel.builder().createdAt(Timestamp.valueOf(LocalDateTime.now()))
				.createdBy(tempRequest.getCreatedBy())
				.notificationChannel(tempRequest.getNotificationChannel())
				.notificationType(NotificationType.valueOf(tempRequest.getNotificationType().toUpperCase()).name())
				.processName(ProcessName.valueOf(tempRequest.getProcessName().toUpperCase()).name())
				.templateBody(tempRequest.getTemplateBody())
				.templateId(tempId)
				.build();

		LoggingResponseMessage logmsg1 = LoggingResponseMessage.builder()
				.message("TemplateModel build successfully.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.data(templateModel)
				.build();

		log.info(gson.toJson(logmsg1));

		TemplateModel templateModel2 = iTemplateRepository.save(templateModel);

		LoggingResponseMessage logmsg2 = LoggingResponseMessage.builder()
				.message("TemplateModel save successfully.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.data(templateModel2)
				.build();

		log.info(gson.toJson(logmsg2));

		return TemplateResponce.builder()
				.status(HttpStatus.OK.value())
				.message("Notification Template is successfully added to the system")
				.template(templateModel2).build();

	}

	/**
	 * This method for fetch all template and
	 * return all template with success message.
	 */
	@Override
	public FetchAllTemplateResponce getAllTemplate() {
		LoggingResponseMessage logmsg1 = LoggingResponseMessage.builder()
				.message("getAllTemplate method start.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.build();

		log.info(gson.toJson(logmsg1));

		List<TemplateModel> list  = iTemplateRepository.findAll();

		if(list.isEmpty()) {
			LoggingResponseMessage logmsg3 = LoggingResponseMessage.builder()
					.message("No Template is found.")
					.messageTypeId(MessageTypeConst.ERROR)
					.data(list)
					.build();

			log.error(gson.toJson(logmsg3));

			throw new TemplateException("No Template is found.");
		}
		LoggingResponseMessage logmsg2 = LoggingResponseMessage.builder()
				.message("Fetch all template successfully.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.data(list)
				.build();

		log.info(gson.toJson(logmsg2));

		return FetchAllTemplateResponce.builder()
				.status(HttpStatus.OK.value())
				.message("Notification-Template List is Fetched Successfully")
				.templateList(list)
				.build();


	}

	/**
	 * This method for fetch one template with id and
	 * return template with success message.
	 */
	@Override
	public TemplateResponce getTemplateById(Integer id) {
		LoggingResponseMessage logmsg2 = LoggingResponseMessage.builder()
				.message("getTemplateById method start.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.data(id)
				.build();

		log.info(gson.toJson(logmsg2));

		Optional<TemplateModel> temp2 = iTemplateRepository.findById(id);

		if(temp2.isPresent()) {
			LoggingResponseMessage logmsg3 = LoggingResponseMessage.builder()
					.message("Fetch template by id is successfully.")
					.messageTypeId(MessageTypeConst.SUCCESS)
					.data(temp2.get())
					.build();

			log.info(gson.toJson(logmsg3));

			return TemplateResponce.builder()
					.status(HttpStatus.OK.value())
					.message("Template is successfully fetched from the system")
					.template(temp2.get())
					.build();

		}
		LoggingResponseMessage logmsg4 = LoggingResponseMessage.builder()
				.message("Template is not found.")
				.messageTypeId(MessageTypeConst.ERROR)
				.data(temp2.toString())
				.build();

		log.error(gson.toJson(logmsg4));

		throw new TemplateException("No Template is found with id "+id);
	}

	/**
	 * This method for delete template with help of id and
	 * return deleted template with success message.
	 */
	@Override
	public TemplateResponce deleteTemplate(Integer id) {
		LoggingResponseMessage logmsg2 = LoggingResponseMessage.builder()
				.message("deleteTemplate method start.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.data(id)
				.build();

		log.info(gson.toJson(logmsg2));

		Optional<TemplateModel> temp=	iTemplateRepository.findById(id);
		if(temp.isPresent()) {
			iTemplateRepository.delete(temp.get());

			LoggingResponseMessage logmsg3 = LoggingResponseMessage.builder()
					.message("Template is deleted successfully.")
					.messageTypeId(MessageTypeConst.SUCCESS)
					.data(temp.get())
					.build();

			log.info(gson.toJson(logmsg3));

			return TemplateResponce.builder()
					.status(HttpStatus.OK.value())
					.message("Template is successfully removed from the system")
					.template(temp.get()).build();
		}

		LoggingResponseMessage logmsg4 = LoggingResponseMessage.builder()
				.message("Template is not found.")
				.messageTypeId(MessageTypeConst.ERROR)
				.data(temp.toString())
				.build();

		log.error(gson.toJson(logmsg4));

		throw new TemplateException("Template is not available with id "+id);
	}

	/**
	 * This is method to fetch the template by different attribute.
	 */
	@Override
	public FetchAllTemplateResponce fetchTemplateByAtribute(FetchTableAtributeRequest atributeRequest) {
		LoggingResponseMessage logmsg2 = LoggingResponseMessage.builder()
				.message("fetchTemplateByAtribute method start.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.data(atributeRequest)
				.build();

		log.info(gson.toJson(logmsg2));

		String[] notificationChannel = atributeRequest.getNotificationChannel();
		String notificationType = atributeRequest.getNotificationType();
		String processName = atributeRequest.getProcessName();
		Integer count = atributeRequest.getLatestCount();

		FetchAllTemplateResponce response = new FetchAllTemplateResponce();

		if((count == null && notificationChannel == null && notificationType == null && processName == null) 
				|| (notificationChannel == null && notificationType == null && processName == null)) {

			throw new NotificationException("Please fill the all request body field.");
		}

		if(notificationChannel != null && notificationType != null && processName != null) {
			LoggingResponseMessage logmsg = LoggingResponseMessage.builder()
					.message("Fetch template by 3 field.")
					.messageTypeId(MessageTypeConst.SUCCESS)
					.build();

			log.info(gson.toJson(logmsg));

			NotificationType.valueOf(notificationType.toUpperCase());
			ProcessName.valueOf(processName.toUpperCase());
			List<TemplateModel> findAll = iTemplateRepository.findAll();

			if(!findAll.isEmpty()) {
				List<TemplateModel> result = new ArrayList<>();
				for(TemplateModel t:findAll) {
					int num = 0;
					for(String str:notificationChannel) {

						for(String s:t.getNotificationChannel()) {

							if(s.equalsIgnoreCase(str) && t.getNotificationType().equalsIgnoreCase(notificationType) && t.getProcessName().equalsIgnoreCase(processName)) {
								num++;
							}

						}
					}
					if(num == notificationChannel.length) {
						result.add(t);
					}
				}
				List<TemplateModel> newResult = result;

				LoggingResponseMessage logmsg3 = LoggingResponseMessage.builder()
						.message("Fetch all template by 3 field.")
						.messageTypeId(MessageTypeConst.SUCCESS)
						.data(newResult)
						.build();

				log.info(gson.toJson(logmsg3));

				if(count != null && result.size() > count) {
					newResult = new ArrayList<>();
					for(int i=result.size()-1; i>=result.size()-count; i--) {
						newResult.add(result.get(i));
					}
				}
				else if(count == null) {
					newResult = result.subList(result.size()-1, result.size());
					count = 1;
				}
				else {
					Collections.reverse(newResult);
				}

				LoggingResponseMessage logmsg4 = LoggingResponseMessage.builder()
						.message("Fetch "+count+" template by 3 field.")
						.messageTypeId(MessageTypeConst.SUCCESS)
						.data(newResult)
						.build();

				log.info(gson.toJson(logmsg4));

				response = FetchAllTemplateResponce.builder()
						.message("Successfully fetch template.")
						.status(200)
						.templateList(newResult).build();
			}
			else {
				throw new NotificationException("No template in the data base.");
			}
		}
		else if(notificationChannel != null && processName != null) {
			LoggingResponseMessage logmsg3 = LoggingResponseMessage.builder()
					.message("Fetch template by 2 field.")
					.messageTypeId(MessageTypeConst.SUCCESS)
					.build();

			log.info(gson.toJson(logmsg3));

			ProcessName.valueOf(processName.toUpperCase());

			List<TemplateModel> findAll = iTemplateRepository.findAll();
			if(!findAll.isEmpty()) {
				List<TemplateModel> result = new ArrayList<>();
				for(TemplateModel t:findAll) {
					int num = 0;
					for(String str:notificationChannel) {

						for(String s:t.getNotificationChannel()) {
							if(s.equalsIgnoreCase(str) && t.getProcessName().equalsIgnoreCase(processName)) {
								num++;
							}
						}
					}
					if(num==notificationChannel.length) {
						result.add(t);
					}
				}
				List<TemplateModel> newResult = result;
				LoggingResponseMessage logmsg = LoggingResponseMessage.builder()
						.message("Fetch all template by 2 field.")
						.messageTypeId(MessageTypeConst.SUCCESS)
						.data(newResult)
						.build();

				log.info(gson.toJson(logmsg));

				if(count != null && result.size() > count) {
					newResult = new ArrayList<>();
					for(int i=result.size()-1; i>=result.size()-count; i--) {
						newResult.add(result.get(i));
					}
					log.info(gson.toJson(newResult));
				}
				else if(count == null) {
					newResult = result.subList(result.size()-1, result.size());
					count = 1;
				}
				else {
					Collections.reverse(newResult);
				}
				LoggingResponseMessage logmsg4 = LoggingResponseMessage.builder()
						.message("Fetch "+count+" template by 2 field.")
						.messageTypeId(MessageTypeConst.SUCCESS)
						.data(newResult)
						.build();

				log.info(gson.toJson(logmsg4));
				response = FetchAllTemplateResponce.builder()
						.message("Successfully fetch template.")
						.status(200)
						.templateList(newResult).build();

			}
			else {
				throw new NotificationException("No template in the data base.");
			}

		}
		else if(notificationChannel != null && notificationType != null) {
			LoggingResponseMessage logmsg4 = LoggingResponseMessage.builder()
					.message("Fetch template by 2 field.")
					.messageTypeId(MessageTypeConst.SUCCESS)
					.build();

			log.info(gson.toJson(logmsg4));

			NotificationType.valueOf(notificationType.toUpperCase());

			List<TemplateModel> findAll = iTemplateRepository.findAll();
			if(!findAll.isEmpty()) {
				List<TemplateModel> result = new ArrayList<>();
				for(TemplateModel t:findAll) {
					int num = 0;
					for(String str:notificationChannel) {
						for(String s:t.getNotificationChannel()) {
							if(s.equalsIgnoreCase(str) && t.getNotificationType().equalsIgnoreCase(notificationType)) {
								num++;
							}
						}
					}
					if(num==notificationChannel.length) {
						result.add(t);
					}
				}
				List<TemplateModel> newResult = result;
				LoggingResponseMessage logmsg = LoggingResponseMessage.builder()
						.message("Fetch all template by 2 field.")
						.messageTypeId(MessageTypeConst.SUCCESS)
						.data(newResult)
						.build();

				log.info(gson.toJson(logmsg));

				if(count != null && result.size() > count) {
					newResult = new ArrayList<>();
					for(int i=result.size()-1; i>=result.size()-count; i--) {

						newResult.add(result.get(i));
					}
					log.info(gson.toJson(newResult));
				}
				else if(count == null) {
					newResult = result.subList(result.size()-1, result.size());
					count  = 1;
				}
				else {
					Collections.reverse(newResult);
				}

				LoggingResponseMessage logmsg3 = LoggingResponseMessage.builder()
						.message("Fetch "+count+" template by 2 field.")
						.messageTypeId(MessageTypeConst.SUCCESS)
						.data(newResult)
						.build();

				log.info(gson.toJson(logmsg3));

				response = FetchAllTemplateResponce.builder()
						.message("Successfully fetch template.")
						.status(200)
						.templateList(newResult).build();

			}
			else {
				throw new NotificationException("No template in the data base.");
			}
		}
		else if(notificationChannel != null) {
			LoggingResponseMessage logmsg4 = LoggingResponseMessage.builder()
					.message("Fetch template by 1 field.")
					.messageTypeId(MessageTypeConst.SUCCESS)
					.build();

			log.info(gson.toJson(logmsg4));

			List<TemplateModel> findAll = iTemplateRepository.findAll();
			if(!findAll.isEmpty()) {
				List<TemplateModel> result = new ArrayList<>();
				for(TemplateModel t:findAll) {
					int num = 0;
					for(String str:notificationChannel) {

						for(String s:t.getNotificationChannel()) {
							if(s.equalsIgnoreCase(str)) {
								num++;
							}
						}	
					}
					if(num== notificationChannel.length) {
						result.add(t);
					}
				}

				List<TemplateModel> newResult = result;
				LoggingResponseMessage logmsg = LoggingResponseMessage.builder()
						.message("Fetch all template by 1 field.")
						.messageTypeId(MessageTypeConst.SUCCESS)
						.data(newResult)
						.build();

				log.info(gson.toJson(logmsg));

				if(count != null && result.size() > count) {
					newResult = new ArrayList<>();
					for(int i=result.size()-1; i>=result.size()-count; i--) {
						newResult.add(result.get(i));
					}
					log.info(gson.toJson(newResult));
				}
				else if(count == null) {
					newResult = result.subList(result.size()-1, result.size());
					count = 1;
				}
				else {
					Collections.reverse(newResult);
				}
				LoggingResponseMessage logmsg3 = LoggingResponseMessage.builder()
						.message("Fetch "+count+" template by 1 field.")
						.messageTypeId(MessageTypeConst.SUCCESS)
						.data(newResult)
						.build();

				log.info(gson.toJson(logmsg3));

				response = FetchAllTemplateResponce.builder()
						.message("Successfully fetch template.")
						.status(200)
						.templateList(newResult).build();
			}
			else {
				throw new NotificationException("No template in the data base.");
			}
		}
		else if(processName != null && notificationType != null) {
			LoggingResponseMessage logmsg4 = LoggingResponseMessage.builder()
					.message("Fetch template by 2 field.")
					.messageTypeId(MessageTypeConst.SUCCESS)
					.build();

			log.info(gson.toJson(logmsg4));

			NotificationType.valueOf(notificationType.toUpperCase());
			ProcessName.valueOf(processName.toUpperCase());

			List<TemplateModel> result = iTemplateRepository.findByNotificationTypeAndProcessName(notificationType.toUpperCase(), processName.toUpperCase());

			LoggingResponseMessage logmsg3 = LoggingResponseMessage.builder()
					.message("Fetch all template by 2 field.")
					.messageTypeId(MessageTypeConst.SUCCESS)
					.data(result)
					.build();

			log.info(gson.toJson(logmsg3));

			if(!result.isEmpty()) {
				List<TemplateModel> newResult = result;
				if(count != null && result.size() > count) {
					newResult = new ArrayList<>();
					for(int i=result.size()-1; i>=result.size()-count; i--) {
						newResult.add(result.get(i));
					}
					log.info(gson.toJson(newResult));
				}
				else if(count == null) {
					newResult = result.subList(result.size()-1, result.size());
					count = 1;
				}
				else {
					Collections.reverse(newResult);
				}

				LoggingResponseMessage logmsg = LoggingResponseMessage.builder()
						.message("Fetch "+count+" template by 2 field.")
						.messageTypeId(MessageTypeConst.SUCCESS)
						.data(newResult)
						.build();

				log.info(gson.toJson(logmsg));

				response = FetchAllTemplateResponce.builder()
						.message("Successfully fetch template.")
						.status(200)
						.templateList(newResult).build();
			}
			else {
				throw new NotificationException("No template in the data base.");
			}
		}
		else if(notificationType != null || processName != null) {
			LoggingResponseMessage logmsg4 = LoggingResponseMessage.builder()
					.message("Fetch template by 1 field.")
					.messageTypeId(MessageTypeConst.SUCCESS)
					.build();

			log.info(gson.toJson(logmsg4));

			if(notificationType != null) {
				NotificationType.valueOf(notificationType.toUpperCase());
				notificationType = notificationType.toUpperCase();
			}
			if(processName != null) {
				ProcessName.valueOf(processName.toUpperCase());
				processName = processName.toUpperCase();
			}
			List<TemplateModel> result = iTemplateRepository.findByNotificationTypeOrProcessName(notificationType, processName);

			LoggingResponseMessage logmsg = LoggingResponseMessage.builder()
					.message("Fetch all template by 1 field.")
					.messageTypeId(MessageTypeConst.SUCCESS)
					.data(result)
					.build();

			log.info(gson.toJson(logmsg));

			if(!result.isEmpty()) {
				List<TemplateModel> newResult = result;
				if(count != null && result.size() > count) {
					newResult = new ArrayList<>();
					for(int i=result.size()-1; i>=result.size()-count; i--) {
						newResult.add(result.get(i));
					}
					log.info(gson.toJson(newResult));
				}
				else if(count == null) {
					count = 1;
					newResult = result.subList(result.size()-1, result.size());
				}
				else {
					Collections.reverse(newResult);
				}
				LoggingResponseMessage logmsg3 = LoggingResponseMessage.builder()
						.message("Fetch "+count+" template by 1 field.")
						.messageTypeId(MessageTypeConst.SUCCESS)
						.build();

				log.info(gson.toJson(logmsg3));

				response = FetchAllTemplateResponce.builder()
						.message("Successfully fetch template.")
						.status(200)
						.templateList(newResult).build();
			}
			else {
				throw new NotificationException("No template in the data base.");
			}
		}
		LoggingResponseMessage logmsg4 = LoggingResponseMessage.builder()
				.message("Fetch template by attribute is successfully fetched.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.data(response)
				.build();

		log.info(gson.toJson(logmsg4));

		return response;
	}

}
