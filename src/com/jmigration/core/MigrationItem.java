package com.jmigration.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.jmigration.Migration;

public class MigrationItem {
	
	private List<Migration> migrations = new ArrayList<Migration>();
	private List<Migration> reverses = new ArrayList<Migration>();
	private final String migrationName;

	public MigrationItem(String migrationName) {
		this.migrationName = migrationName;
	}

	public void addReverse(Collection<Migration> reverses) {
		this.reverses.addAll(reverses);
	}

	public void addMigrations(Collection<Migration> migrations) {
		this.migrations.addAll(migrations);
	}

	public Iterator<Migration> migrationIterator() {
		return this.migrations.iterator();
	}
	
	public Iterator<Migration> reverseIterator() {
		return this.reverses.iterator();
	}

	public String getName() {
		return migrationName;
	}

}
