package org.beeblos.bpm.tm.model;

/*
 * Softpoint - Beeblos BPM
 *
 * Copyright (c) 2013, Softpoint Iberica SL
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Softpoint Iberica SL.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 *
 */

import java.io.Serializable;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.function.SQLFunctionRegistry;
import java.sql.Types;

/**
 * A column of a relational database table
 * @author Gavin King
 */
public class Column implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int DEFAULT_LENGTH = 255;
	public static final int DEFAULT_PRECISION = 19;
	public static final int DEFAULT_SCALE = 2;

	private int length=DEFAULT_LENGTH;
	private int precision=DEFAULT_PRECISION;
	private int scale=DEFAULT_SCALE;

	private String name;
	private boolean nullable=true;
	private boolean unique=false;
	private String sqlType;
	private Integer sqlTypeCode;
	private boolean quoted=false;
	int uniqueInteger;
	private String checkConstraint;
	private String comment;
	private String defaultValue;

	public Column() { };

	public Column(String columnName) {
		setName(columnName);
	}

	public Column(int length, String name, boolean nullable, boolean unique,
			String sqlType, Integer sqlTypeCode, String comment,
			String defaultValue) {
		setLength(length);
		setName(name);
		setNullable(nullable);
		setUnique(unique);
		setSqlType(sqlType);
		setSqlTypeCode(sqlTypeCode);
		setComment(comment);
		setDefaultValue(defaultValue);
		
		
	}

	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	/** returns quoted name as it would be in the mapping file. */
	public String getQuotedName() {
		return quoted ?
				"`" + name + "`" :
				name;
	}

	public String getQuotedName(Dialect d) {
		return quoted ?
			d.openQuote() + name + d.closeQuote() :
			name;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable=nullable;
	}

	/**
	 * Returns the underlying columns sqltypecode.
	 * If null, it is because the sqltype code is unknown.
	 * 
	 * Use #getSqlTypeCode(Mapping) to retreive the sqltypecode used
	 * for the columns associated Value/Type.
	 * 
	 * @return sqltypecode if it is set, otherwise null.
	 */
	public Integer getSqlTypeCode() {
		return sqlTypeCode;
	}
	
	public void setSqlTypeCode(Integer typecode) {
		sqlTypeCode=typecode;
	}
	
	public boolean isUnique() {
		return unique;
	}


	public boolean equals(Object object) {
		return object instanceof Column && equals( (Column) object );
	}

	public boolean equals(Column column) {
		if (null == column) return false;
		if (this == column) return true;

		return isQuoted() ? 
			name.equals(column.name) :
			name.equalsIgnoreCase(column.name);
	}

	//used also for generation of FK names!
	public int hashCode() {
		return isQuoted() ? 
			name.hashCode() : 
			name.toLowerCase().hashCode();
	}

	public String getSqlType() {
		return sqlType;
	}

	public void setSqlType(String sqlType) {
		this.sqlType = sqlType;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public boolean isQuoted() {
		return quoted;
	}

	public String toString() {
		return getClass().getName() + '(' + getName() + ')';
	}

	public String getCheckConstraint() {
		return checkConstraint;
	}

	public void setCheckConstraint(String checkConstraint) {
		this.checkConstraint = checkConstraint;
	}

	public boolean hasCheckConstraint() {
		return checkConstraint!=null;
	}

	public String getTemplate(Dialect dialect, SQLFunctionRegistry functionRegistry) {
		return getQuotedName(dialect);
	}

	public boolean isFormula() {
		return false;
	}

	public String getText(Dialect d) {
		return getQuotedName(d);
	}
	public String getText() {
		return getName();
	}
	
	public int getPrecision() {
		return precision;
	}
	public void setPrecision(int scale) {
		this.precision = scale;
	}

	public int getScale() {
		return scale;
	}
	public void setScale(int scale) {
		this.scale = scale;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getCanonicalName() {
		return quoted ? name : name.toLowerCase();
	}

	/**
	 * Shallow copy, the value is not copied
	 */
	protected Object clone() {
		Column copy = new Column();
		copy.setLength( length );
		copy.setScale( scale );
		copy.setName( getQuotedName() );
		copy.setNullable( nullable );
		copy.setPrecision( precision );
		copy.setUnique( unique );
		copy.setSqlType( sqlType );
		copy.setSqlTypeCode( sqlTypeCode );
		copy.uniqueInteger = uniqueInteger; 
		copy.setCheckConstraint( checkConstraint );
		copy.setComment( comment );
		copy.setDefaultValue( defaultValue );
		return copy;
	}

}