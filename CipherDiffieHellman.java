
package podL4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CipherDiffieHellman {
    
    private static int[] ng = new int[2];
    private static int secretValue;
    private static String filePath = "D:\\Test\\";
    private static int userId = 1;
    
    public static void main(String[] args) throws Exception {
        secretValue = getRandomSecret();
        if(!readPqFromFile()) {
            ng = getNG(100);
            userId = 0;
            writeNgToFile();
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
        a = a.pow(secretValue).mod(new BigInteger(String.valueOf(ng[0])));
        return a.longValue();
    }
    
    private static long generateA(){
        BigInteger a = new BigInteger(String.valueOf(ng[1]));
        a = a.pow(secretValue).mod(new BigInteger(String.valueOf(ng[0])));
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
                ng[index++] = Integer.parseInt(line);
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
    
    private static void writeNgToFile(){
        try {
            PrintWriter pw = new PrintWriter(filePath + "keys.txt");
            pw.println(ng[0]);
            pw.print(ng[1]);
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
    
    private static int[] getNG(int minValue){
        int n = getPrime(minValue);
        int g = getPrimaryElement(n);
        
        while(g == -1){
            n = getPrime(n++);
            g = getPrimaryElement(n);
        }
    
        return new int[]{n,g};
    }
    
    private static int getPrime(int val){
        while(true){
            boolean isPrime = isPrime(val);
            if(isPrime){
                return val;
            }
            val++;
        }
    }
    
    private static boolean isPrime(int val){
        if(val%2 == 0) return false;
        
        for(int r=3; r<val/2; r+=2){
            if(val%r == 0){
                return false;
            }
        }
        
        return true;
    }
    
    private static int getPrimaryElement(int n){        
        ArrayList<Integer> rests;
        for(int i = 2; i<n; i++){
            rests = new ArrayList<>();
            int power = 1;
            while(true){
                int rest = new BigInteger(String.valueOf(i)).pow(power++).mod(new BigInteger(String.valueOf(n))).intValue();
                if(!rests.contains(rest)){
                    rests.add(rest);
                }
                else{
                    if(n == rests.size()+1) return i;
                    else break;
                }
            }
        }
        return -1;
    }
}
