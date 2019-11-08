
package podL3;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class CipherAES {
    
    private static final String cipherInstance = "AES/ECB/NoPadding";
    private static final String myKey = "ABCDE";

    private static SecretKeySpec secretKey;
    private static byte[] key;

    public static void main(String[] args) throws Exception {
        
        testCharSwap();
        
        /*for(int i=32; i<382/Character.BYTES; i++){
            cipher(generateMessage(i));
        }*/
    }
    
    private static void init(){
        try {
            key = myKey.getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); 
            secretKey = new SecretKeySpec(key, "AES");
        } 
        catch (Exception e) {
            e.printStackTrace();
        } 
    }
    
    private static void testCharSwap() throws Exception {
        System.out.println("Test zamiany znaków");
        init();
        String mess = checkMessage("This is not encrypted text");
        
        String enc = encrypt(mess);
        
        int charIndex = 123;
        if(charIndex >= enc.length()) charIndex = charIndex % enc.length();
        
        byte[] encByte = enc.getBytes();
        byte b = encByte[charIndex];
        
        encByte[charIndex] = encByte[charIndex-1];
        encByte[charIndex-1] = b;
        
        String dec = decrypt(new String(encByte));
        System.out.println("Wiadomość oryginalna:   " + mess);
        System.out.println("Wiadomość po działaniu: " + dec);
        System.out.println();
    }
    
    private static void cipher(String plainText) throws Exception {
        init();        
        plainText = checkMessage(plainText);
        
        String cipher = encrypt(plainText);
        String plain = decrypt(cipher);
        
        System.out.println("Czy poprawnie rozszyfrowano: " + plain.equals(plainText));
        System.out.println();
    }
    
    private static String checkMessage(String message){
        int length = message.length();
        String ret = message;
        for(int i = 0; i<16-length%16; i++){
            ret = ret + " ";
        }
        return ret;
    }
    
    private static String generateMessage(int bytes){
        long start = System.nanoTime();
        
        Random random = new Random();
        byte[] retBytes = new byte[bytes];
        random.nextBytes(retBytes);
        String ret = new String(retBytes, Charset.forName("UTF-8"));
        
        long stop = System.nanoTime();
        System.out.println("Wiadomość: " + ret + " " + ret.length());
        System.out.println("Czas generowania wiadomości: " + (stop-start) + " ns");
        
        return ret;
    }
    
    
    
    
    private static String encrypt(String message) throws Exception {
        long start = System.nanoTime();

        Cipher cipher = Cipher.getInstance(cipherInstance);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        
        Encoder encoder = Base64.getEncoder();
        System.out.println(message.length());
        String ret = encoder.encodeToString(cipher.doFinal(message.getBytes("UTF-8")));
        
        long stop = System.nanoTime();
        System.out.println("Czas szyfrowania: " + (stop-start) + " ns");
        
        return ret;
    }
    
    private static String decrypt(String message) throws Exception {
        long start = System.nanoTime();

        Cipher cipher = Cipher.getInstance(cipherInstance);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        
        Decoder decoder = Base64.getDecoder();
        String ret = new String(cipher.doFinal(decoder.decode(message)));
        
        long stop = System.nanoTime();
        System.out.println("Czas deszyfrowania: " + (stop-start) + " ns");
        
        return ret;
    }
}
