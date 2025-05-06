public class EncDec {
    private RoundEx roundEx;
    private String key;
    private int[][] state;
    private byte[] roundKey;
    private KeySchedule ks;
    private UtilFunc utilFunc = new UtilFunc();

    public EncDec(String key, byte[] roundKey) {
        this.roundKey = roundKey;
        this.key = key;
        this.state = new int[4][4];
        this.ks = new KeySchedule(this.key);
    }

    private void createState(String input){
        this.state = ks.convertState(input);
    }


    public String encrypt(String input) {
        createState(input);
        for(int round = 0; round< 11; round++){
            byte[][] eachRoundKey = ks.convertRoundKey(roundKey,round);
            roundEx = new RoundEx(state, eachRoundKey);
            if(round == 0){
                roundEx.firstRound();
            } else if(round < 10){
                roundEx.middleRound();
            } else {
                roundEx.lastRound();
                System.out.print("암호문 \t: ");
                return utilFunc.printCiphertext(roundEx.getState());
            }
            state = roundEx.getState();
        }
        return "";
    }

    public void decrypt(String input) {
        createState(input);
        for(int round = 10; round >= 0; round--){
            byte[][] eachRoundKey = ks.convertRoundKey(roundKey,round);
            roundEx = new RoundEx(state, eachRoundKey);
            if(round == 10){
                roundEx.firstRound();
            } else if(round > 0){
                roundEx.invMiddleRound();
            } else{
                roundEx.invLastRound();
                System.out.print("복호화 \t: ");
                utilFunc.printCiphertext(roundEx.getState());
            }
            state = roundEx.getState();
        }
    }
}
