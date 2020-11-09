package servlet;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Message implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String message;
	private Date date;
	private ArrayList<String> imageNames;
	
	private static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	public Message(String name, String message, Date date, ArrayList<String> imageNames) {
		this.name = name;
		this.message = message;
		this.date = date;
		this.imageNames = imageNames;
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
	
	public ArrayList<String> getImageNames() {
		return imageNames;
	}
	
	public String objectToHTML(String filePath) {
		String result = "";
		
		result += "<p>" + Message.formatter.format(date) + "</p>";
		result += "<p>" + name + ": " + message + "</p>";
		for (String imageName : imageNames) {
			result += "<img src='" + filePath + imageName + "' height='200'>";
		}
		result += "<hr/>";
		
		return result;
	}

}
