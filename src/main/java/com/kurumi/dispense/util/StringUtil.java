package com.kurumi.dispense.util;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {
	
	public static String getArrayOfElement(String str) {
		if(StringUtils.isNotBlank(str)) {
			String [] array = str.split("=");
			
			if (array.length == 2) {
				return array[1];
			} 
		}
		return null;
	}
	
	
	
	
	
	
	
	
	
}
