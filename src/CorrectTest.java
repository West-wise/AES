import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class CorrectTest {
    private String key;
    private String plainText;
    private UtilFunc utilFunc;

    public CorrectTest(String key, String plainText) {
        this.key = key;
        this.plainText = plainText;
        utilFunc = new UtilFunc();
    }

    public void correctTest() throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        // HEX 문자열 → 바이트 배열 변환
        byte[] keyBytes = utilFunc.hexStringToByteArray(key);
        byte[] plainBytes = utilFunc.hexStringToByteArray(plainText);

        // 키 객체 생성
        SecretKeySpec keyClass = new SecretKeySpec(keyBytes, "AES");

        // Cipher 인스턴스 생성 (AES/ECB/NoPadding 사용)
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding"); // PKCS5Padding 아님!

        // 암호화 수행
        cipher.init(Cipher.ENCRYPT_MODE, keyClass);
        byte[] encrypted = cipher.doFinal(plainBytes);

        // 결과 출력 (Hex로 출력)
        String text = utilFunc.bytesToHex(encrypted);
        System.out.println("비교 \t: " + text);
    }
}
