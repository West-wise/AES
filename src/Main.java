
public class Main {
    public static byte[] roundKeys;
    public static void main(String[] args) {
        // GF(2^8) 곱셈 테스트

        String Key = "2b7e151628aed2a6abf7158809cf4f3c";
        String PlainText = "6bc1bee22e409f96e93d7e117393172a";
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


}