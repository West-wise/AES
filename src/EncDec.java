public class EncDec {
    private RoundEx roundEx;
    private String key;
    private int[][] state;
    private byte[] roundKey;
    private KeySchedule ks;
    private UtilFunc utilFunc = new UtilFunc();

    public EncDec(String key, int[][] state, byte[] roundKey) {
        this.roundKey = roundKey;
        this.state = new int[4][4];
        for(int i = 0; i < 4; i++) {
            System.arraycopy(state[i], 0, this.state[i], 0, 4);
        }
        this.ks = new KeySchedule(this.key);
    }

    public void encrypt() {
        for(int round = 0; round< 11; round++){
            byte[][] eachRoundKey = ks.convertRoundKey(roundKey,round);
            roundEx = new RoundEx(state, eachRoundKey, round);
            if(round == 0){
                roundEx.firstRound();
            } else if(round < 10){
                roundEx.middleRound();
            } else {
                roundEx.lastRound();
                utilFunc.printCiphertext(roundEx.getState());
            }
            state = roundEx.getState();
        }
    }

    public void decrypt(String cipherText) {

    }
}
