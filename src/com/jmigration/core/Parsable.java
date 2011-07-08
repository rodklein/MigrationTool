package com.jmigration.core;

import com.jmigration.MigrationSession;


public interface Parsable {
	
	void parse(MigrationSession session, SQLCommand sqlCommand);

}
