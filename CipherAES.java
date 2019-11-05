
package block.cipher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class BlockCipherAES {
    
    private static final int keySize = 4096;
    private static final String cipherInstance = "AES/ECB/NoPadding";

    private static PublicKey publicKey;
    private static PrivateKey privateKey;

    public static void main(String[] args) throws Exception {
        
        //testCharSwap();
        
        // Maksymalna długość 382 bajtów
        for(int i=16; i<382-16; i+=16){
            System.out.println("Długość: " + i);
            cipher(generateMessage(i));
        }
    }
    
    private static void testCharSwap() throws Exception {
        System.out.println("Test zamiany znaków");
        init();
        String mess = "This is not encrypted text";
        
        byte[] enc = encrypt(mess);
        
        int charIndex = 123;
        if(charIndex >= enc.length) charIndex = charIndex % enc.length;
        byte b = enc[charIndex];
        
        enc[charIndex] = enc[charIndex-1];
        enc[charIndex-1] = b;
        
        String dec = decrypt(enc);
        System.out.println("Wiadomość oryginalna:   " + mess);
        System.out.println("Wiadomość po działaniu: " + dec);
        System.out.println();
    }
    
    private static void cipher(String plainText) throws Exception {
        init();
        
        byte[] cipher = encrypt(plainText);
        String plain = decrypt(cipher);
        
        System.out.println("Czy poprawnie rozszyfrowano: " + plain.equals(plainText));
        System.out.println();
    }
    
    private static String generateMessage(int bytes){
        long start = System.nanoTime();
        
        StringBuilder ret = new StringBuilder();
        Random random = new Random();
        for(int i=0; i<bytes; i++) ret.append((char)(random.nextInt()%Byte.MAX_VALUE));
        
        long stop = System.nanoTime();
        System.out.println("Czas generowania wiadomości: " + (stop-start) + " ns");
        
        return ret.toString();
    }
    
    private static void init() throws Exception {
        long start = System.currentTimeMillis();
        
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(keySize);
        
        KeyPair kp = kpg.generateKeyPair();
        publicKey = kp.getPublic();
        privateKey = kp.getPrivate();
        
        long stop = System.currentTimeMillis();
        System.out.println("Czas inicjacji kluczy: " + (stop-start)/1000.0 + " s");
    }
    
    private static byte[] encrypt(String message) throws Exception {
        long start = System.nanoTime();
        
        SecretKeySpec sks = new SecretKeySpec(publicKey.getEncoded(), "AES");
        Cipher cipher = Cipher.getInstance(cipherInstance);
        cipher.init(Cipher.ENCRYPT_MODE, sks);
        
        Encoder encoder = Base64.getEncoder();
        byte[] ret = encoder.encode(message.getBytes());
        
        long stop = System.nanoTime();
        System.out.println("Czas szyfrowania: " + (stop-start) + " ns");
        
        return ret;
    }
    
    private static String decrypt(byte[] message) throws Exception {
        long start = System.nanoTime();
        
        Decoder decoder = Base64.getDecoder();
        SecretKeySpec sks = new SecretKeySpec(publicKey.getEncoded(), "AES");
        Cipher cipher = Cipher.getInstance(cipherInstance);
        cipher.init(Cipher.DECRYPT_MODE, sks);
        String ret = new String(cipher.doFinal(decoder.decode(message)));
        
        long stop = System.nanoTime();
        System.out.println("Czas deszyfrowania: " + (stop-start) + " ns");
        
        return ret;
    }
}
