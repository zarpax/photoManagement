package com.juanan.photoManagement.common.utils;

public class StringUtils {
	public static boolean esNuloOVacio(String cadena) {
		
		return (cadena == null || cadena.isEmpty());
	}
	
	public static String sustituir(String text, Object... params) {
		StringBuffer param = new StringBuffer(10);
		String resultado = text;
		int len = params.length;
		for (int i = 0; i < len; i++) {
			// Limpio la cadena
			param.delete(0, param.length());
			
			Object p = params[i];
			resultado = resultado.replaceAll(param.append("\\{").append(i).append("}").toString(), safeRegexReplacement(String.valueOf(p)));
		}
		return resultado;
	}

	protected static String safeRegexReplacement(String replacement) {
		if (replacement == null) {
			return "";
		}

		return replacement.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\$", "\\\\\\$");
	}

	
	public static String formateaValorString(String text) {
		return (text == null) ? "" : text.trim();
	}
	
	public static String asegurarString(String text) {
		return (text == null) ? null : text.toUpperCase()
 				   						   .trim()
				   						   .replace("Á", "A")
				   						   .replace("É", "E")
				   						   .replace("Í", "I")
				   						   .replace("Ó", "O")
				   						   .replace("Ú", "U")
				  						   .replace("À", "A")
				  						   .replace("È", "E")
				   						   .replace("Ì", "I")
				   						   .replace("Ò", "O")
				  						   .replace("Ù", "U")
				   						   .replace("Ò", "O")
				  						   .replace("Ù", "U")
				   						   .replace("Â", "A")
				   						   .replace("Ê", "E")
				   						   .replace("Î", "I")
				   						   .replace("Ô", "O")
				   						   .replace("Û", "U")
				   						   .replace("Ã", "A")
				   						   .replace("Õ", "O")
				  						   .replace("Ä", "A")
				   						   .replace("Ë", "E")
				   						   .replace("Ï", "I")
				   						   .replace("Ö", "O")
				   						   .replace("Ü", "U")
				   						   .replace("Ç", "C");
	}
}
