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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import utility.Helper;

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


	public DBHandler(String dbName) throws Exception {

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

			key = resourceBundle.getString("encryption_key");
			
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
	
	public boolean AccountExist(String dbTableName, String username, String password) {
		
		// data validation
		username = utility.Helper.StripInvalidDataPattern(username);
		password = utility.Helper.StripInvalidDataPattern(password);
		
		String query = selectQuery + dbTableName + " WHERE user_name = '" + username + "' AND password = SHA1('" + password + "')";
		StringBuilder queryResult = new StringBuilder();
		
		queryResult.append(openConnection());
		
		try {
			statement = dbConnection.createStatement();
			
			resultSet = statement.executeQuery(query);
			
			resultSetMetaData = resultSet.getMetaData();
			
			if (!resultSet.next()) return false;
			
		} catch (SQLException e) {
			queryResult.append(e.getMessage());
		} finally {
			queryResult.append(closeConnection());
		}
		
		return true;
	}
	
	public boolean AccountDuplicated(String dbTableName, String username) {
		
		// data validation
		username = utility.Helper.StripInvalidDataPattern(username);
		
		String query = selectQuery + dbTableName + " WHERE user_name = '" + username + "'";
		StringBuilder queryResult = new StringBuilder();
		
		queryResult.append(openConnection());
		
		try {
			statement = dbConnection.createStatement();
			
			resultSet = statement.executeQuery(query);
			
			resultSetMetaData = resultSet.getMetaData();
			
			if (!resultSet.next()) return false;
			
		} catch (SQLException e) {
			queryResult.append(e.getMessage());
		} finally {
			queryResult.append(closeConnection());
		}
		
		return true;
	}
	
	public boolean PreparedStatementRegister(String dbTableName, String username, String password) {
		
		// data validation
		username = utility.Helper.StripInvalidDataPattern(username);
		password = utility.Helper.StripInvalidDataPattern(password);
		
		if (username.isEmpty() || password.isEmpty()) return false;
		
		StringBuilder queryResult = new StringBuilder();
		
		String insertCommand = "INSERT INTO " + dbTableName + " VALUES (?, SHA1(?))";
		
		queryResult.append(openConnection());

		int updatedRows = 0;
		try {

			ps = dbConnection.prepareStatement(insertCommand);			
			
			// Here we set the value for the first parameter
			ps.setString(1, username);

			// Here we set the value for the second parameter
			ps.setString(2, password);

			// Here we execute the PreparedStatement
			updatedRows = ps.executeUpdate();
			
		} catch (SQLException e) {
			queryResult.append("<p>" + e.getMessage() + "</p>");
		} finally {
			queryResult.append(closeConnection());
		}
		if (updatedRows != 0)
			return true;
		else
			return false;
	}
	
	public boolean PreparedStatementInsertReceipt(String dbTableName, String username, String name, String address, String receipt) {
		StringBuilder queryResult = new StringBuilder();
		
		String insertCommand = "INSERT INTO " + dbTableName + " VALUES (?, ?, ?, ?, ?, ?)";
		
		queryResult.append(openConnection());

		int updatedRows = 0;
		try {

			ps = dbConnection.prepareStatement(insertCommand);			
			
			// Here we set the value for the first parameter
			ps.setString(1, username);
			// Here we set the value for the second parameter
			ps.setString(2, name);
			
			ps.setString(3, address);
			
			String[] productsAndTotal = receipt.split("#");
			ps.setString(4, productsAndTotal[0]);
			ps.setString(5, productsAndTotal[1]);

			ps.setString(6, formatter.format(new Date(System.currentTimeMillis())));

			// Here we execute the PreparedStatement
			updatedRows = ps.executeUpdate();
			
		} catch (SQLException e) {
			queryResult.append("<p>" + e.getMessage() + "</p>");
		} finally {
			queryResult.append(closeConnection());
		}
		if (updatedRows != 0)
			return true;
		else
			return false;
	}
	
	public String GetReceipt(String dbTableName, String username) {
		
		StringBuilder result = new StringBuilder();
		
		String query = selectQuery + dbTableName + " WHERE user_name = '" + username + "'";
		StringBuilder queryResult = new StringBuilder();
		
		queryResult.append(openConnection());
		
		try {
			statement = dbConnection.createStatement();
			
			resultSet = statement.executeQuery(query);
			
			while (resultSet.next()) {
				String name = resultSet.getObject(2).toString();
				String address = resultSet.getObject(3).toString();
				String[] products = resultSet.getObject(4).toString().split(";");
				String totalPrice = resultSet.getObject(5).toString();
				String date = resultSet.getObject(6).toString();
				
				result.append("<table style='padding: 20px; border: 1px solid black'>");
				result.append("<tr><th>Date:</th><td>" + date + "</td></tr>");
				result.append("<tr><th>Name:</th><td>" + name + "</td></tr>");
				result.append("<tr><th>Address:</th><td>" + address + "</td></tr>");
				result.append("<tr></tr><tr><th>Product</th><th>Quantity</th><th>Unit price</th></tr>");
				for (String product : products) {
					String[] p = product.split(":");
					result.append("<tr>");
					for (String pi : p)
						result.append("<td>" + pi + "</td>");
					result.append("</tr>");
				}
				result.append("</table>");
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			queryResult.append(closeConnection());
		}
		return result.toString();
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