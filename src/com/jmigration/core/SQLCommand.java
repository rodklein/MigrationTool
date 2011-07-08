package com.jmigration.core;

public class SQLCommand {
	
	private StringBuilder builder = new StringBuilder();
	
	public SQLCommand append(String sqlFragment) {
		builder.append(sqlFragment);
		return this;
	}
	
	@Override
	public String toString() {
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((builder == null) ? 0 : builder.toString().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SQLCommand other = (SQLCommand) obj;
		if (builder == null) {
			if (other.builder != null)
				return false;
		} else if (!builder.toString().equals(other.builder.toString()))
			return false;
		return true;
	}
	
	

}
