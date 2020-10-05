package servlet;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String message;
	private Date date;
	private String[] sports;
	private String[] views;
	
	public Message(String name, String message, String[] sports, String[] views, Date date) {
		this.name = name;
		this.message = message;
		this.date = date;
		this.sports = sports;
		this.views = views;
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
	
	public String[] getSports() {
		return sports;
	}
	
	public String[] getViews() {
		return views;
	}

}
