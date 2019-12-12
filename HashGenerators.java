
package podL6;

import java.io.IOException;
import java.security.*;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Random;
import jxl.write.WriteException;
import org.bouncycastle.jcajce.provider.digest.SHA3;

public class HashGenerators {
    
    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException, IOException, WriteException {
        
        /*XLSWriter xls = new XLSWriter();
        xls.openFile();
        xls.openSheet("ABC");
        
        
        int index = 0;       
        for(int i=1024*1024; i<=50*1024*1024; i+=512*1024){
            System.out.println("Długość wiadomości: " + i/1024 + " kB");
            testAllHashs(generateMessage(i), index++, xls);
            System.out.println("");
        }
        
        xls.closeFile();*/
        initHashs(" ");
        testAllHashs("ABC");
    }
    
    private static void initHashs(String mes) throws UnsupportedEncodingException, NoSuchAlgorithmException, IOException, WriteException{
        MD5(mes).length();
        SHA1(mes).length();
        SHA224(mes).length();
        SHA256(mes).length();
        SHA384(mes).length();
        SHA512(mes).length();
        SHA3(mes).length();
    }
    
    private static void testAllHashs(String mes) throws UnsupportedEncodingException, NoSuchAlgorithmException, IOException, WriteException{
        long start, stop, length;

        start = System.nanoTime();
        String hash = MD5(mes);
        length = hash.length();
        stop = System.nanoTime();
        System.out.println("Czas MD5:     " + (stop-start)/1000 + " ms. Długość: " + length +". Skrót: " + hash);
        
        start = System.nanoTime();
        hash = SHA1(mes);
        length = hash.length();
        stop = System.nanoTime();
        System.out.println("Czas SHA-1:   " + (stop-start)/1000 + " ms. Długość: " + length +". Skrót: " + hash);
        
        start = System.nanoTime();
        hash = SHA224(mes);
        length = hash.length();
        stop = System.nanoTime();
        System.out.println("Czas SHA-224: " + (stop-start)/1000 + " ms. Długość: " + length +". Skrót: " + hash);
        
        start = System.nanoTime();
        hash = SHA256(mes);
        length = hash.length();
        stop = System.nanoTime();
        System.out.println("Czas SHA-256: " + (stop-start)/1000 + " ms. Długość: " + length +". Skrót: " + hash);
        
        start = System.nanoTime();
        hash = SHA384(mes);
        length = hash.length();
        stop = System.nanoTime();
        System.out.println("Czas SHA-384: " + (stop-start)/1000 + " ms. Długość: " + length +". Skrót: " + hash);
        
        start = System.nanoTime();
        hash = SHA512(mes);
        length = hash.length();
        stop = System.nanoTime();
        System.out.println("Czas SHA-512: " + (stop-start)/1000 + " ms. Długość: " + length +". Skrót: " + hash);
        
        start = System.nanoTime();
        hash = SHA3(mes);
        length = hash.length();
        stop = System.nanoTime();
        System.out.println("Czas SHA-3:   " + (stop-start)/1000 + " ms. Długość: " + length +". Skrót: " + hash);
    }
    
    private static void testAllHashs(String mes, int iteration, XLSWriter xls) throws UnsupportedEncodingException, NoSuchAlgorithmException, IOException, WriteException{
        long start, stop, length;
        
        xls.writeLabel(1, iteration, ((iteration*0.5)+1)+"MB");

        start = System.nanoTime();
        length = MD5(mes).length();
        stop = System.nanoTime();
        xls.writeNumber(2, iteration, (int)((stop-start)/1000));
        System.out.println("Czas MD5:     " + (stop-start)/1000 + " ms. Długość: " + length);
        
        start = System.nanoTime();
        length = SHA1(mes).length();
        stop = System.nanoTime();
        xls.writeNumber(3, iteration, (int)((stop-start)/1000));
        System.out.println("Czas SHA-1:   " + (stop-start)/1000 + " ms. Długość: " + length);
        
        start = System.nanoTime();
        length = SHA224(mes).length();
        stop = System.nanoTime();
        xls.writeNumber(4, iteration, (int)((stop-start)/1000));
        System.out.println("Czas SHA-224: " + (stop-start)/1000 + " ms. Długość: " + length);
        
        start = System.nanoTime();
        length = SHA256(mes).length();
        stop = System.nanoTime();
        xls.writeNumber(5, iteration, (int)((stop-start)/1000));
        System.out.println("Czas SHA-256: " + (stop-start)/1000 + " ms. Długość: " + length);
        
        start = System.nanoTime();
        length = SHA384(mes).length();
        stop = System.nanoTime();
        xls.writeNumber(6, iteration, (int)((stop-start)/1000));
        System.out.println("Czas SHA-384: " + (stop-start)/1000 + " ms. Długość: " + length);
        
        start = System.nanoTime();
        length = SHA512(mes).length();
        stop = System.nanoTime();
        xls.writeNumber(7, iteration, (int)((stop-start)/1000));
        System.out.println("Czas SHA-512: " + (stop-start)/1000 + " ms. Długość: " + length);
        
        start = System.nanoTime();
        length = SHA3(mes).length();
        stop = System.nanoTime();
        xls.writeNumber(8, iteration, (int)((stop-start)/1000));
        System.out.println("Czas SHA-3:   " + (stop-start)/1000 + " ms. Długość: " + length);
    }

    
    private static String MD5(String mes) throws UnsupportedEncodingException, NoSuchAlgorithmException{
       byte[] bytesOfMessage = mes.getBytes("UTF-8");
       MessageDigest md = MessageDigest.getInstance("MD5");
       return bytesToHex(md.digest(bytesOfMessage));
    }

    private static String SHA1(String mes) throws UnsupportedEncodingException, NoSuchAlgorithmException{
       byte[] bytesOfMessage = mes.getBytes("UTF-8");
       MessageDigest md = MessageDigest.getInstance("SHA-1");
       return bytesToHex(md.digest(bytesOfMessage));
    }
    
    private static String SHA224(String mes) throws UnsupportedEncodingException, NoSuchAlgorithmException{
       byte[] bytesOfMessage = mes.getBytes("UTF-8");
       MessageDigest md = MessageDigest.getInstance("SHA-224");
       return bytesToHex(md.digest(bytesOfMessage));
    }
    
    private static String SHA256(String mes) throws UnsupportedEncodingException, NoSuchAlgorithmException{
       byte[] bytesOfMessage = mes.getBytes("UTF-8");
       MessageDigest md = MessageDigest.getInstance("SHA-256");
       return bytesToHex(md.digest(bytesOfMessage));
    }
    
    private static String SHA384(String mes) throws UnsupportedEncodingException, NoSuchAlgorithmException{
       byte[] bytesOfMessage = mes.getBytes("UTF-8");
       MessageDigest md = MessageDigest.getInstance("SHA-384");
       return bytesToHex(md.digest(bytesOfMessage));
    }
    
    private static String SHA512(String mes) throws UnsupportedEncodingException, NoSuchAlgorithmException{
       byte[] bytesOfMessage = mes.getBytes("UTF-8");
       MessageDigest md = MessageDigest.getInstance("SHA-512");
       return bytesToHex(md.digest(bytesOfMessage));
    }
    
    private static String SHA3(String mes) throws UnsupportedEncodingException, NoSuchAlgorithmException{
       byte[] bytesOfMessage = mes.getBytes("UTF-8");
       SHA3.DigestSHA3 md = new SHA3.Digest512();
       return bytesToHex(md.digest(bytesOfMessage));
    }

    

    private static String bytesToHex(byte[] hashInBytes) {
       StringBuilder sb = new StringBuilder();
       for (byte b : hashInBytes) {
           sb.append(String.format("%02x", b));
       }
       return sb.toString().toUpperCase();
    }
    
    private static String generateMessage(int bytes){
        long start = System.nanoTime();
        
        Random random = new Random();
        byte[] retBytes = new byte[bytes];
        random.nextBytes(retBytes);
        String ret = new String(retBytes, Charset.forName("UTF-8"));
        
        long stop = System.nanoTime();
        System.out.println("Czas generowania wiadomości: " + (stop-start) + " ns");
        
        return ret;
    }
}
