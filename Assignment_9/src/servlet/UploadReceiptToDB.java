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

public class UploadReceiptToDB extends HttpServlet {

	/**
	 * 
	 */
	
	String dbName = "e1800950_shop";
	
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.sendRedirect("index.html");

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
        
        // Here we get access to the current session
        HttpSession session = request.getSession();
        
        // Here we create a local variable which holds the shopping cart
        objects.ShoppingCart shoppingCart = null;
        
        // Here we get access to the shopping cart
        if (session != null)
            shoppingCart = (objects.ShoppingCart) session.getAttribute("objects.ShoppingCart");
		
        String name = request.getParameter("recipient_name");
        String address = request.getParameter("address");
        
		//doGet(request, response);
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<body>");
		
		try {
			DBHandler dbHandler = new DBHandler(dbName);
			if (dbHandler.PreparedStatementInsertReceipt("receipts", username, name, address, shoppingCart.objectToDBFormat())) {
				out.println("<p>Upload successful</p>");
				
				// set cookies after log in
				
		        
		        //response.sendRedirect("index.html");
			}
			else {
				//response.sendRedirect("login_error.html");
				out.println("<p>Upload error</p>");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.sendRedirect("index.html");
		out.println("</body>");
		out.println("</html>");
		out.close();

		
		
	}

}