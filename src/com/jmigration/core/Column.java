package com.jmigration.core;

import com.jmigration.MigrationSession;

@SuppressWarnings("unchecked")
public class Column<T extends Column<T>> {
	
	public static final int VARCHAR99 = 99;
	
	final String columnName;
	int type = -1;
	int lenght = -1;
	int precision = -1;
	boolean notNull;
	boolean nullable;
	private String defaultValue;

	public Column(String name) {
		this.columnName = name;
	}
	
	public T as(int type) {
		this.type = type;
		return (T) this;
	}

	public T notNull() {
		notNull = true;
		return (T) this;
	}
	
	public T nullable() {
		nullable = true;
		return (T) this;
	}

	public T size(int lenght) {
		this.lenght = lenght;
		return (T) this;
	}

	public T size(int lenght, int precision) {
		this.lenght = lenght;
		this.precision = precision;
		return (T) this;
	}
	
	public T defaultValue(String value) {
		this.defaultValue = value;
		return (T) this;
	}

	public void parse(MigrationSession session, SQLCommand sqlCommand) {
		sqlCommand.append(columnName);
		if (type > -1) {
			sqlCommand.append(" ").append(session.getDialect().getType(type));
		}
		if (lenght > -1) {
			sqlCommand.append("(").append(String.valueOf(lenght));
			if (precision > 0) {
				sqlCommand.append(",").append(String.valueOf(precision));
			}
			sqlCommand.append(")");
		}
		if (defaultValue != null) {
			sqlCommand.append(" default ").append(defaultValue);
		}
		if (notNull) {
			sqlCommand.append(" not null");
		} else if (nullable) {
			sqlCommand.append(" null");
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((columnName == null) ? 0 : columnName.hashCode());
		result = prime * result + lenght;
		result = prime * result + (notNull ? 1231 : 1237);
		result = prime * result + precision;
		result = prime * result + type;
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
		Column<?> other = (Column<?>) obj;
		if (columnName == null) {
			if (other.columnName != null)
				return false;
		} else if (!columnName.equals(other.columnName))
			return false;
		if (lenght != other.lenght)
			return false;
		if (notNull != other.notNull)
			return false;
		if (precision != other.precision)
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	public String getColumnName() {
		return columnName;
	}

	public boolean hasType() {
		return type > -1;
	}

	public int getType() {
		return type;
	}

	public boolean hasLength() {
		return lenght > -1;
	}

	public int getLenght() {
		return lenght;
	}

	public boolean hasPrecision() {
		return precision > -1;
	}

	public int getPrecision() {
		return precision;
	}

	public boolean isNotNull() {
		return notNull;
	}

	public boolean isNullable() {
		return nullable;
	}
	
	public String getDefaultValue() {
		return defaultValue;
	}
}
