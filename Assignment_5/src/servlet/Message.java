package servlet;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String message;
	private Date date;
	private String imageName;
	
	private static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	public Message(String name, String message, Date date, String imageName) {
		this.name = name;
		this.message = message;
		this.date = date;
		this.imageName = imageName;
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
	
	public String getImageName() {
		return imageName;
	}
	
	public String objectToHTML(String filePath) {
		String result = "";
		
		result += "<p>" + Message.formatter.format(date) + "</p>";
		result += "<p>" + name + ": " + message + "</p>";
		result += "<img src='" + filePath + imageName + "' height='200'>";
		result += "<hr/>";
		
		return result;
	}

}
