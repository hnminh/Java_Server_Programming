package servlet;

import java.util.Date;

public class Message {
	
	private String name;
	private String message;
	private Date date;
	
	public Message(String name, String message, Date date) {
		this.name = name;
		this.message = message;
		this.date = date;
	}
	
	public String getName() {
		return name;
	}
	
	public String getMessage() {
		return message;
	}
	
	public Date getDate() {
		return date;
	}

}
