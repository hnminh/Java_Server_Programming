package db;

import java.io.File;
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
	private ResourceBundle resourceBundle;
	
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");


	public DBHandler(String dbName, String dbUsername, String dbPassword) throws Exception {

		this.dbName = dbName;
		this.dbUsername = dbUsername;
		this.dbPassword = dbPassword;
		try {

			resourceBundle = ResourceBundle.getBundle("db.settings", new Locale(""));

			dbServerURL = resourceBundle.getString("db_server_url").trim();

			serverTimeZone = serverTimeZone + resourceBundle.getString("server_time_zone").trim();

			dbDriver = resourceBundle.getString("db_driver").trim();
			selectQuery = resourceBundle.getString("select_query") + " ";

			// Here we specify the database full path as well as the time zone in which the
			// query is executed.
			dbURL = dbServerURL + "/" + dbName + serverTimeZone;
			
			System.out.println(dbURL);

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

	
	public String getDataTable(String dbTableName, String filePath) {

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
			int columns = resultSetMetaData.getColumnCount();

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
				
				if (imageName != null)
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
	
	public String findMessageByName(String queryName, String dbTableName, String filePath) {

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
			int columns = resultSetMetaData.getColumnCount();

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
				
				if (imageName != null)
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

	public String createTable(String tableName) {

		String createTableQuery = "CREATE TABLE " + tableName
				+ " (NAME VARCHAR(40), MESSAGE VARCHAR(50000), DATE DATE, IMAGENAME VARCHAR(512))";

		StringBuilder queryResult = new StringBuilder();
		queryResult.append("<h2>Results for '" + createTableQuery + "':</h2>");
		queryResult.append(openConnection());

		try {

			statement = dbConnection.createStatement();
			statement.executeUpdate(createTableQuery);

			queryResult.append("<p>Table '" + tableName + "' was created successfully!</p>");
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
				ps.setString(4, message.getImageNames().get(0));
			}

			// Here we execute the PreparedStatement
			updatedRows = ps.executeUpdate();
			queryResult.append("<p>Data was added to " + dbTableName + " successfully; number of updated rows=" + updatedRows);
		} catch (SQLException e) {
			queryResult.append("<p>" + e.getMessage() + "</p>");
		} finally {
			queryResult.append(closeConnection());
		}

		return queryResult.toString();

	}
/*
	public String preparedStatemenUpdateData(String dbTableName, String tableRowData) {

		if (tableRowData == null || tableRowData.isEmpty())
			return Utility.getInvalidDataErrorMessage();

		StringBuilder queryResult = new StringBuilder();
		String[] columnData = tableRowData.split(";");
		// If enough data is not provided, we print the error message and quit.
		if (columnData.length < 3) {
			queryResult.append("<p>Enough data was not provided!</p>");
			return queryResult.toString();
		} else {

			if (!Utility.validateNumberParameter(columnData[0])) {

				queryResult.append("<p>" + columnData[0] + " is not a valid number!</p>");
				return queryResult.toString();
			}

			if (!Utility.validateTextParameter(columnData[1])) {

				queryResult.append("<p>" + columnData[1] + " is not valid!</p>");
				return queryResult.toString();
			}

			if (!Utility.validatePhoneNumber(columnData[2])) {

				queryResult.append("<p>" + columnData[2] + " is not a valid phone number!</p>");
				return queryResult.toString();
			}

		}

		String updateCommand = "update " + dbTableName + " set NAME=?, PHONE=? where ID=?";
		queryResult.append("<h2>Results for '" + updateCommand + "':</h2>");
		queryResult.append(openConnection());

		int updatedRows = 0;
		try {

			PreparedStatement ps = dbConnection.prepareStatement(updateCommand);

			// Here we set the value for the second parameter
			ps.setString(1, columnData[1]);

			// Here we set the value for the second parameter
			ps.setString(2, columnData[2]);

			// Here we set the value for the where condition
			ps.setInt(3, Integer.parseInt(columnData[0]));

			// Here we execute the PreparedStatement
			updatedRows = ps.executeUpdate();
			queryResult.append(
					"<p>Data was added to " + dbTableName + " successfully; number of updated rows=" + updatedRows);
		} catch (SQLException e) {
			queryResult.append("<p>" + e.getMessage() + "</p>");
		} finally {
			queryResult.append(closeConnection());
		}

		return queryResult.toString();

	}
*/
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