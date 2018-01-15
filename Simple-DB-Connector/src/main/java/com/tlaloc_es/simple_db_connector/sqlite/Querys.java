package com.tlaloc_es.simple_db_connector.sqlite;

/**
 * This class contains the necessary queries to establish and consult the control board of Simple DB Controller.
 */

public enum Querys {
	SET_VERSIONED("CREATE TABLE 'main'.'simple-db-connector'('version' INTEGER);"),							/**< Query to create the control table */
	INSERT_VERSION("INSERT INTO 'main'.'simple-db-connector' (version) VALUES(?);"),						/**< Query to insert version */
	EXIST_VERSIONED("SELECT name FROM sqlite_master WHERE TYPE='table' AND name='simple-db-connector';"),	/**< Query to know if the database is being versioned */
	GET_VERSION("SELECT version FROM 'main'.'simple-db-connector';"),										/**< Query to get DB version */
	UPDATE_VERSION("UPDATE 'main'.'simple-db-connector' SET 'version' = ?;");								/**< Query to update DB version */

    private final String text; /**< value of the querys */

    private Querys(final String query) {
        this.text = query;
    }

    /**
     * Return the query.
     *  @return String query
     */
    public String toString() {
        return text;
    }
}

