//This is the content of logic.HandleShoppingCart.java file
package logic;
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
public class HandleShoppingCart extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    public void init() {
    }
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("index.html");
    }
    
    @Override
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
        
        String action = request.getParameter("submit");
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
        
        if (action.equalsIgnoreCase("Delete")) {
            String key = request.getParameter("product");
            shoppingCart.remove(key);
        }
        
        if (action.equalsIgnoreCase("Update")) {
            String key = request.getParameter("product");
            shoppingCart.remove(key);
            session.setAttribute("customer", request.getParameter("customer"));
            shoppingCart.put(request.getParameter("product"), request.getParameter("amount"));
        }
        
        if (action.equalsIgnoreCase("Add To Cart")) {
            session.setAttribute("customer", request.getParameter("customer"));
            shoppingCart.put(request.getParameter("product"), request.getParameter("amount"));
        }
        
        if (action.equalsIgnoreCase("Empty Cart")) {
            if (session != null)
                session.invalidate();
            response.sendRedirect("index.html?info=Shopping cart is empty now!");
            return;
        }
        
        out.println("<table>");
        out.println("<tr><th>Customer:</th><td>" + username + "</td></tr>");
        
        if (shoppingCart.getSize() < 1) {
            out.println("<p style='color:red;'>Shopping Cart is empty!</p>");
        } else {
            out.println("<p>Shopping Cart:</p>");
            
            // Here we get access to the shopping cart
            out.println(shoppingCart.getValues());
        }

        out.println("</table>");
        out.println("<hr />");
        out.println("<p style='text-align: center;'><a href='index.html'>Continue Shopping</a></p>");
        out.println("<form method='POST' action='checkout'>");
        out.println("<p style='text-align: center;'><input type='submit' name='check_out' value='Check out'></form></p>");
        out.println("</body></html>");
        out.close();
    }
}