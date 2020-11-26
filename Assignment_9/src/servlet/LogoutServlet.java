package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Cookie[] cookies = null;
        // Get an array of Cookies associated with this domain
        cookies = request.getCookies();
        boolean cookieFound = false;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals(utility.Helper.getCookieName())) {
                    cookieFound = true;
                    c.setMaxAge(0);
                    response.addCookie(c);
                }
            }
        }
		
		HttpSession session = request.getSession();
		session.invalidate();
		response.sendRedirect("index.html");

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);

	}

}