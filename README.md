# Simple DB Connector

Simple DB Connector provides a set of classes to connect to databases in a simple way. 

# Supported databases in version 0.0.1
* SQLite with *SQLITEConnector(String fileName)*

# Examples of use
## With SQLITE

### Creating your own connector

You must create a class that extends SQLiteConnector.

```java
	public class SQLiteDB extends SQLiteConnector
```

Now you will have the following methods so that the management of the database

```java
	public void connect() throws SQLiteException
	
	public void close() throws SQLiteException

	public void onSetVersioned()

	public void onCreate()

	public void onUpgrade(int oldVersion, int newVersion)
```

#### connect()
Attempts to establish a connection to the given database URL.

#### close()

#### onCreate()
Add functionality when you create a database

#### onSetVersioned()
Add a functionality when you start a version in the database

#### onUpgrade(int oldVersion, int newVersion)
Add a functionality when you update the version