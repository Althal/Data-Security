
package podL3;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Random;
import javax.crypto.Cipher;

public class CipherRSA {
    
    private static final int keySize = 4096;
    private static final String cipherInstance = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";

    private static PublicKey publicKey;
    private static PrivateKey privateKey;

    public static void main(String[] args) throws Exception {

        // Maksymalna długość 446 bajtów
        for(int i=16; i<446/Character.BYTES; i+=16){
            System.out.println("Długość: " + i);
            cipher(generateMessage(i));
        }
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
        
        Cipher cipher = Cipher.getInstance(cipherInstance);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] ret = cipher.doFinal(message.getBytes());
        
        long stop = System.nanoTime();
        System.out.println("Czas szyfrowania: " + (stop-start) + " ns");
        
        return ret;
    }
    
    private static String decrypt(byte[] message) throws Exception {
        long start = System.nanoTime();
        
        Cipher cipher = Cipher.getInstance(cipherInstance);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        String ret = new String(cipher.doFinal(message));
        
        long stop = System.nanoTime();
        System.out.println("Czas deszyfrowania: " + (stop-start) + " ns");
        
        return ret;
    }
}
