public class Main {
    public static byte[] roundKeys;
    public static void main(String[] args) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        // GF(2^8) 곱셈 테스트

        String Key = "";
        KeySchedule ks = new KeySchedule(Key);
        byte[] expandedKey = ks.genKey(Key);
    }
}