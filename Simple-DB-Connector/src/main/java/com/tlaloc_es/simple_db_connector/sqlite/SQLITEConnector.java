package com.tlaloc_es.simple_db_connector.sqlite;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.tlaloc_es.simple_db_connector.DBConnector;
import com.tlaloc_es.simple_db_connector.exceptions.VersionException;
import com.tlaloc_es.simple_db_connector.sqlite.exceptions.SQLiteException;

/**
 * This class provides a series of functions that facilitate access to the SQLite database.
 */
public abstract class SQLiteConnector extends DBConnector{

	/**
	 * Constructs a new SQLiteConnector
	 * @param dbPath Absolute path of the sqlite database.
	 * @throws SQLiteException
	 */
	public SQLiteConnector(String dbPath) throws SQLiteException {
		super("jdbc:sqlite:", dbPath);
		createNewDatabase(dbPath);
	}

	/**
	 * Constructs a new SQLiteConnector
	 * @param dbPath Absolute path of the sqlite database.
	 * @param version Version of DB.
	 * @pre The version should be higher or equal for the version to change and greather than 0.
	 * @throws SQLiteException
	 */
	public SQLiteConnector(String dbPath, int version) throws SQLiteException {
		super("jdbc:sqlite:", dbPath);	
			createNewDatabase(dbPath);
			setVersion(version);
		}
	
	/**
	 * Implement this to provide behavior when versioned a database.
     *         This function is called when the database is versioned
	 */
	public abstract void onSetVersioned();
	
	/**
	 * Implement this to provide behavior when creating a database.
     *         This function is called when the database file does not exist and had to be created.
	 */
	public abstract void onCreate();
	
	/**
	 * Implement this to provide behavior when update a database version.
     *         This function is called when the database version is updated.
	 * @param oldVersion The version that had the database until it was updated.
	 * @param newVersion The version that will now have the database.
	 */
	public abstract void onUpgrade(int oldVersion, int newVersion);
	
	/**
	 * Create a database in the indicated path, if exist, if it does not do anything.
	 * @param dbPath Absolute path of the sqlite database.
	 * @throws SQLiteException
	 */
	private void createNewDatabase(String dbPath) throws SQLiteException{
		try {
			Class.forName("org.sqlite.JDBC");
			String url = "jdbc:sqlite:" + dbPath;
			
			File tmpDir = new File(dbPath);
			boolean exists = tmpDir.exists();
			
		    try (Connection connect = DriverManager.getConnection(url)) {
		    	if (connect != null) {
		    		DatabaseMetaData meta = connect.getMetaData();	
		        }
		        connect.close();
		        
		        tmpDir = new File(dbPath);
		        boolean exists2 = tmpDir.exists();
		        
		        if(!exists){
		        	if(exists2){
		        		onCreate();
		        	}
		        }
		        
		    } catch (Exception e) {
		    	throw new SQLiteException(e.getMessage());
		    }
		} catch (Exception e) {
			throw new SQLiteException(e.getMessage());
		}
    }
	
	/**
	 * If the database is not versioned, start the version.
	 * @param version Number of version to be established.
	 * @pre The version should be higher or equal for the version to change and greather than 0.
	 * @throws SQLiteException
	 */
	private void setVersion(int version) throws SQLiteException {
		if(version <= 0){
			throw new VersionException("You can not set a version number lower than 1");
		}else{
			if(isVersioned()){
				int actualVersion = getVersion();
				if(version > actualVersion){
					onUpgrade(actualVersion, version);
					updateVersion(version);
				}else if(version < actualVersion){
					throw new VersionException("You can not set a version number lower than the current one");
				}
			}else{
				setSimpleDBConnectorTable();
				setInitVersion(version);
				onSetVersioned();
			}
		}
	}
	
	/**
	 * Execute a query to update the version in the database.
	 * @param version Number of version to be established.
	 * @throws SQLiteException
	 */
	private void updateVersion(int version) throws SQLiteException{
		connect();
		PreparedStatement ps = prepareStatement(Querys.UPDATE_VERSION.toString());
		
		try {
			ps.setInt(1, version);
			ps.executeUpdate();
		} catch (Exception e) {
			throw new SQLiteException(e.getMessage());
		} finally {
	        closePrepareStatement(ps);
	    }
		close();
	}
	
	/**
	 * Run a query to insert the version
	 * @pre There should not be a version.
	 * @param version Number of version to be established.
	 * @throws SQLiteException
	 */
	private void setInitVersion(int version) throws SQLiteException{
		connect();
		PreparedStatement ps = prepareStatement(Querys.INSERT_VERSION.toString());
		try {
			ps.setInt(1, version);
			ps.execute();
		} catch (Exception e) {
			throw new SQLiteException(e.getMessage());
		} finally {
	        closePrepareStatement(ps);
	    }
		close();
		
	}

	/**
	 * Execute a query to establish a control table for Simple DB Connector
	 * @pre There should not have created the Simple DB Connector control table.
	 * @throws SQLiteException
	 */
	private void setSimpleDBConnectorTable()throws SQLiteException{
		connect();
		PreparedStatement ps = prepareStatement(Querys.SET_VERSIONED.toString());
		try {
			ps.execute();
		} catch (Exception e) {
			throw new SQLiteException(e.getMessage());
		} finally {
	        closePrepareStatement(ps);
	    }
		close();
	}
	
	/**
	 * Executes the SQL query in this \ref PreparedStatement object and returns the ResultSet object generated by the query.
	 * @param ps A sql \ref PreparedStatement.
	 * @return ResultSet Object that contains the data produced by the query; never null
	 * @throws SQLiteException
	 */
	public ResultSet executeQuery(PreparedStatement ps) throws SQLiteException{
		ResultSet resultSet = null;
		connect();
		try {
			resultSet = ps.executeQuery();
		} catch (Exception e) {
			throw new SQLiteException(e.getMessage());
		} finally {
	        closePrepareStatement(ps);
	    }
		close();
		return resultSet;
	}
	
	/**
	 * Executes the given SQL statement, which may return multiple results.
	 * @param ps A sql preparement statement.
	 * @return boolean true if the first result is a ResultSet object; false if it is an update count or there are no results.
	 * @throws SQLiteException
	 */
	public boolean execute(String sql) throws SQLiteException{
		boolean result = false;
		connect();
		PreparedStatement ps = prepareStatement(sql);
		try {
			result = ps.execute();
		} catch (Exception e) {
			throw new SQLiteException(e.getMessage());
		} finally {
	        closePrepareStatement(ps);
	    }
		close();
		return result;
	}
	
	/**
	 * Releases this Statement object's database and JDBC resources immediately instead of waiting for this to happen when it is automatically closed. 
	 * @param ps \ref PreparedStatement to close.
	 * @throws SQLiteException
	 */
	public void closePrepareStatement(PreparedStatement ps) throws SQLiteException{
		if(ps != null){
            try{
                ps.close();
            } catch(Exception e){
            	throw new SQLiteException(e.getMessage());
            }
        }
	}
	
	/**
	 * Releases this ResultSet object's database and JDBC resources immediately instead of waiting for this to happen when it is automatically closed. 
	 * @param rs \ref ResultSet to close.
	 * @throws SQLiteException
	 */
	public void closeResultSet(ResultSet rs) throws SQLiteException{
		if(rs != null){
            try{
                rs.close();
            } catch(Exception e){
            	throw new SQLiteException(e.getMessage());
            }
        }
	}
	
	/**
	 * Indicates whether or not it is versioned
	 * @return boolean Returns true when db is versioned if not return false
	 * @throws SQLiteException
	 */
	public boolean isVersioned() throws SQLiteException{
		PreparedStatement ps = null;
		ResultSet rs =  null;
		
		connect();
		
		try {
			ps = prepareStatement(Querys.EXIST_VERSIONED.toString());
			rs = ps.executeQuery();
			if(rs.next()){
				return true;
			}
			
		} catch (Exception e) {
			throw new SQLiteException(e.getMessage());
		} finally {
	        closeResultSet(rs);
	        closePrepareStatement(ps);
	    }
		
		close();
		return false;
		
	}
	
	/**
	 * Get the version of the DB
	 * @return int Returns 0 when there is no version, otherwise the version returns
	 * @throws SQLiteException
	 */
	public int getVersion() throws SQLiteException{
		int version = 0;
		
		if(isVersioned()){
			connect();
			PreparedStatement ps = prepareStatement(Querys.GET_VERSION.toString());
			
			try {
				ResultSet rs = ps.executeQuery();
				if(rs.next()){
					version = rs.getInt("version");
				}
			} catch (Exception e) {
				throw new SQLiteException(e.getMessage());
			} finally {
		        closePrepareStatement(ps);
		    }
			
			close();
		}
		return version;
		
	}
	
}