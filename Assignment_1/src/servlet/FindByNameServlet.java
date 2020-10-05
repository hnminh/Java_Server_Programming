package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FindByNameServlet extends HttpServlet {

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
		
		String requestedUserName = request.getParameter("userName");
		
		response.setContentType("text/html");

		PrintWriter out = response.getWriter();

		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<body>");
		
		out.println("<a href='add_message.html'><button>Add message</button></a>");
		out.println("<a href='findby_name.html'><button>Find message by name</button></a>");
		out.println("<a href='findby_date.html'><button>Find message by date</button></a>");

		out.println("<h1>Search for: " + requestedUserName + "</h1>");

		Enumeration<Message> vEnum = AddMessageServlet.messages.elements();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		
		while (vEnum.hasMoreElements()) {
			Message mess = (Message) vEnum.nextElement();
			
			if (mess.getName().contains(requestedUserName)) {
				out.println("<p>" + formatter.format(mess.getDate()) + "</p>");
				out.println("<p>" + mess.getName() + ": " + mess.getMessage() + "</p><hr/>");
			}
		}

		out.println("</body>");
		out.println("</html>");
		out.close();
		
	}
}
