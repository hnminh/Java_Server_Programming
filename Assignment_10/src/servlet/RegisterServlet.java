package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import db.DBHandler;

public class RegisterServlet extends HttpServlet {

	/**
	 * 
	 */
	
	String dbName = "e1800950_shop";
	
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.sendRedirect("login.html");

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<body>");
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String password_re = request.getParameter("password_re");
		
		if (password.equals(password_re)) {
			try {
				DBHandler dbHandler = new DBHandler(dbName);
				if (dbHandler.AccountDuplicated("users", username)) {
					out.println("<p>Username has been chosen. Please choose another username.</p>");
					out.println("<a href='register.html'><button>Register</button></a>");
				}
				else {
					if (dbHandler.PreparedStatementRegister("users", username, password)) {
						out.println("<p>Register successfully. Please log in.</p>");
						out.println("<a href='login.html'><button>Log in</button></a>");
					} else {
						out.println("<p>Cannot register. Please try again</p>");
						out.println("<a href='register.html'><button>Register</button></a>");
					}
					
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		out.println("</body>");
		out.println("</html>");
		out.close();
		

	}

}