//This is the content of logic.HandleShoppingCart.java file
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
import utility.Helper;
public class CheckOutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("index.html");
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	
    	// Here we get access to the current session
        HttpSession session = request.getSession();
		if (session.getAttribute("username") == null) {
			response.sendRedirect("login.html");
		}
		String username = (String) session.getAttribute("username");
        
        String action = request.getParameter("check_out");
        if (action == null)
            response.sendRedirect("index.html");
        
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html><html><head><title>Online Shop</title><style>th { text-align:left;}</style></head><body>");
        
        // Here we create a local variable which holds the shopping cart
        objects.ShoppingCart shoppingCart = null;
        
        // Here we get access to the shopping cart
        if (session != null)
            shoppingCart = (objects.ShoppingCart) session.getAttribute("objects.ShoppingCart");
        
        out.println("<form method='POST' action='upload_to_db'>");
        out.println("<table>");
        out.println("<tr><th>Name</th><td><input type='text' size='40' name='recipient_name' required></td></tr>");
        out.println("<tr><th>Address</th><td><input type='text' size='40' name='address' required></td></tr>");
        
        if (shoppingCart.getSize() < 1) {
        	response.sendRedirect("index.html");
        } else {
            out.println("<tr><th>Shopping Cart</th></tr>");
            
            // Here we get access to the shopping cart
            out.println(shoppingCart.summary());
        }

        out.println("</table>");
        out.println("<hr/>");
        out.println("<input type='submit' name='submit' value='Check out'></form>");
        out.println("</body></html>");
        out.close();
    }
}