package servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SaveRemoteContentServlet extends HttpServlet {

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
		downloadDir = getServletContext().getRealPath(getServletContext().getInitParameter("download-dir")) + separator;

	}

	private void displayError(HttpServletRequest request, HttpServletResponse response, String error) {
		response.setContentType("text/html");
		try {
			// Here we initialise the PrintWriter object
			PrintWriter out = response.getWriter();
			// Here we print HTML tags
			out.println("<!DOCTYPE html>");
			out.println("<html>");
			out.println("<head>");
			out.println("<title>View Resource Servlet Error Message</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<center>");
			out.println("<h1>" + error + "</h1>");
			out.println("<p><b>Error:</b> " + error);
			out.println("<p><a href='save_content.html'>Back</a>");
			out.println("</center>");
			out.println("</body>");
			out.println("</html>");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String error = null;
		URL url = null;
		URLConnection urlConnection = null;
		PrintWriter printWriter = null;
		BufferedReader reader = null;
		printWriter = response.getWriter();

		try {
			// Here we access the web resource within the web application
			// as a URL object
			// url = getServletContext().getResource(filePath);

			String siteName = request.getParameter("site_name");
			String fileName = request.getParameter("fileName");

			if (siteName == null || siteName.isEmpty()) {
				error = "Siten name has not been specified!";
				return;

			}

			if (fileName == null || fileName.isEmpty()) {
				fileName = "abc.html";
			}

			if (!fileName.contains(".html")) {
				if (!fileName.contains(".")) {
					fileName += ".html";
				}
			}

			url = new URL(siteName);

			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-16");
			urlConnection = url.openConnection();

			// Here we establish connection with URL representing web.xml
			urlConnection.connect();

			// The following would be useful to read data in binary format
			/*
			 * BufferedInputStream inputStream = new
			 * BufferedInputStream(urlConnection.getInputStream()); int readByte;
			 * while((readByte=inputStream.read())!=-1) printWriter.write(readByte);
			 */
			reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			String line = "";
			String urlContent = "";
			while ((line = reader.readLine()) != null) {
				urlContent += line;
			}

			PrintWriter writer = new PrintWriter(downloadDir + fileName, "UTF-8");
			writer.print(urlContent);
			writer.close();

			printWriter.println("<h1>File successfully saved</h1>");

			printWriter.println("<hr><p><a href='save_content.html'>Back</a></p>");
		} catch (Exception e) {
			error = "Something wrong with: " + url.toString() + " " + e;
			displayError(request, response, error);
		} finally {
			// Here we close the input/output streams
			if (printWriter != null)
				printWriter.close();
			if (reader != null) {
				reader.close();
			}
		}
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Here we redirect the request to the index page
		response.sendRedirect("save_content.html");
		return;
	}
}