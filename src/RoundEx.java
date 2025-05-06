public class RoundEx {

    private int[][] state;
    private byte[][] key;
    private static final int[][] MIX_COLUMNS_MATRIX = {
            {2,3,1,1},
            {1,2,3,1},
            {1,1,2,3},
            {3,1,1,2}
    };
    private static final int[][] INV_COLUMNS_MATRIX = {
            {0x0E, 0x0B, 0x0D, 0x09},
            {0x09, 0x0E, 0x0B, 0x0D},
            {0x0D, 0x09, 0x0E, 0x0B},
            {0x0B, 0x0D, 0x09, 0x0E}
    };
    private final GfCalc gf;
    private final Sbox sbox;

    public RoundEx(int[][] inputState, byte[][] key) {
        this.state = new int[4][4];
        for(int i = 0; i<4; i++){
            System.arraycopy(inputState[i],0, this.state[i],0, 4);
        }
        this.key = key;
        this.gf = new GfCalc();
        this.sbox = new Sbox();
    }

    private void addRoundKey(){
        for(int col = 0; col < 4; col++){
            for(int row = 0; row < 4; row++){
                state[row][col] ^= key[row][col] & 0xFF;
            }
        }
    }

    // 각 행을 left rotate
    private void shiftRows() {
        state[1] = leftRotate(state[1], 1);
        state[2] = leftRotate(state[2], 2);
        state[3] = leftRotate(state[3], 3);
    }
    private void invShiftRows(){
        state[1] = rightRotate(state[1], 1);
        state[2] = rightRotate(state[2], 2);
        state[3] = rightRotate(state[3], 3);
    }

    // 헬퍼 함수 추가
    private int[] leftRotate(int[] row, int n) {
        int[] result = new int[4];
        for (int i = 0; i < 4; i++) {
            result[i] = row[(i + n) % 4];
        }
        return result;
    }
    private int[] rightRotate(int[] row, int n){
        int[] result = new int[4];
        for(int i = 0; i<4; i++){
            result[i] = row[(i - n + 4) % 4];
        }
        return result;
    }

    private void mixColumns(){
        int[][] temp = new int[4][4];
        for(int col = 0; col < 4; col++){
            for(int row = 0; row < 4; row++){
                temp[row][col] = 0;
                for(int i = 0; i<4; i++){
                    temp[row][col] ^= gf.multiply(MIX_COLUMNS_MATRIX[row][i], state[i][col]);
                }
            }
        }
        state = temp;
    }
    private void invMixColumns(){
        int[][] temp = new int[4][4];
        for(int col = 0; col < 4; col++){
            for(int row = 0; row < 4; row++){
                temp[row][col] = 0;
                for(int i = 0; i<4; i++){
                    temp[row][col] ^= gf.multiply(INV_COLUMNS_MATRIX[row][i], state[i][col]);
                }
            }
        }
        state=temp;
    }

    private void subBytesInRound() {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                state[row][col] = sbox.subBytes(state[row][col] & 0xFF);
            }
        }
    }
    private void invSubBytesInRound() {
        for (int row = 0; row < 4; row++) {
            for(int col = 0; col < 4; col++){
                state[row][col] = sbox.invSubBytes(state[row][col] & 0xFF);
            }
        }
    }


    public void firstRound(){
        addRoundKey();
    }

    public void middleRound(){
        subBytesInRound();
        shiftRows();
        mixColumns();
        addRoundKey();
    }

    public void lastRound(){
        subBytesInRound();
        shiftRows();
        addRoundKey();
    }
    public int[][] getState() {
        return state;
    }
    public void invMiddleRound(){
        invShiftRows();
        invSubBytesInRound();
        addRoundKey();
        invMixColumns();
    }
    public void invLastRound(){
        invShiftRows();
        invSubBytesInRound();
        addRoundKey();
    }
}
