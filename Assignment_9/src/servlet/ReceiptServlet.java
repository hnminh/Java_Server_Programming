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

public class ReceiptServlet extends HttpServlet {

	/**
	 * 
	 */
	
	String dbName = "e1800950_shop";
	
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Cookie[] cookies = null;
        Cookie userCookie = null;
        String username = "";
        // Get an array of Cookies associated with this domain
        cookies = request.getCookies();
        boolean cookieFound = false;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals(utility.Helper.getCookieName())) {
                    cookieFound = true;
                    username = c.getValue();
                }
            }
        }
        if (!cookieFound) {
        	response.sendRedirect("login.html");
        }
    	
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<body>");
		out.println("<a href='index.html'><button>Home</button></a>");

		
		try {
			DBHandler dbHandler = new DBHandler(dbName);
			out.println(dbHandler.GetReceipt("receipts", username));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.println("</body>");
		out.println("</html>");
		out.close();

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);

	}

}