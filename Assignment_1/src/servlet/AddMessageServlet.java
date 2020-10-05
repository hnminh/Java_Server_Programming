package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddMessageServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static Vector<Message> messages = new Vector<Message>();
	
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

		String userName = request.getParameter("userName");
		String message = request.getParameter("message");
		Date date = new Date();
		
		messages.addElement(new Message(userName, message, date));

		response.setContentType("text/html");

		PrintWriter out = response.getWriter();

		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<body>");
		
		out.println("<a href='add_message.html'><button>Add message</button></a>");
		out.println("<a href='findby_name.html'><button>Find message by name</button></a>");
		out.println("<a href='findby_date.html'><button>Find message by date</button></a>");

		out.println("<h1>Forum</h1>");
		
		Enumeration<Message> vEnum = messages.elements();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		
		while (vEnum.hasMoreElements()) {
			Message mess = (Message) vEnum.nextElement();
			out.println("<p>" + formatter.format(mess.getDate()) + "</p>");
			out.println("<p>" + mess.getName() + ": " + mess.getMessage() + "</p><hr/>");
		}
		out.println("<p>Message has been sent successfully</p>");


		out.println("</body>");
		out.println("</html>");
		out.close();
	}
}