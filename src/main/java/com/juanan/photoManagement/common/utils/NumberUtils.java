package com.juanan.photoManagement.common.utils;

import java.math.BigDecimal;

public class NumberUtils {
	
	public static Number getValueOrZero(Number numero){
		return (numero==null) ? 0 : numero;
	}
	
	public static BigDecimal getValueOrZero(BigDecimal numero){
		return (numero==null) ? BigDecimal.ZERO : numero;
	}
	
	public static Number redondear(Number numero,int digitos)
	{
	      int cifras=(int) Math.pow(10,digitos);
	      return Math.rint(numero.doubleValue()*cifras)/cifras;
	}
	
	public static BigDecimal redondear(BigDecimal numero,int digitos)
	{
	      int cifras=(int) Math.pow(10,digitos);
	      return (numero==null) ? null : BigDecimal.valueOf(Math.rint(numero.doubleValue()*cifras)/cifras);
	}
	
	public static boolean esNumero(String numero) {
	    
		if (numero == null || numero.isEmpty()) {
	        return false;
	    }
	    int i = 0;
	   
	    if (numero.charAt(0) == '-') {
	        if (numero.length() > 1) {
	            i++;
	        } else {
	            return false;
	        }
	    }
	    
	    int tamano = numero.length();
	    for (; i < tamano; i++) {
	        if (!Character.isDigit(numero.charAt(i))) {
	            return false;
	        }
	    }
	    
	    return true;
	}


}
