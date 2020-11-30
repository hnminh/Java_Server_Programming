package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import db.DBHandler;

public class LoginServlet extends HttpServlet {

	/**
	 * 
	 */
	
	String dbName = "e1800950_shop";
	
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.sendRedirect("login.html");

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//doGet(request, response);
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<body>");
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		//String buttonClicked = request.getParameter("")
		
		try {
			DBHandler dbHandler = new DBHandler(dbName);
			if (dbHandler.AccountExist("users", username, password)) {
				out.println("<p>Login successful</p>");
				
				// Here we get access to the current session
				HttpSession session = request.getSession();
				session.setAttribute("sessionID", session.getId());
				session.setAttribute("objects.ShoppingCart", new objects.ShoppingCart(username));
				session.setAttribute("username", username);

				
				// set cookies after log in
				Cookie[] cookies = null;
		        Cookie userCookie = null;
		        String cookieContent = null;
		        // Get an array of Cookies associated with this domain
		        cookies = request.getCookies();
		        boolean cookieFound = false;
		        if (cookies != null) {
		            for (Cookie c : cookies) {
		                if (c.getName().equals(utility.Helper.getCookieName())) {
		                	// extract the cookie content
		                	cookieContent = c.getValue();
		                	
		                    cookieFound = true;
		                    break;
		                }
		            }
		        }
		        if (!cookieFound) {
		            // Here we create cookies for user name
		            // user name cannot contain white space or any of the following characters:
		            //[ ] ( ) = , " / ? @ : ;
		            String cookieValue = username;
		            userCookie = new Cookie(utility.Helper.getCookieName(), utility.Helper.stripCookieValue(cookieValue));
		            // Here we set the expire date for the cookie.
		            userCookie.setMaxAge(utility.Helper.getCookieAge());
		            // Add bothet the cookies in the response header.
		            response.addCookie(userCookie);
		        } else {
		        	// put cookie content to shopping cart
		        	String[] products = cookieContent.split("#");
		        	for (String product : products) {
						String[] p = product.split("!");
						
						objects.ShoppingCart shoppingCart = (objects.ShoppingCart) session.getAttribute("objects.ShoppingCart");
						if (p.length >= 2)
							shoppingCart.put(p[0], p[1]);
					}
		        }
		        
		        response.sendRedirect("index.html");
			}
			else {
				response.sendRedirect("login_error.html");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.println("</body>");
		out.println("</html>");
		out.close();

	}

}