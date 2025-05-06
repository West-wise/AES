import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Main {
    public static byte[] roundKeys;
    public static void main(String[] args) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        // GF(2^8) 곱셈 테스트

        String Key = "2b7e151628aed2a6abf7158809cf4f3c";
        String[] plainTexts = {"6bc1bee22e409f96e93d7e117393172a", "ae2d8a571e03ac9c9eb76fac45af8e51", "30c81c46a35ce411e5fbc1191a0a52ef"};
        KeySchedule ks = new KeySchedule(Key);
        roundKeys = ks.genKey(Key);
        for(String pt : plainTexts){
            System.out.println("평문 \t: " + pt);
            CorrectTest ct = new CorrectTest(Key, pt);
            EncDec ed = new EncDec(Key, roundKeys);
            String cipherText = ed.encrypt(pt);
            ct.correctTest();

            ed.decrypt(cipherText);
            System.out.println();
        }
    }


}