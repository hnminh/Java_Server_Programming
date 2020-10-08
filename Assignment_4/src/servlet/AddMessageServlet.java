package servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

public class AddMessageServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyy_HHmmss");
	
	String uploadFilePath;
    String relativePath;
    
    String dataFileName;
    String subDirName;
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
        separator = System.getProperty("file.separator");
        dataFileName = this.getInitParameter("data_file");
        subDirName = this.getInitParameter("sub_dir_name");
        filePath = this.getServletContext().getRealPath(separator) + subDirName + separator;
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
		
		String fileName = null;
        File fileObj = null;

        for (Part part : request.getParts()) {
        	fileName = getFileName(part);
        	
        	if (!fileName.equals("")) {
                fileObj = new File(fileName);
                fileName = fileObj.getName();
                // Here we rename the file by adding user name and date before the name of the
                // file
                fileName = userName + "_" + dateFormat.format(date) + "_" + fileName;
                fileObj = new File(uploadFilePath + fileName);
                part.write(fileObj.getAbsolutePath());
                
                MessagesData.messages.addElement(new Message(userName, message, date, fileName));
        	}
        }
        
        // Here we save data to a text file
 		FileOutputStream fos = new FileOutputStream(filePath);
 		ObjectOutputStream oos = new ObjectOutputStream(fos);
 		oos.writeObject(MessagesData.messages);
 		oos.close();
 		fos.close();
        
        response.sendRedirect("index.html");
		
	}
}