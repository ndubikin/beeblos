package org.beeblos.bpm.wc.taglib.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public final class MD5Hash
{
    public static String encode(byte[] passwordBytes) {
        try
        {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hashBytes = digest.digest(passwordBytes);
            String hashString = Base64.encode(hashBytes);
            return hashString.trim();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return null;        
    } 
    
    public static boolean verifyHash(byte[] passwordBytes, String passwordHash) {
        return encode(passwordBytes).equals(passwordHash);
    }
    

}
