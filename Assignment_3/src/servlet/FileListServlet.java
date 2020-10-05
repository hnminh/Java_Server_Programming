package servlet;

import java.io.File;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FileListServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String separator = File.separator;
	private String downloadDir;

	public void init() {
		// Here we specify the path to the directory where downloadable files
		// reside.
		// We read the name of the download directory from web.xml file, where
		// the name of
		// the directory is specified under "download-dir" parameter.
		downloadDir = getServletContext().getRealPath(getServletContext().getInitParameter("download-dir"))
				+ separator;
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		
		response.setContentType("text/html");

		PrintWriter out = response.getWriter();

		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<body>");

		out.println("<h1>Index of download_files</h1><hr/>");
		
		String[] fileList = new File(downloadDir).list();
		System.out.println(downloadDir);
		
		out.println("<form action='fileDownload_servlet' method='POST'>");
		out.println("<select name='fileName'>");
		for (String fileName : fileList) {
			out.println("<option value='" + fileName + "'>" + fileName);
		}
		
		out.println("</select>");
		out.println("<input type='submit' value='Download'>");
		out.println("</form>");

		out.println("</body>");
		out.println("</html>");
		out.close();
	}
	

}
