package servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FindByNameServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String uploadFilePath;
    String relativePath;
    public void init() {
        // Here we get the absolute path of the destination directory
        // uploadFilePath = this.getServletContext().getRealPath(UPLOAD_DIR) +
        // File.separator;
        
        relativePath = getServletContext().getInitParameter("upload_path");
        
        uploadFilePath = this.getServletContext().getRealPath(relativePath)
                + File.separator;
        /*
         * uploadFilePath =
         * this.getServletContext().getRealPath(getServletConfig().getInitParameter(
         * "upload_path")) + File.separator;
         */
        // Here we create the destination directory under the project main directory if
        // it does not exists
        File fileSaveDir = new File(uploadFilePath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdirs();
        }
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.sendRedirect("findby_name.html");
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String requestedUserName = request.getParameter("userName");
		
		response.setContentType("text/html");

		PrintWriter out = response.getWriter();

		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<body>");
		
		out.println("<a href='add_message.html'><button>Add message</button></a>");
		out.println("<a href='findby_name.html'><button>Find message by name</button></a>");

		out.println("<h1>Search for: " + requestedUserName + "</h1>");

		ArrayList<Message> messages = MessagesData.SearchByName(requestedUserName);
		for (Message message : messages) {
			out.println(message.objectToHTML(relativePath + File.separator));
		}

		out.println("</body>");
		out.println("</html>");
		out.close();
		
	}
}
