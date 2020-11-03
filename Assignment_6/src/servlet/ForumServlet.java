package servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ForumServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String uploadFilePath;
    String relativePath;
    
    String dataFileName;
    String dataPath;
    String filePath;
    String separator;
    File filePathDir;
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
        
        // Here we init the stored data file
        dataFileName = getServletContext().getInitParameter("data_file");
        dataPath = getServletContext().getInitParameter("data_path");
        filePath = this.getServletContext().getRealPath(dataPath) + File.separator + dataFileName;
        filePathDir = new File(filePath);
        if (!filePathDir.exists()) {
            filePathDir.mkdir();
        }
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		try {
			FileInputStream fis = new FileInputStream(filePath);
			ObjectInputStream ois = new ObjectInputStream(fis);
			MessagesData.messages = (Vector<Message>) ois.readObject();
			ois.close();
			fis.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			System.out.println("Error initializing stream");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		response.setContentType("text/html");

		PrintWriter out = response.getWriter();

		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<body>");
		
		out.println("<a href='add_message.html'><button>Add message</button></a>");
		out.println("<a href='findby_name.html'><button>Find message by name</button></a>");
		out.println("<a href='logout.html'><button>Logout</button></a>");

		out.println("<h1>Forum</h1>");

		Enumeration<Message> vEnum = MessagesData.messages.elements();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		
		while (vEnum.hasMoreElements()) {
			Message mess = (Message) vEnum.nextElement();
			/*
			out.println("<p>" + formatter.format(mess.getDate()) + "</p>");
			out.println("<p>" + mess.getName() + ": " + mess.getMessage() + "</p>");
			out.println("<img src='" + relativePath + File.separator + mess.getImageName() + "' height='200'>");
			out.println("<hr/>");
			*/
			out.println(mess.objectToHTML(relativePath + File.separator));
		}

		out.println("</body>");
		out.println("</html>");
		out.close();
		
		HttpSession session = request.getSession();
		session.setMaxInactiveInterval(30);
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		doGet(request, response);
		
	}
}
