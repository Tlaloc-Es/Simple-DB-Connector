package com.tlaloc_es.simple_db_connector.test;

import com.tlaloc_es.simple_db_connector.sqlite.SQLITEConnector;
import com.tlaloc_es.simple_db_connector.sqlite.SQLiteException;

/**
 *\brief Definicion de la clase
 */
public class DB extends SQLITEConnector{

		public DB(String fileName) throws SQLiteException {
			super(fileName);
		}
		
		public DB(String fileName, int version) throws SQLiteException {
			super(fileName, version);
		}

		@Override
		public void onSetVersioned() {
			System.out.println("Version Iniciada");
		}
		
		

		@Override
		public void onCreate() {
			String sqlSetVersioned = "CREATE TABLE 'main'.'simple-db-connector2'('version' INTEGER);";
			 
			try {
				execute(sqlSetVersioned);
			} catch (SQLiteException e) {
				System.out.println(e.getMessage());
			}

		}

		@Override
		public void onUpgrade(int oldVersion, int newVersion) {
			System.out.println("Version actualizada: " +  oldVersion + " a " + newVersion);		
		}
	
}
