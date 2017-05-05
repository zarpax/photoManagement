package com.juanan.photoManagement.data.pagination;

public class OrderParameters {
	
	public enum Order{
		ASC, DESC;
		
		public String toString() {		
			String text = "null";
			switch (this) {
			case ASC:
				text = "Asc";
				break;
			case DESC:
				text = "Desc";
				break;

			default:
				break;
			}
			
			return text;
		}
	}
	
	public String field = null;
	// Por defecto Ascendente..
	public String operator = "Asc";

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
	
	public void setOperatorEnum(Order operator) {
		this.operator = operator.toString();
	}

	public String toString() {		
		return field + " " + operator;
	}

}
