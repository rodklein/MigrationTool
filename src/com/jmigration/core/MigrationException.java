package com.jmigration.core;

@SuppressWarnings("serial")
public class MigrationException extends RuntimeException {
	
	private final MigrationItem item;

	public MigrationException(MigrationItem item, Throwable cause) {
		super(cause);
		this.item = item;
	}

	public MigrationItem getItem() {
		return item;
	}
}
