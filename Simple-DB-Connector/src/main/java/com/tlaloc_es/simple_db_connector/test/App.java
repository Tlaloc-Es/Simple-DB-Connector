package com.tlaloc_es.simple_db_connector.test;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.tlaloc_es.simple_db_connector.sqlite.SQLiteException;

/**
 *\brief Definicion de la clase
 */
public class App {

	public static void main(String[] args) throws SQLException{
		
		try {
			DB sql = new DB("C:/Users/jfuentes/Desktop/a.db",6);
			String sqlQuery = "select * from 'main'.'simple-db-connector' where version = ?";
			
			sql.connect();
			PreparedStatement ps = sql.prepareStatement(sqlQuery);
			ps.setInt(1, 1);
			sql.executeQuery(ps);
			sql.close();
			
			
			
		} catch (SQLiteException e) {
			System.out.println("error: " + e.getMessage());
		}
		
	}
	

	
}

