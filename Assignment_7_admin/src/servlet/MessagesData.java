package servlet;

import java.util.ArrayList;
import java.util.Vector;

public class MessagesData {
	
	public static Vector<Message> messages = new Vector<Message>();
	
	public static ArrayList<Message> SearchByName(String requestedUserName) {
		
		ArrayList<Message> result = new ArrayList<>();
		
		for (Message msg : MessagesData.messages) {
			if (msg.getName().contains(requestedUserName)) {
				result.add(msg);
			}
		}
		
		return result;
	}

}
