package com.tlaloc_es.simple_db_connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import com.tlaloc_es.simple_db_connector.sqlite.exceptions.SQLiteException;

/**
 * Generic class for DB connector
 */
public class DBConnector {
		String url; 			/**< Database URL */
		String dataBaseType;	/**< Type of database */
		Connection con;			/**< DB connection */

		/**
		 * 
		 * @param datataBaseType Tipe of database
		 * @param url Database url
		 */
		public DBConnector(String datataBaseType, String url){
			this.dataBaseType = datataBaseType;
			this.url = url;
		}

		/**
		 * Attempts to establish a connection to the given database URL.		 * 
		 * @throws SQLiteException
		 */
		public void connect() throws SQLiteException{
			try {
				setConnection(DriverManager.getConnection(dataBaseType+url));
			 }catch (Exception e) {
				throw new SQLiteException(e.getMessage());
			 }
		}
		
		/**
		 * Creates a \ref PreparedStatement object for sending parameterized SQL statements to the database.
		 * @param sql SQL query
		 * @return A new default PreparedStatement object containing the pre-compiled SQL statement
		 * @throws SQLiteException
		 */
		public PreparedStatement prepareStatement(String sql) throws SQLiteException{
			try {
				return getConection().prepareStatement(sql);
			} catch (Exception e) {
				throw new SQLiteException(e.getMessage());
			}
		}
		
		/**
		 * Releases this Connection object's database and JDBC resources immediately instead of waiting for them to be automatically released. 
		 * @throws SQLiteException
		 */
		public void close() throws SQLiteException{
			try {
				getConection().close();
	        }catch (Exception e) {
	        	throw new SQLiteException(e.getMessage());
	        }
		}
		
		/**
		 * Return a DB connection.
		 * @return Connection A DB \ref connection 
		 */
		protected Connection getConection(){
			return con;
		}
		
		/**
		 * Set a Connection for a DB
		 * @param con \ref Connection a DB connection 
		 */
		protected void setConnection (Connection con){
			this.con = con;
		}

		
}
