package com.kurumi.dispense.util;

import java.math.BigInteger;


public class SuidToHash {
		
	  private final String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	  private static String toHash(String s) throws Exception
      {
          byte[] bs = s.getBytes("utf-8");
          long hash31 = 0, hash131 = 0;
          for (int i = 0; i < bs.length; ++i)
          {
              hash31 = hash31 * 31 + bs[i];
              hash131 = hash131 * 131 + bs[i];
          }
		  long  uc = (hash131 >> 23) & 0x1FF;
          hash31 <<= 9;
          hash31 &= 0x1FFFFFFFFFFL;
          uc |= hash31;
          //10 ת36����
          String hashStr = new BigInteger(uc+"", 10).toString(36);
        
          while (hashStr.length() < 8) {
        	  hashStr="0"+hashStr;
          }
          return hashStr;
      }
	 
	  public static String getHash(String str)
      {
          try
          {
              String strHash = SuidToHash.toHash(str);
              if (strHash.length() < 8)
              {
                  for (int inx = strHash.length(); inx < 8; inx++)
                  {
                      strHash = "0" + strHash;
                  }
              }
              int i = 1;
              StringBuilder sb = new StringBuilder();
              for(char strHashChar:strHash.toCharArray()) {
            	  sb.append(strHashChar);
                  if (i % 2 == 0 && i < 8)
                  {
                      sb.append('/');
                  }
                  i++;
              }
              
              return sb.toString();
          }
          catch (Exception ex)
          {
        	  System.out.println(ex.getStackTrace());
              return null;
          }
      }
	 

//	 public static void main(String[] args) {
//		 try {
//			 //jjdkfjskdjfksaa
//			//System.out.println(getHash("jjdkfjskdjfksaa"));
//			System.out.println(toHash("1.2.840.113619.2.55.3.2831208458.218.1288344040.596.1"));
//			System.out.println(getHash("1.2.840.113619.2.55.3.2831208458.218.1288344040.596.1"));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	 
	 
	 
	 
	 

}




