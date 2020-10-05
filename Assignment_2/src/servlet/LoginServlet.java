package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login.html")
public class LoginServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String adminUserName = "root";
		String adminPassword = "admin";
		
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		String action = request.getParameter("submit");
		
		boolean userNameCheck = checkParameter(userName);
		boolean passwordCheck = checkParameter(password);
		
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		
		out.println("<!DOCTYPE html>");
		out.println("<html><head><title>Login Window</title></head>");
		out.println("<body><h1>Login Form</h1>");
		out.println("<form action='login.html' method='GET'>");
		out.println("<table><tr>");
		out.println("<td>User name</td>");
		out.println("<td><input type='text' size='40' name='userName'></td>");
		if (action != null && !userNameCheck) {
			out.println("<td style='color: red'>Please enter an user name</td>");
		}
		out.println("</tr><tr>");
		out.println("<td>Password</td>");
		out.println("<td><input type='password' size='40' name='password'></td>");
		if (action != null && !passwordCheck) {
			out.println("<td style='color: red'>Please enter password</p>");
		}
		out.println("</tr><tr>");
		out.println("<td></td>");
		out.println("<td><input type='submit' name='submit' value='Login'></td>");
		out.println("</tr></table></form>");
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/forum_servlet");
		
		if (userNameCheck && passwordCheck) {
			if (userName.equals(adminUserName) && password.equals(adminPassword)) {
				GregorianCalendar calendar = new GregorianCalendar();
				String message = null;
	
				if (calendar.get(Calendar.AM_PM) == Calendar.AM) {
					message = "Good Morning";
	
				} else {
	
					message = "Good Afternoon";
	
				}
				out.println("<p>" + message + ", " + userName + "!</p>");
				
				dispatcher.forward(request, response);
			}
			else {
				out.println("<p>Invalid username or password!</p>");
			}
		}
		
		out.println("</body></html>");
	}
	
	private boolean checkParameter(String parameter) {
        if (parameter != null && !parameter.trim().isEmpty() && !parameter.equals("null"))
            return true;
        return false;
    }

}