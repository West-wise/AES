import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Main {
    public static byte[] roundKeys;
    public static void main(String[] args) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        // GF(2^8) 곱셈 테스트

        String Key = "";
        KeySchedule ks = new KeySchedule(Key);
        roundKeys = ks.genKey(Key);
        int[][] state = ks.convertState(PlainText);
        for(int round = 0; round< 11; round++){
            byte[][] eachRoundKey = ks.convertRoundKey(roundKeys,round);
            RoundEx roundEx = new RoundEx(state, eachRoundKey, round);
            if(round == 0){
                roundEx.firstRound();
            } else if(round < 10){
                roundEx.middleRound();
            } else {
                roundEx.lastRound();
                printCiphertext(roundEx.getState());
            }
            state = roundEx.getState();
        }

        correctTest();
    }
    private static void printCiphertext(int[][] state) {
        System.out.print("암호문: ");
        for (int col = 0; col < 4; col++) {
            for (int row = 0; row < 4; row++) {
                System.out.printf("%02X", state[row][col]);
            }
        }
        System.out.println();
    }

    private static void correctTest() throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        // 입력값
        String keyHex = "2b7e151628aed2a6abf7158809cf4f3c";
        String plainHex = "6bc1bee22e409f96e93d7e117393172a";

        // HEX 문자열 → 바이트 배열 변환
        byte[] keyBytes = hexStringToByteArray(keyHex);
        byte[] plainBytes = hexStringToByteArray(plainHex);

        // 키 객체 생성
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

        // Cipher 인스턴스 생성 (AES/ECB/NoPadding 사용)
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding"); // PKCS5Padding 아님!

        // 암호화 수행
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(plainBytes);

        // 결과 출력 (Hex로 출력)
        System.out.println("암호문: " + bytesToHex(encrypted));

    }
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}