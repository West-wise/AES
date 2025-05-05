public class RoundEx {

    private int[][] state;
    private byte[][] key;
    private static final int[][] MIX_COLUMNS_MATRIX = {
            {2,3,1,1},
            {1,2,3,1},
            {1,1,2,3},
            {3,1,1,2}
    };
    private final GfCalc gf;
    private final Sbox sbox;
    int round = 0;

    public RoundEx(int[][] inputState, byte[][] key, int round) {
        this.state = new int[4][4];
        for(int i = 0; i<4; i++){
            System.arraycopy(inputState[i],0, this.state[i],0, 4);
        }
        this.key = key;
        this.gf = new GfCalc();
        this.round = round;
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

    // 헬퍼 함수 추가
    private int[] leftRotate(int[] row, int n) {
        int[] result = new int[4];
        for (int i = 0; i < 4; i++) {
            result[i] = row[(i + n) % 4];
        }
        return result;
    }

    private void invShiftRows(){

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
    }

    private void invMixColumns(){

    }

    public int[][] onlyAddRoundKeyStep(){
        addRoundKey();
        return state;
    }

    public int[][] everyStep(){
        shiftRows();
        mixColumns();
        addRoundKey();
        return state;
    }
}
