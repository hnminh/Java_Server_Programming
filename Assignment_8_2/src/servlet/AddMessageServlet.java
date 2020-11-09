package servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import db.DBHandler;

public class AddMessageServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyy_HHmmss");
	
	String uploadFilePath;
    String relativePath;
    
    String dataFileName;
    String dataPath;
    String filePath;
    String separator;
    File filePathDir;
    
	String dbName = "e1800950_forum";
    
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
        
        dataFileName = getServletContext().getInitParameter("data_file");
        dataPath = getServletContext().getInitParameter("data_path");
        filePath = this.getServletContext().getRealPath(dataPath) + File.separator;
        filePathDir = new File(filePath);
        if (!filePathDir.exists()) {
            filePathDir.mkdir();
        }
        filePath += dataFileName;
    }
    
    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        if (contentDisp != null) {
            /*
             * In the following line we split a piece of text like the following: form-data;
             * name="fileName"; filename="C:\Users\Public\Pictures\Sample Pictures\img.jpg"
             */
            String[] tokens = contentDisp.split(";");
            for (String token : tokens) {
                if (token.trim().startsWith("filename")) {
                    return new File(token.split("=")[1].replace('\\', '/')).getName().replace("\"", "");
                }
            }
        }
        return "";
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.sendRedirect("add_message.html");
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String userName = request.getParameter("userName");
		String message = request.getParameter("message");
		Date date = new Date();
		
		// file uploading
		String fileName = null;
        File fileObj = null;

        ArrayList<String> files = new ArrayList<>();
        int numberOfFiles = 0;
        for (Part part : request.getParts()) {
        	fileName = getFileName(part);
        	
        	if (!fileName.equals("")) {
        		numberOfFiles += 1;
                fileObj = new File(fileName);
                fileName = fileObj.getName();
                // Here we rename the file by adding user name and date before the name of the file
                fileName = userName + "_" + dateFormat.format(date) + "_" + fileName;
                fileObj = new File(uploadFilePath + fileName);
                part.write(fileObj.getAbsolutePath());
                
                files.add(fileName);
        	}
        }
        if (numberOfFiles <= 1) {
        	
        	//MessagesData.messages.addElement(new Message(userName, message, date, files));
        	
        	// add to database
        	try {
				DBHandler dbHandler = new DBHandler(dbName, uploadFilePath);
				dbHandler.preparedStatementInsertData("Users", new Message(userName, message, date, files));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        
        response.sendRedirect("index.html");
		
	}
}