package com.tlaloc_es.simple_db_connector.exceptions;
/**
 * Exception for database versioning
 */
public class VersionException extends RuntimeException{
	public VersionException(String msg){
		super(msg);
	}
}
