package com.tlaloc_es.simple_db_connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import com.tlaloc_es.simple_db_connector.sqlite.SQLiteException;

/**
 *\brief Definicion de la clase
 */
public class DBConnector {
		String url;
		String dataBaseType;
		Connection con;

		public DBConnector(String datataBaseType, String url){
			this.dataBaseType = datataBaseType;
			this.url = url;
		}

		public void connect() throws SQLiteException{
			try {
				setConnection(DriverManager.getConnection(dataBaseType+url));
			 }catch (Exception e) {
				throw new SQLiteException(e.getMessage());
			 }
		}
		
		public PreparedStatement prepareStatement(String sql) throws SQLiteException{
			try {
				return getConection().prepareStatement(sql);
			} catch (Exception e) {
				throw new SQLiteException(e.getMessage());
			}
		}
		
		public void close() throws SQLiteException{
			try {
				getConection().close();
	        }catch (Exception e) {
	        	throw new SQLiteException(e.getMessage());
	        }
		}
		
		protected Connection getConection(){
			return con;
		}
		
		protected void setConnection (Connection con){
			this.con = con;
		}

		
}
