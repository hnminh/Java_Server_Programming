package servlet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddMessageServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.setContentType("text/html");

		PrintWriter out = response.getWriter();

		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<body>");

		out.println("<h1>Error</h1>");
		out.println("<p>Cannot perform GET method on this</p>");
		
		
		out.println("<a href='add_message.html'>Add message</a>");

		out.println("</body>");
		out.println("</html>");
		out.close();
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String userName = new String();
		String message = new String();
		String[] sports = null, views = null;
		Date date = new Date();
		
		Map<String, String[]> paramMap = request.getParameterMap();
		
		if (paramMap != null) {
			Iterator<Entry<String, String[]>> iterator = paramMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, String[]> entry = iterator.next();
				String key = entry.getKey();
				if (key.equals("userName")) {
					userName = entry.getValue()[0];
				}
				if (key.equals("message")) {
					message = entry.getValue()[0];
				}
				if (key.equals("sportOptions")) {
					sports = entry.getValue();
				}
				if (key.equals("viewOptions")) {
					views = entry.getValue();
				}
			}
			MessagesData.messages.addElement(new Message(userName, message, sports, views, date));
		}
		
		
		// Here we save data to a text file
		String filename = this.getServletContext().getRealPath(System.getProperty("file.separator")) + "data.txt";
		FileOutputStream fos = new FileOutputStream(filename);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(MessagesData.messages);
		oos.close();
		fos.close();
		

		response.setContentType("text/html");

		PrintWriter out = response.getWriter();

		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<body>");
		
		out.println("<a href='add_message.html'><button>Add message</button></a>");
		out.println("<a href='findby_name.html'><button>Find message by name</button></a>");
		out.println("<a href='findby_date.html'><button>Find message by date</button></a>");

		out.println("<h1>Forum</h1>");
		
		Enumeration<Message> vEnum = MessagesData.messages.elements();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		
		while (vEnum.hasMoreElements()) {
			Message mess = vEnum.nextElement();
			out.println("<p>" + formatter.format(mess.getDate()) + "</p>");
			out.println("<p>" + mess.getName() + ": " + mess.getMessage() + "</p>");
			out.println("<p>Favorite sports</p><ul>");
			if (mess.getSports() != null) {
				for (String sport : mess.getSports()) {
					if (sport != null) {
						out.println("<li>" + sport + "</li>");
					}
				}
			}
			out.println("</ul>");
			out.println("<p>Favorite views</p><ul>");
			if (mess.getViews() != null) {
				for (String view : mess.getViews()) {
					if (view != null) {
						out.println("<li>" + view + "</li>");
					}
				}
			}
			out.println("</ul>");
			out.println("<hr/>");
		}
		out.println("<p>Message has been sent successfully</p>");


		out.println("</body>");
		out.println("</html>");
		out.close();
	}
}