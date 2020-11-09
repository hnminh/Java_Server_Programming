package db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import helper.Utility;
import servlet.Message;

public class DBHandler {

	private String dbName;
	private String dbUsername;
	private String dbPassword;
	private Connection dbConnection;
	private Statement statement;
	private PreparedStatement ps;
	private ResultSet resultSet;
	private ResultSetMetaData resultSetMetaData;

	private String dbServerURL;
	private String dbURL;
	private String selectQuery;
	private String dbDriver;
	private String serverTimeZone = "?serverTimezone=";
	private String[] acceptedImageSuffixes;
	private ResourceBundle resourceBundle;
	
    private static String resourceDir, tempImageDir, tempImageDirAbsolute; 
    private File tempImageDirAbsoluteFileObj;
    private String key;
	
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");


	public DBHandler(String dbName, String resourceDir) throws Exception {

		this.dbName = dbName;
		try {

			resourceBundle = ResourceBundle.getBundle("db.settings", new Locale(""));
			
			dbUsername = resourceBundle.getString("db_username").trim();
			dbPassword = resourceBundle.getString("db_password").trim();

			dbServerURL = resourceBundle.getString("db_server_url").trim();

			serverTimeZone = serverTimeZone + resourceBundle.getString("server_time_zone").trim();

			dbDriver = resourceBundle.getString("db_driver").trim();
			selectQuery = resourceBundle.getString("select_query") + " ";

			// Here we specify the database full path as well as the time zone in which the
			// query is executed.
			dbURL = dbServerURL + "/" + dbName + serverTimeZone;
			
			acceptedImageSuffixes = resourceBundle.getString("accepted_image_suffixes").trim().split(" ");
			key = resourceBundle.getString("encryption_key");
			
			this.resourceDir = resourceDir;
			

		} catch (Exception e) {
			throw new Exception();
		}

	}

	private String openConnection() {

		// For mySQL database the above code would look like this:
		try {

			Class.forName(dbDriver);

			// Here we create connection to the database
			dbConnection = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
			
		} catch (ClassNotFoundException e) {
			return e.getLocalizedMessage();
		} catch (SQLException e) {
			return e.getLocalizedMessage();
		}

		return "";

	}

	private String closeConnection() {

		try {
			// Here we close all open streams
			if (statement != null)
				statement.close();
			if (dbConnection != null)
				dbConnection.close();
		} catch (SQLException sqlexc) {
			return "Exception occurred while closing database connection!";
		}

		return "";

	}

	public void deleteDir(File file) {
		File[] contents = file.listFiles();
	    if (contents != null) {
	        for (File f : contents) {
	            f.delete();
	        }
	    }
	}
	
	public String getDataTable(String dbTableName, String filePath) {
		
		File tempImageDirAbsoluteFileObj = new File(resourceDir);
		if (tempImageDirAbsoluteFileObj.exists()) {
			deleteDir(tempImageDirAbsoluteFileObj);
			tempImageDirAbsoluteFileObj.delete();
        }
        tempImageDirAbsoluteFileObj.mkdirs();

		String query = selectQuery + dbTableName;
		StringBuilder queryResult = new StringBuilder();

		queryResult.append(openConnection());

		// Here we create the statement object for executing SQL commands.
		try {

			statement = dbConnection.createStatement();

			// Here we execute the SQL query and save the results to a ResultSet
			// object
			resultSet = statement.executeQuery(query);

			// Here we get the metadata of the query results
			resultSetMetaData = resultSet.getMetaData();
			
			// Here we calculate the number of columns
			//int columns = resultSetMetaData.getColumnCount();

			String date = null;
			String name = null;
			String message = null;
			String imageName = null;
			while (resultSet.next()) {
				
				if (resultSet.getObject(3) != null)
					date = resultSet.getObject(3).toString();
				
				name = resultSet.getObject(1).toString();
				message = resultSet.getObject(2).toString();
				
				if (resultSet.getObject(4) != null)
					imageName = resultSet.getObject(4).toString();
				
				queryResult.append("<p>" + date + "</p>"); // date
				queryResult.append("<p>" + name + ": " + message + "</p>"); // name, message
				
				if (imageName != null) {
					ReadImageFromDB("Images", imageName);
					queryResult.append("<img src='" + filePath + imageName + "' height='200'>");
				}
				queryResult.append("<hr>");
				
			}
			

		} catch (SQLException e) {
			queryResult.append(e.getMessage());
		} finally {
			queryResult.append(closeConnection());
		}

		return queryResult.toString();
	}
	
	public String findMessageByName(String queryName, String dbTableName, String filePath) {
		
		File tempImageDirAbsoluteFileObj = new File(resourceDir);
		if (tempImageDirAbsoluteFileObj.exists()) {
			deleteDir(tempImageDirAbsoluteFileObj);
			tempImageDirAbsoluteFileObj.delete();
        }
        tempImageDirAbsoluteFileObj.mkdirs();

		String query = selectQuery + dbTableName + " WHERE NAME = '" + queryName + "'";
		StringBuilder queryResult = new StringBuilder();

		queryResult.append(openConnection());

		// Here we create the statement object for executing SQL commands.
		try {

			statement = dbConnection.createStatement();

			// Here we execute the SQL query and save the results to a ResultSet
			// object
			resultSet = statement.executeQuery(query);

			// Here we get the metadata of the query results
			resultSetMetaData = resultSet.getMetaData();
			
			// Here we calculate the number of columns
			//int columns = resultSetMetaData.getColumnCount();

			String date = null;
			String name = null;
			String message = null;
			String imageName = null;
			while (resultSet.next()) {
				
				if (resultSet.getObject(3) != null)
					date = resultSet.getObject(3).toString();
				
				name = resultSet.getObject(1).toString();
				message = resultSet.getObject(2).toString();
				
				if (resultSet.getObject(4) != null)
					imageName = resultSet.getObject(4).toString();
				
				ReadImageFromDB("Images", imageName);
				
				queryResult.append("<p>" + date + "</p>"); // date
				queryResult.append("<p>" + name + ": " + message + "</p>"); // name, message
				
				if (imageName != null)
					ReadImageFromDB("Images", imageName);
					queryResult.append("<img src='" + filePath + imageName + "' height='200'>");
				queryResult.append("<hr>");
				
			}

		} catch (SQLException e) {
			queryResult.append(e.getMessage());
		} finally {
			queryResult.append(closeConnection());
		}

		return queryResult.toString();
	}

	public String createUsersDataTable(String tableName) {

		String createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName
				+ " (NAME VARCHAR(40), MESSAGE VARCHAR(50000), DATE DATE, IMAGENAME VARCHAR(512))";

		StringBuilder queryResult = new StringBuilder();
		queryResult.append(openConnection());

		try {

			statement = dbConnection.createStatement();
			statement.executeUpdate(createTableQuery);

		} catch (SQLException e) {
			queryResult.append("<p>" + e.getMessage() + "</p>");
		} finally {
			queryResult.append(closeConnection());
		}

		return queryResult.toString();
	}

	public String preparedStatementInsertData(String dbTableName, Message message) {

		if (message == null)
			return Utility.getInvalidDataErrorMessage();
		
		createUsersDataTable(dbTableName);
		
		StringBuilder queryResult = new StringBuilder();
		
		String insertCommand = "INSERT INTO " + dbTableName + " VALUES (?, ?, ?, ?)";
		queryResult.append("<h2>Results for '" + insertCommand + "':</h2>");
		queryResult.append(openConnection());

		int updatedRows = 0;
		try {

			ps = dbConnection.prepareStatement(insertCommand);			
			
			// Here we set the value for the first parameter
			ps.setString(1, message.getName());

			// Here we set the value for the second parameter
			ps.setString(2, message.getMessage());

			// Here we set the value for the third parameter
			ps.setString(3, formatter.format(message.getDate()));
			
			// Here we set the value for the fourth parameter
			if (message.getImageNames().size() == 0) {
				ps.setString(4,  null);
			}
			else {
				//System.out.println(message.getImageNames().toString());
				String imageName = message.getImageNames().get(0);
				ps.setString(4, imageName);
				WriteImageToDB("Images", imageName);
			}
			

			// Here we execute the PreparedStatement
			updatedRows = ps.executeUpdate();
			//queryResult.append("<p>Data was added to " + dbTableName + " successfully; number of updated rows=" + updatedRows);
		} catch (SQLException e) {
			queryResult.append("<p>" + e.getMessage() + "</p>");
		} finally {
			queryResult.append(closeConnection());
		}

		return queryResult.toString();

	}
	
	public String createImageTable(String tableName) {

		String createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName
				+ " (IMAGENAME VARCHAR(512), IMAGE MEDIUMBLOB, IMAGESIZE INTEGER)";

		StringBuilder queryResult = new StringBuilder();
		//queryResult.append("<h2>Results for '" + createTableQuery + "':</h2>");
		queryResult.append(openConnection());

		try {

			statement = dbConnection.createStatement();
			statement.executeUpdate(createTableQuery);

			//queryResult.append("<p> Table '" + tableName + "' was created successfully! ");
		} catch (SQLException e) {
			queryResult.append("<p>" + e.getMessage() + "</p>");
		} finally {
			queryResult.append(closeConnection());
		}

		return queryResult.toString();
	}
	
	public String WriteImageToDB(String dbTableName, String imageName) {

		// Here we make sure that the image TABLE is created in the database
		createImageTable(dbTableName);

		StringBuilder queryResult = new StringBuilder();
		String insertImageCommand = "INSERT INTO " + dbTableName + " VALUES (?,?,?)";

		//queryResult.append("<h2>Results for '" + insertImageCommand + "':</h2>");
		int updatedRows = 0;

		try {
			queryResult.append(openConnection());

			// Here we initialize the preparedStatement object
			PreparedStatement ps = dbConnection.prepareStatement(insertImageCommand);
			
			ps.setString(1, imageName);
			
			File image = new File(resourceDir + imageName);
			
			FileInputStream fileInputStream = new FileInputStream(image.getAbsolutePath());
			
			ps.setBinaryStream(2, (InputStream)fileInputStream, (int)(image.length()));
			
			ps.setLong(3, image.length());
			
			//System.out.println(insertImageCommand);
			
			int counter = ps.executeUpdate();
			fileInputStream.close();
			
			if (counter == 0)
				queryResult.append(image.getName() + " data was not uploaded sucessfully!");
			else
				updatedRows++;
			
		} catch (SQLException e) {
			queryResult.append("<p>" + e.getMessage() + "</p>");
		} catch (IOException e) {
			queryResult.append("<p>" + e.getMessage() + "</p>");

		} finally {
			queryResult.append(closeConnection());

			queryResult.append("<p>" + updatedRows + " row(s) were updated successfully in " + dbTableName + ".</p>");
		}
		return queryResult.toString();

	}
	
	public String ReadImageFromDB(String tableName, String imageName) {
		
		String query = "SELECT * FROM " + tableName + " WHERE IMAGENAME = '" + imageName + "'";
		try {
			statement = dbConnection.createStatement();
			ResultSet imageResultSet = statement.executeQuery(query);
			
			while (imageResultSet.next()) {
				File destinationFile = new File(resourceDir + imageName);
				FileOutputStream fileOutputStream = new FileOutputStream(destinationFile);
			
				InputStream inputStream = imageResultSet.getBinaryStream(2);
				byte[] imageBuffer = new byte[inputStream.available()];
				inputStream.read(imageBuffer);
				
				fileOutputStream.write(imageBuffer);
				fileOutputStream.close();

			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return "";
	}

	public String getDbServerURL() {
		return dbServerURL;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDbUsername() {
		return dbUsername;
	}

	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
}