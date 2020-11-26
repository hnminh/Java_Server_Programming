//This is the content of servlet/ShopServlet.java file.
package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import utility.Helper;

public class ShopServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private String productHtmlList;
	private String amountHtmlList;

	public ShopServlet() {
		super();
	}

	public void init() {

		productHtmlList = utility.Helper.getProductHtmlList();
		amountHtmlList = utility.Helper.getAmounttHtmlList();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
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
        } else {
	        
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			
			// Here we get access to the current session
			HttpSession session = request.getSession();
			if (session.isNew()) {
				session.setAttribute("sessionID", session.getId());
				session.setAttribute("objects.ShoppingCart", new objects.ShoppingCart(request.getParameter("customer")));
			}
			
			Object customer = null;
			
			if (session != null)
				customer = session.getAttribute("customer");
			
			out.println("<!DOCTYPE html><html><head><title>Online Shop</title><style>th { text-align:left;}</style></head><body>");
			out.println("<a href='receipt.html'><button>Receipt</button></a>");
			out.println("<a href='logout.html'><button>Log out</button></a>");
			out.println("<h1>Online Shop</h1>");
			out.println("<p>" + (request.getParameter("info") == null ? "" : request.getParameter("info")) + "</p>");
			out.println("<form method='post' action='handle_shopping_cart'>"
						+ "<table><tr><th>User name:</th><td>" + username);
			out.println("</td></tr>" + "<tr><th>Product:</th><td>" + productHtmlList
						+ "</td><th>Amount:</th>" + "<td>" + amountHtmlList + "</td>"
						+ "<td> <input type='submit' name='submit' value='Add To Cart'></td></tr>");
			out.println("</table>");
			out.println("<hr/>");
			out.println("<p style='text-align:center'><input style='border: none; background: none; display: inline; color: blue; text-decoration: underline;' type='submit' name='submit' value='Go to summary'></p>");
			out.println("</form>");
			out.println("</body></html>");
			out.close();
        }
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}