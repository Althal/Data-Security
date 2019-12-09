
package block.cipher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CipherDiffieHellman {
    
    private static long[] pq = new long[2];
    private static int secretValue;
    private static String filePath = "C:\\Test\\";
    private static int userId = 1;
    
    public static void main(String[] args) throws Exception {
        secretValue = getRandomSecret();
        if(!readPqFromFile()) {
            pq = generatePQ(100);
            userId = 0;
            writePqToFile();
        }
        
        long A = generateA();
        writeToFile(A);
        long B = 0;
        
        while(true){
            try {
                B = readFromFile();
                if(B != 0 ) break;
            }
            catch(FileNotFoundException ex){}
        }
        
        long secret = generateS(B);
        System.out.println("Sekretna wartość: " + secret);
    }
    
    private static long generateS(long b){
        BigInteger a = new BigInteger(String.valueOf(b));
        a = a.pow(secretValue).mod(new BigInteger(String.valueOf(pq[1])));
        return a.longValue();
    }
    
    private static long generateA(){
        BigInteger a = new BigInteger(String.valueOf(pq[1]));
        a = a.pow(secretValue).mod(new BigInteger(String.valueOf(pq[0])));
        return a.longValue();
    }
    
    private static int getRandomSecret(){
        Random rand = new Random();
        int ret = rand.nextInt();
        ret = Math.abs(ret) % 100;
        System.out.println("Wylosowano tajną wartość: " + ret);
        return ret;
    }
    
    private static boolean readPqFromFile(){
        File file = new File(filePath + "keys.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            int index = 0;
            while((line = br.readLine()) != null){
                pq[index++] = Long.parseLong(line);
            }
            return true;
            
        } catch (FileNotFoundException ex) {
            System.out.println("Nie znaleziono pliku z kluczami");
            return false;
            
        } catch (IOException ex) {
            Logger.getLogger(CipherDiffieHellman.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    private static long readFromFile() throws FileNotFoundException, IOException{
        int user = userId == 1? 0:1;
        File file = new File(filePath + "A" + user + ".txt");
        
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        long ret = 0;
        while((line = br.readLine()) != null){
            ret = Long.valueOf(line);
        }
        System.out.println("Użytkownik " + userId + " odczytał A " + ret);
        
        return ret;
    }
    
    private static void writePqToFile(){
        try {
            PrintWriter pw = new PrintWriter(filePath + "keys.txt");
            pw.println(pq[0]);
            pw.print(pq[1]);
            pw.close();
            System.out.println("Zapisano PQ w pliku");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            Logger.getLogger(CipherDiffieHellman.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void writeToFile(long l){
        try {
            PrintWriter pw = new PrintWriter(filePath + "A" + userId + ".txt");
            pw.println(l);
            pw.close();
            System.out.println("Użytkownik " + userId + " zapisał swoje A " + l);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CipherDiffieHellman.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static long[] generatePQ(long minValue){
        long[] ret = new long[2];
        long val = minValue;
        int index = 0;
        while(true){
            boolean isPrime = true;
            for(int i=3; i<val/2; i+=2){
                if(val%i == 0){
                    isPrime=false;
                    break;
                }
            }
            if(isPrime){
                ret[index++]=val;
                if(index == 2)break;
            }
            val++;
        }
                
        return ret;
    }
}
