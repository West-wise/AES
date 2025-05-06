
public class Sbox {
    private static final int SBOX_Y = 0x63;
    private static final int[][] MATRIX_X = {
            {1, 0, 0, 0, 1, 1, 1, 1},
            {1, 1, 0, 0, 0, 1, 1, 1},
            {1, 1, 1, 0, 0, 0, 1, 1},
            {1, 1, 1, 1, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 0, 0, 0},
            {0, 1, 1, 1, 1, 1, 0, 0},
            {0, 0, 1, 1, 1, 1, 1, 0},
            {0, 0, 0, 1, 1, 1, 1, 1}
    };
    private static final int[][] MATRIX_X_INV = {
            {0, 0, 1, 0, 0, 1, 0, 1},
            {1, 0, 0, 1, 0, 0, 1, 0},
            {0, 1, 0, 0, 1, 0, 0, 1},
            {1, 0, 1, 0, 0, 1, 0, 0},
            {0, 1, 0, 1, 0, 0, 1, 0},
            {0, 0, 1, 0, 1, 0, 0, 1},
            {1, 0, 0, 1, 0, 1, 0, 0},
            {0, 1, 0, 0, 1, 0, 1, 0}
    };
    private final GfCalc gf;
    public Sbox(){
        this.gf = new GfCalc();
    }

    // S-Box 생성
    public int subBytes(int value){
        int inv_value = gf.gfInverse(value);
        return matrixMultiply(byteToMatrix(inv_value)) ^ SBOX_Y;
    }

//    public int invSubBytes(int value){
//        int[] vec = byteToMatrix(value ^ SBOX_Y);
//        int preimage = matrixMultiplyInv(vec);
//        return gf.gfInverse(preimage); // Inv(S(x)) = Inv(A*x ⊕ c)
//    }
    public int invSubBytes(int value){
        int inv = matrixMultiplyInv(byteToMatrix(value ^ SBOX_Y));
        return gf.gfInverse(inv);
    }
    private int[] byteToMatrix(int value){
        int[] matrix = new int[8];
        for(int i = 0; i<8; i++){
            matrix[i] = (value >> i) & 0x01;
        }
        return matrix;
    }
    private int matrixMultiply(int[] matrix){
        int result = 0;
        for(int i = 0; i<8; i++){
            int bit = 0;
            for(int k = 0; k<8; k++){
                if(MATRIX_X[i][k] == 1 && matrix[k] == 1){
                    bit ^= 1;
                }
            }
            if(bit == 1){
                result |= (1 << i);
            }
        }
        return result;
    }

    private int matrixMultiplyInv(int[] matrix){
        int result = 0;
        for(int i = 0; i<8; i++){
            int bit = 0;
            for(int k = 0; k<8; k++){
                if(MATRIX_X_INV[i][k] == 1 && matrix[k] == 1){
                    bit ^= 1;
                }
            }
            if(bit == 1){
                result |= (1 << i);
            }
        }
        return result;
    }
}
