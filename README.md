# Simple DB Connector

Simple DB Connector provides a set of classes to connect to databases in a simple way. 

# Supported databases in version 0.0.1
* SQLite with *SQLITEConnector(String fileName)*

# Examples of use
## With SQLITE

### Connect to a database that does not exist

```java
	SQLITEConnector sql = new SQLITEConnector("/data/a.db");
  sql.connect();
```

### Connect to an existing database
```java
	SQLITEConnector sql = new SQLITEConnector("/data/a.db");
  sql.connect();
```