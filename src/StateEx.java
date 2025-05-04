public class StateEx {
    private int[][] state = new int[4][4];
    public int[][] convertState(byte[] input){
        for(int col = 0; col < 4; col++){
            for(int row = 0; row < 4; row++){
                state[row][col] = input[col * 4 + row] & 0xFF;
            }
        }
        return state;
    }

}
