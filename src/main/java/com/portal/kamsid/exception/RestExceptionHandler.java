package com.portal.kamsid.exception;

import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.portal.kamsid.util.ApiResponse;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	protected ResponseEntity<Object> handleNotFound(ResourceNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.failure(ex.getMessage()));
	}

	@ExceptionHandler(InsufficientStockException.class)
	protected ResponseEntity<Object> handleInsufficient(InsufficientStockException ex) {
		// 409 Conflict is suitable when business rule prevents the operation.
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.failure(ex.getMessage()));
	}

	@ExceptionHandler(ConflictException.class)
	protected ResponseEntity<Object> handleConflict(ConflictException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.failure(ex.getMessage()));
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	protected ResponseEntity<Object> handleDataIntegrity(DataIntegrityViolationException ex) {
		// database constraint violation
		return ResponseEntity.badRequest()
				.body(ApiResponse.failure("Database constraint violation: " + ex.getMostSpecificCause().getMessage()));
	}

	@ExceptionHandler(OptimisticLockingFailureException.class)
	protected ResponseEntity<Object> handleOptimisticLock(OptimisticLockingFailureException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(ApiResponse.failure("Concurrent update error. Please retry."));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	protected ResponseEntity<Object> handleIllegal(IllegalArgumentException ex) {
		return ResponseEntity.badRequest().body(ApiResponse.failure(ex.getMessage()));
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, org.springframework.http.HttpStatusCode status, WebRequest request) {
		String msg = ex.getBindingResult().getFieldErrors().stream()
				.map(fe -> fe.getField() + " : " + fe.getDefaultMessage()).collect(Collectors.joining(", "));
		return ResponseEntity.badRequest().body(ApiResponse.failure(msg));
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Object> handleAll(Exception ex) {
		ex.printStackTrace();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(ApiResponse.failure("Internal server error"));
	}
}
