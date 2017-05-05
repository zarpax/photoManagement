package com.juanan.photoManagement.data.pagination;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.juanan.photoManagement.data.exception.DevelopmentException;

public class QueryParameters {
	public String field = null;
	public String operator = null;
	public String type = null;
	public String value = null;
	
	static Log logger = LogFactory.getLog(QueryParameters.class);

	
	public enum Type {
		TEXT, INT, FLOAT, LONG, BOOLEAN, DATE, TEXT_NO_CASE_SENSITIVE, BIGDECIMAL, DOUBLE;
		
		public static Type parse(String value){
			for(Type t : Type.values()){
				if(t.toString().equals(value)){
					return t;
				}
			}
			
			//FIXME: Revisar
			//logger.warn(QueryParameters.class, "El tipo {0} no está contemplado, se devuelve {1}", value, Type.TEXT);
			return Type.TEXT;
		}
		
		public String toString() {		
			String type = "null";
			switch (this) {
			case TEXT:
				type = "text";
				break;
			case INT:
				type = "int";
				break;
			case FLOAT:
				type = "float";
				break;
			case LONG:
				type = "long";
				break;
			case BIGDECIMAL:
				type = "bigDecimal";
				break;
			case BOOLEAN:
				type = "boolean";
				break;
			case DATE:
				type = "date";
				break;
			case TEXT_NO_CASE_SENSITIVE:
				type = "textNoCanseSensitive";
				break;

			default:
				throw new DevelopmentException("Valor no implementado, type=" + type);
			}
			
			return type;
		}
	}
	
	public enum Operador {
		END_WITH, NOT_END_WITH, BEGIN_WITH, NOT_BEGIN_WITH, CONTAINS, NOT_CONTAINS, IS, EQUAL;
		
		public static Operador parse(String value){
			
			for(Operador op : Operador.values()){
				if(op.toString().equals(value)){
					return op;
				}
			}
			
			// FIXME: Revisar
			//logger.warn(QueryParameters.class, "El operador {0} no está contemplado, se devuelve {1}", value, Operador.EQUAL);
			return Operador.EQUAL;
		}
		
		public String toString() {		
			String operador = "null";
			switch (this) {
			case END_WITH:
				operador = "ends with";
				break;
			case NOT_END_WITH:
				operador = "not ends with";
				break;
			case BEGIN_WITH:
				operador = "begins with";
				break;
			case NOT_BEGIN_WITH:
				operador = "not begins with";
				break;
			case CONTAINS:
				operador = "contains";
				break;
			case NOT_CONTAINS:
				operador = "not contains";
				break;
			case IS:
				operador = "is";
				break;
			case EQUAL:
				operador = "=";
				break;

			default:
				throw new DevelopmentException("Operador no implementado, type=" + operador);
			}
			
			return operador;
		}
	}
	
	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setTypeEnum(Type type) {
		this.type = type.toString();
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public void setValueDate(Date value) {
		this.value = String.valueOf(value.getTime());
	}


	private String parseTextField() {
		StringBuffer buffer = new StringBuffer(100);

		if (getOperator() != null && getOperator().startsWith("not ")) {
			buffer.append(" " + getField() + " NOT LIKE ?");
		} else {
			buffer.append(" " + getField() + " LIKE ?");
		}
		
		return buffer.toString();
	}
	
	private String parseTextNoCanseSensitiveField() {
		StringBuffer buffer = new StringBuffer(100);

		buffer.append(" LOWER(" + getField() + ") LIKE ?");
		
		return buffer.toString();
	}

	private String parseNumberField() {
		StringBuffer buffer = new StringBuffer(100);
		
		buffer.append(" " + getField() + " " + getOperator() + " ?");
		
		return buffer.toString();
	}

	private String parseBooleanField() {
		return parseNumberField();
	}
	
	private String parseDateField() {
		StringBuffer buffer		= new StringBuffer(100);
		
		buffer.append(" " + getField() + " " + parsedOperator() + " ?");
		
		return buffer.toString();
	}
	
	private String parsedOperator() {
		if ("is".equals(getOperator())) {
			return "=";
		} else {
			return getOperator();
		}
	}
	
	public String toString() {
		String strRetorno = "";
		if (getValue() != null) {
			if ("text".equals(type)) {
				strRetorno = parseTextField();
			} else if("textNoCanseSensitive".equals(type)){ 
				strRetorno = parseTextNoCanseSensitiveField();
			} else if ("int".equals(type) || "float".equals(type) || "long".equals(type)) {
				strRetorno = parseNumberField();
			} else if ("boolean".equals(type)) {
				strRetorno = parseBooleanField();
			} else if ("date".equals(type)) {
				strRetorno = parseDateField();
			}
		} else {
			StringBuffer sb = new StringBuffer(" ");
		    sb.append(getField());
		    sb.append( " IS NULL");
		    strRetorno = sb.toString();		    
		}
		return strRetorno;
		
		
	}



}
