package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CookieReview extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Cookie[] cookies = request.getCookies();
		
		response.setContentType("text/html");

		PrintWriter out = response.getWriter();
		
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<body>");
		out.println("<h2>Cookies Content</h2>");
		for (Cookie c : cookies) {
//            if (c.getName().equals(utility.Helper.getCookieName())) {
            	// extract the cookie content
            	
        	out.println("<table>");
        	out.println("<tr><th>Name</th><td>" + c.getName() + "</td></tr>");
        	out.println("<tr><th>Value</th><td>" + c.getValue() + "</td></tr>");
        	out.println("<tr><th>Domain</th><td>" + c.getDomain() + "</td></tr>");
        	out.println("<tr><th>MaxAge</th><td>" + c.getMaxAge() + "</td></tr>");
        	out.println("<tr><th>Path</th><td>" + c.getPath() + "</td></tr>");
        	out.println("<tr><th>Comment</th><td>" + c.getComment() + "</td></tr>");
        	out.println("</table>");
//                break;
//            }
        }
		out.println("<form action='delete_cookies' method='POST'><input type='submit' name='submit' value='Delete Cookies'></form>");
		out.println("</body>");
		out.println("</html>");
		out.close();

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Cookie[] cookies = request.getCookies();
		for (Cookie c : cookies) {
			c.setMaxAge(0);
		}
		response.sendRedirect("index.html");

	}

}