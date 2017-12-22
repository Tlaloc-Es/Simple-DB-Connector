package com.tlaloc_es.simple_db_connector.sqlite;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.event.EventListenerList;

import com.tlaloc_es.simple_db_connector.DBConnector;

/**
 *\brief Definicion de la clase
 */
public abstract class SQLITEConnector extends DBConnector{

	private String sqlSetVersioned = "CREATE TABLE 'main'.'simple-db-connector'('version' INTEGER);";
	private String sqlInsertVersion = "INSERT INTO 'main'.'simple-db-connector' (version) VALUES(?);";
	private String sqlExistVersioned = "SELECT name FROM sqlite_master WHERE TYPE='table' AND name='simple-db-connector';";
	private String sqlGetVersion = "SELECT version FROM 'main'.'simple-db-connector';";
	private String sqltUpdateVersion = "UPDATE 'main'.'simple-db-connector' SET 'version' = ?;";

	
	public SQLITEConnector(String fileName) throws SQLiteException {
		super("jdbc:sqlite:", fileName);
		createNewDatabase(fileName);
	}

	public SQLITEConnector(String fileName, int version) throws SQLiteException {
		super("jdbc:sqlite:", fileName);
		createNewDatabase(fileName);
		setVersion(version);
	}
	
	public abstract void onSetVersioned();
	public abstract void onCreate();
	public abstract void onUpgrade(int oldVersion, int newVersion);
	
	private void createNewDatabase(String fileName) throws SQLiteException{
		try {
			Class.forName("org.sqlite.JDBC");
			String url = "jdbc:sqlite:" + fileName;
			
			File tmpDir = new File(fileName);
			boolean exists = tmpDir.exists();
			
			
		    try (Connection connect = DriverManager.getConnection(url)) {
		    	if (connect != null) {
		    		DatabaseMetaData meta = connect.getMetaData();	
		        }
		        connect.close();
		        
		        tmpDir = new File(fileName);
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
	
	private void setVersion(int version) throws SQLiteException {
		if(isVersioned()){
			int actualVersion =getVersion();
			if(version > actualVersion){
				onUpgrade(actualVersion, version);
				updateVersion(version);
			}
		}else{
			setSimpleDBConnectorTable();
			setInitVersion(version);
			onSetVersioned();
		}
	}
	
	private void updateVersion(int version) throws SQLiteException{
		connect();
		PreparedStatement ps = prepareStatement(sqltUpdateVersion);
		
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
	
	private void setInitVersion(int version) throws SQLiteException{
		connect();
		PreparedStatement ps = prepareStatement(sqlInsertVersion);
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
	
	private void setSimpleDBConnectorTable()throws SQLiteException{
		connect();
		PreparedStatement ps = prepareStatement(sqlSetVersioned);
		try {
			ps.execute();
		} catch (Exception e) {
			throw new SQLiteException(e.getMessage());
		} finally {
	        closePrepareStatement(ps);
	    }
		close();
	}
	
	
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
	
	public void closePrepareStatement(PreparedStatement ps) throws SQLiteException{
		if(ps != null){
            try{
                ps.close();
            } catch(Exception e){
            	throw new SQLiteException(e.getMessage());
            }
        }
	}
	
	public void closeResultSet(ResultSet rs) throws SQLiteException{
		if(rs != null){
            try{
                rs.close();
            } catch(Exception e){
            	throw new SQLiteException(e.getMessage());
            }
        }
	}
	
	private boolean isVersioned() throws SQLiteException{
		PreparedStatement ps = null;
		ResultSet rs =  null;
		
		connect();
		
		try {
			ps = prepareStatement(sqlExistVersioned);
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
	
	private int getVersion() throws SQLiteException{
		int version = -1;
		
		connect();
		PreparedStatement ps = prepareStatement(sqlGetVersion);
		
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
		
		return version;
		
	}
	
}
