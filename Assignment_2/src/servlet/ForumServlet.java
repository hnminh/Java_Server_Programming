package servlet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ForumServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String filename = this.getServletContext().getRealPath(System.getProperty("file.separator")) + "data.txt";
		try {
			FileInputStream fis = new FileInputStream(filename);
			ObjectInputStream ois = new ObjectInputStream(fis);
			MessagesData.messages = (Vector<Message>) ois.readObject();
			ois.close();
			fis.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			System.out.println("Error initializing stream");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
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
			Message mess = (Message) vEnum.nextElement();
			out.println("<p>" + formatter.format(mess.getDate()) + "</p>");
			out.println("<p>" + mess.getName() + ": " + mess.getMessage() + "</p><hr/>");
		}

		out.println("</body>");
		out.println("</html>");
		out.close();
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.setContentType("text/html");

		PrintWriter out = response.getWriter();

		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<body>");

		out.println("<h1>Error</h1>");
		out.println("<p>Cannot perform POST method on this</p>");
		
		
		out.println("<a href='add_message.html'>Add message</a>");

		out.println("</body>");
		out.println("</html>");
		out.close();
		
	}
}
