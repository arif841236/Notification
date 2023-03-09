package com.template.exception;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.google.gson.Gson;
import com.template.model.common.LoggingResponseMessage;
import com.template.model.common.MessageTypeConst;
import lombok.extern.slf4j.Slf4j;

/**
 * This class for handle all exception
 */
@ControllerAdvice
@Slf4j
public class ExceptionHandlerGlobal extends ResponseEntityExceptionHandler {

	@Autowired
	Gson gson;

	String message = "Some internal issue.";

	@ExceptionHandler(NotificationException.class)
	public ResponseEntity<TemplateErrorResponce> userExceptionHandler(NotificationException ue, WebRequest wb, Exception e) {

		TemplateErrorResponce error = new TemplateErrorResponce(HttpStatus.UNPROCESSABLE_ENTITY.value(), ue.getMessage(),wb.getDescription(false).substring(4));

		LoggingResponseMessage msgStart = LoggingResponseMessage.builder()
				.message(message)
				.messageTypeId(MessageTypeConst.ERROR)
				.data(error)
				.build();

		log.error(gson.toJson(msgStart));
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(TemplateException.class)
	public ResponseEntity<TemplateErrorResponce> templateExceptionHandler(TemplateException ue, WebRequest wb) {
		TemplateErrorResponce error = new TemplateErrorResponce(HttpStatus.UNPROCESSABLE_ENTITY.value(), ue.getMessage(),wb.getDescription(false).substring(4));

		LoggingResponseMessage msgStart = LoggingResponseMessage.builder()
				.message(message)
				.messageTypeId(MessageTypeConst.INTERNAL_ERROR)
				.data(error)
				.build();

		log.error(gson.toJson(msgStart));
		return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	/**
	 * 
	 * @param nValid :its argument of validate.
	 * @return : its return response entity of Map.
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		List<FieldError> error = ex.getBindingResult().getFieldErrors();
		TemplateErrorResponce otpVException = new TemplateErrorResponce(HttpStatus.UNPROCESSABLE_ENTITY.value(),
				error.get(0).getDefaultMessage(),request.getDescription(false).substring(4));

		LoggingResponseMessage msgStart = LoggingResponseMessage.builder()
				.message(message)
				.messageTypeId(MessageTypeConst.ERROR)
				.data(error)
				.build();

		log.error(gson.toJson(msgStart));

		return new ResponseEntity<>(otpVException, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		TemplateErrorResponce resp = new TemplateErrorResponce(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex.getMessage(),request.getDescription(false).substring(4));

		LoggingResponseMessage msgStart = LoggingResponseMessage.builder()
				.message(message)
				.messageTypeId(MessageTypeConst.ERROR)
				.data(resp)
				.build();

		log.error(gson.toJson(msgStart));

		return new ResponseEntity<>(resp, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		ErrorResponce error = ErrorResponce.builder()
				.errorCode(1)
				.status(HttpStatus.NOT_FOUND.value())
				.errorMessage(HttpStatus.NOT_FOUND.name())
				.path(request.getDescription(false).substring(4))
				.timestamp(Timestamp.valueOf(LocalDateTime.now()))
				.traceID(Instant.now().toEpochMilli())
				.errorDetails(ex.getMessage())
				.build();

		LoggingResponseMessage msgStart = LoggingResponseMessage.builder()
				.message(message)
				.messageTypeId(MessageTypeConst.ERROR)
				.data(error)
				.build();

		log.error(gson.toJson(msgStart));

		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@Override
	protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		ErrorResponce error = ErrorResponce.builder()
				.errorCode(1)
				.status(HttpStatus.METHOD_NOT_ALLOWED.value())
				.errorMessage(HttpStatus.METHOD_NOT_ALLOWED.name())
				.path(request.getDescription(false).substring(4))
				.timestamp(Timestamp.valueOf(LocalDateTime.now()))
				.traceID(Instant.now().toEpochMilli())
				.errorDetails(ex.getMessage())
				.build();

		LoggingResponseMessage msgStart = LoggingResponseMessage.builder()
				.message(message)
				.messageTypeId(MessageTypeConst.ERROR)
				.data(error)
				.build();

		log.error(gson.toJson(msgStart));

		return new ResponseEntity<>(error, HttpStatus.METHOD_NOT_ALLOWED);
	}

	@ExceptionHandler(NullPointerException.class)
	public final ResponseEntity<Object> nullpointerExceptions(NullPointerException ex, WebRequest request) {
		ErrorResponce error = ErrorResponce.builder()
				.errorCode(1)
				.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.errorMessage(HttpStatus.INTERNAL_SERVER_ERROR.name())
				.path(request.getDescription(false).substring(4))
				.timestamp(Timestamp.valueOf(LocalDateTime.now()))
				.traceID(Instant.now().toEpochMilli())
				.errorDetails(ex.getMessage())
				.build();

		LoggingResponseMessage msgStart = LoggingResponseMessage.builder()
				.message(message)
				.messageTypeId(MessageTypeConst.ERROR)
				.data(error)
				.build();

		log.error(gson.toJson(msgStart));

		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(SQLException.class)
	public final ResponseEntity<Object> sqlExceptions(SQLException ex, WebRequest request) {
		ErrorResponce error = ErrorResponce.builder()
				.errorCode(1)
				.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.errorMessage(HttpStatus.INTERNAL_SERVER_ERROR.name())
				.path(request.getDescription(false).substring(4))
				.timestamp(Timestamp.valueOf(LocalDateTime.now()))
				.traceID(Instant.now().toEpochMilli())
				.errorDetails(ex.getMessage())
				.build();

		LoggingResponseMessage msgStart = LoggingResponseMessage.builder()
				.message(message)
				.messageTypeId(MessageTypeConst.ERROR)
				.data(error)
				.build();

		log.error(gson.toJson(msgStart));

		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}


	@ExceptionHandler(IllegalArgumentException.class)
	public final ResponseEntity<Object> IllegalArgumentExceptionHandler(IllegalArgumentException ex, WebRequest request) {
		TemplateErrorResponce resp = new TemplateErrorResponce(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex.getMessage(),request.getDescription(false).substring(4));

		LoggingResponseMessage msgStart = LoggingResponseMessage.builder()
				.message(message)
				.messageTypeId(MessageTypeConst.ERROR)
				.data(resp)
				.build();

		log.error(gson.toJson(msgStart));

		return new ResponseEntity<>(resp, HttpStatus.UNPROCESSABLE_ENTITY);

	}

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
		ErrorResponce error = ErrorResponce.builder()
				.errorCode(1)
				.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.errorMessage(HttpStatus.INTERNAL_SERVER_ERROR.name())
				.path(request.getDescription(false).substring(4))
				.timestamp(Timestamp.valueOf(LocalDateTime.now()))
				.traceID(Instant.now().toEpochMilli())
				.errorDetails(ex.getMessage())
				.build();

		LoggingResponseMessage msgStart = LoggingResponseMessage.builder()
				.message(message)
				.messageTypeId(MessageTypeConst.ERROR)
				.data(error)
				.build();

		log.error(gson.toJson(msgStart));

		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		ErrorResponce error = ErrorResponce.builder()
				.errorCode(1).status(HttpStatus.NOT_FOUND.value())
				.errorMessage(HttpStatus.NOT_FOUND.name())
				.path(request.getDescription(false).substring(4))
				.timestamp(Timestamp.valueOf(LocalDateTime.now()))
				.traceID(Instant.now().toEpochMilli())
				.errorDetails(ex.getMessage())
				.build();

		LoggingResponseMessage msgStart = LoggingResponseMessage.builder()
				.message(message)
				.messageTypeId(MessageTypeConst.ERROR)
				.data(error)
				.build();

		log.error(gson.toJson(msgStart));

		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
}
