//This is the content of objects.ShoppingCart.java file
package objects;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServlet;
public class ShoppingCart extends HttpServlet {
	
    private static final long serialVersionUID = 1L;
    
    private Map<String, String> map;
    private String customer;
    @SuppressWarnings({ "rawtypes", "unchecked" })
    
    public ShoppingCart(String customer) {
        if (customer == null)
            this.customer = "Anonymous";
        else
            this.customer = customer;
        map = Collections.synchronizedMap(new HashMap());
    }
    
    public String getCustomer() {
        return this.customer;
    }
    
    public void put(String key, String value) {
        if (key == null || value == null)
            return;
        String currentValue;
        if ((currentValue = map.get(key)) != null) {
            Integer currentAmount = Integer.parseInt(currentValue) + Integer.parseInt(value);
            map.put(key, currentAmount + "");
        } else {
            map.put(key, value);
        }
    }
    
    public void remove(String key) {
        if (key == null)
            return;
        if (map.containsKey(key)) {
            map.remove(key);
        }
    }
    
    public void removeAll() {
        if (map != null)
            map.clear();
    }
    
    public Map<String, String> getMap() {
        return map;
    }
    
    public String getValues() {
    	
    	Double totalPrice = (double) 0;
    	
        StringBuffer content = new StringBuffer("<tr><th>Product</th><th>Amount</th><th>Unit Price</th></tr>");
		// Here we explicitly synchronise since we use an iterator
		// This makes the the map thread safe so that only one thread a time
		// can access the map
        
        synchronized (map) {
        	// Iterator i=keys.iterator();
            Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
            
            while (iterator.hasNext()) {
                Map.Entry<String, String> me = (Map.Entry<String, String>) iterator.next();
                totalPrice += Double.parseDouble(utility.Helper.getProductPrice(me.getKey()))*Integer.parseInt(me.getValue());
                
                content.append("<tr><form id='entry' method='post' action='handle_shopping_cart'><td><input type='text' name='product' value='"
                                + me.getKey() + "' readonly></td><td><input type='text' name='amount' value='"
                                + me.getValue() + "'></td><td><input type='text' name='unit_price' value='"
                                + utility.Helper.getProductPrice(me.getKey()) + "' readonly></td>"
                                + "<td><input type='submit' name='submit' value='Delete'></td>"
                                + "<td><input type='submit' name='submit' value='Update'></td></tr></form>");
            }
        } // end of synchronised
        
        content.append("<tr></tr><tr><th>Total Price</th><td><input type='text' name='product' value='" + 
        				+ totalPrice + "' readonly></td></tr>");
        content.append("<tr><td><input form='entry' type='submit' name='submit' value='Empty cart'></td></tr>");
        return content.toString();
    }
    
    public String summary() {
    	Double totalPrice = (double) 0;
    	
        StringBuffer content = new StringBuffer("<tr><th>Product</th><th>Amount</th><th>Unit Price</th></tr>");
		// Here we explicitly synchronise since we use an iterator
		// This makes the the map thread safe so that only one thread a time
		// can access the map
        
        synchronized (map) {
        	// Iterator i=keys.iterator();
            Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
            
            while (iterator.hasNext()) {
                Map.Entry<String, String> me = (Map.Entry<String, String>) iterator.next();
                totalPrice += Double.parseDouble(utility.Helper.getProductPrice(me.getKey()))*Integer.parseInt(me.getValue());
                
                content.append("<tr><td>" + me.getKey() + "</td><td>" + me.getValue() + "</td><td>" 
                				+ utility.Helper.getProductPrice(me.getKey()) + "</td>");
            }
        } // end of synchronised
        
        content.append("<tr></tr><tr><th>Total Price: </th><td>" + 
        				+ totalPrice + "</td></tr>");
        return content.toString();
    }
    
    public String objectToDBFormat() {
    	Double totalPrice = (double) 0;
    	
        StringBuffer content = new StringBuffer();
		// Here we explicitly synchronise since we use an iterator
		// This makes the the map thread safe so that only one thread a time
		// can access the map
        
        synchronized (map) {
        	// Iterator i=keys.iterator();
            Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
            
            while (iterator.hasNext()) {
                Map.Entry<String, String> me = (Map.Entry<String, String>) iterator.next();
                totalPrice += Double.parseDouble(utility.Helper.getProductPrice(me.getKey()))*Integer.parseInt(me.getValue());
                
                // product name : amount : price
                content.append(me.getKey() + ":" + me.getValue() + ":" + utility.Helper.getProductPrice(me.getKey()) + ";");
            }
        } // end of synchronised
        
        content.append("#" + totalPrice);
        
        return content.toString();
    }
    
    public int getSize() {
        return map.size();
    }
    @Override
    public String toString() {
        return getClass().getName() + "[" + map + "]";
    }
}