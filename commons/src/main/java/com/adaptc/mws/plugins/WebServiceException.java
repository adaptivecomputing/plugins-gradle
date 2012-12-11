package com.adaptc.mws.plugins;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This exception is designed to be thrown from a plugin's custom web service.  It has fields
 * for specifying the messages to be returned as well as the HTTP response code.
 * @author bsaville
 */
public class WebServiceException extends Throwable {
	private static final long serialVersionUID = -5471717572752385451L;

	private List<String> messages = new ArrayList<String>();
	private Integer responseCode;

	/**
	 * Empty constructor.  If no additional values are set, this will return a 500 error
	 * with a default message.
	 */
	public WebServiceException() { /* Empty */ }

	/**
	 * If no additional values are set, this will return a list of messages containing the
	 * specified message as the single element and a 500 error.
	 * @param message A message to return to the user
	 */
	public WebServiceException(String message) {
		setMessages(message);
	}

	/**
	 * If no additional values are set, this will return the default message and the
	 * specified response code.
	 * @param responseCode The HTTP response code to return to the user
	 */
	public WebServiceException(Integer responseCode) {
		setResponseCode(responseCode);
	}

	/**
	 * This will return a list of errors containing the specified message and the specified
	 * response code.
	 * @param message A message to return to the user
	 * @param responseCode HTTP The response code to return to the user
	 */
	public WebServiceException(String message, Integer responseCode) {
		setMessages(message);
		setResponseCode(responseCode);
	}

	/**
	 * This will return the list of errors specified and the specified response code.
	 * @param messages A list of messages to return to the user
	 * @param responseCode The HTTP response code to return to the user
	 */
	public WebServiceException(List<String> messages, Integer responseCode) {
		setMessages(messages);
		setResponseCode(responseCode);
	}

	/**
	 * Sets the messages to a list with a single element as the specified message.
	 * @param message A message to return to the user
	 */
	public void setMessages(String message) {
		this.messages = Arrays.asList(message);
	}

	/**
	 * Sets the messages to the specified list.
	 * @param messages A list of messages to return to the user
	 */
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	/**
	 * Retrieves the list of messages for this exception.
	 * @return The current value
	 */
	public List<String> getMessages() {
		return messages;
	}

	/**
	 * Sets the response code for this exception.
	 * @param responseCode The HTTP response code to return to the user
	 */
	public void setResponseCode(Integer responseCode) {
		this.responseCode = responseCode;
	}

	/**
	 * Retrieves the HTTP response code for this exception.
	 * @return The current value
	 */
	public Integer getResponseCode() {
		return responseCode;
	}
}
