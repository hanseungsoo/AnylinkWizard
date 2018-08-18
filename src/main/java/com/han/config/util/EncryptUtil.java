package com.han.config.util;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class EncryptUtil {
  private static SecretKey secretKey;
  private static Key key = null;
  
  static
  {
    if (key == null) {
      try
      {
        byte[] salt = { 65, 11, -16, -101, -68, 14, -55, 74, -75, -50, 11, -22, 5, -17, 82, 49, -41, -20, 46, 117, -61, 29, 62, 97 };
        
        DESedeKeySpec keySpec = new DESedeKeySpec(salt);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        
        secretKey = keyFactory.generateSecret(keySpec);
      }
      catch (NoSuchAlgorithmException e)
      {
        e.printStackTrace();
      }
      catch (GeneralSecurityException e)
      {
        e.printStackTrace();
      }
    }
  }
  
  public String encode(String inStr)
  {
    StringBuffer sb = null;
    try
    {
      Cipher cipher = Cipher.getInstance("TripleDES/ECB/PKCS5Padding");
      cipher.init(1, secretKey);
      byte[] plaintext = inStr.getBytes("UTF8");
      byte[] ciphertext = cipher.doFinal(plaintext);
      
      sb = new StringBuffer(ciphertext.length * 2);
      for (int i = 0; i < ciphertext.length; i++)
      {
        String hex = "0" + Integer.toHexString(0xFF & ciphertext[i]);
        sb.append(hex.substring(hex.length() - 2));
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return sb.toString();
  }
  
  public String decode(String inStr)
  {
    String text = null;
    try
    {
      byte[] b = new byte[inStr.length() / 2];
      Cipher cipher = Cipher.getInstance("TripleDES/ECB/PKCS5Padding");
      cipher.init(2, secretKey);
      for (int i = 0; i < b.length; i++) {
        b[i] = ((byte)Integer.parseInt(inStr.substring(2 * i, 2 * i + 2), 16));
      }
      byte[] decryptedText = cipher.doFinal(b);
      text = new String(decryptedText, "UTF8");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return text;
  }
}
