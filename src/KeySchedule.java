public class KeySchedule {

    // AES의 키 스케줄링을 위한 상수
    private static final int Nk = 4; // 키 길이 (32비트 워드 개수)
    private static final int Nb = 4; // 블록 길이 (32비트 워드 개수)
    private static final int Nr = 10; // 라운드 수 (128비트 키 기준)
    private static final int[] RCON = {
            0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80,
            0x1B, 0x36
    };

    private final Sbox sbox;

    public KeySchedule(String key) {
        this.sbox = new Sbox();
    }
    public byte[] genKey(String Key) {
        byte[] key = hexToByte(Key);
        // AES-128: 11개 라운드 키, 각 16바이트 = 총 176바이트
        byte[] expandedKey = new byte[4 * Nb * (Nr + 1)]; // 4 * 4 * 11 = 176
        System.arraycopy(key, 0, expandedKey, 0, 4 * Nk);
        byte[] temp = new byte[4];
        for (int i = 4; i < 44; i++) {
            System.arraycopy(expandedKey, 4 * (i - 1), temp, 0, 4);
            if (i % 4 == 0) {
                // 4의 배수 위치: RotWord -> SubWord -> Rcon XOR
                temp = rotWord(temp);
                temp = subWord(temp);
                temp[0] ^= (byte) RCON[i / 4 - 1]; // Rcon은 첫 바이트에만 적용
            }
            // 4개 전 워드와 XOR
            for (int j = 0; j < 4; j++) {
                expandedKey[4 * i + j] = (byte) (expandedKey[4 * (i - 4) + j] ^ temp[j]);
            }
        }
        printKey(expandedKey);
        return expandedKey;
    }


    public byte[] rotWord(byte[] word){
        return new byte[] {word[1],word[2],word[3],word[0]};
    }

    public byte[] subWord(byte[] word){
        byte[] result = new byte[4];
        for(int i = 0; i<4; i++){
            result[i] = (byte) sbox.subBytes(word[i] & 0xFF);
        }
        return result;
    }

    private byte[] hexToByte(String hexKey){
        int len = hexKey.length();
        byte[] data = new byte[len/2];
        for(int i = 0; i < len; i+= 2){
            data[i/2] = (byte) ((Character.digit(hexKey.charAt(i), 16) << 4)
            + Character.digit(hexKey.charAt(i+1), 16));
        }
        return data;
    }

    public int[][] convertState(String input){
        int[][] state = new int[4][4];
        byte[] inputBytes = hexToByte(input);
        for(int col = 0; col < 4; col++){
            for(int row = 0; row < 4; row++){
                state[row][col] = inputBytes[col * 4 + row] & 0xFF;
            }
        }
        return state;
    }

    public void printKey(byte[] key){
        System.out.println("------------Round Keys------------");
        StringBuilder sb = new StringBuilder();
        for(int round = 0; round < 11; round++){
            sb.append(String.format("Round %2d key: ", round+1));
            for(int i = 0; i<16; i++){
                sb.append(String.format("%02X ", key[round * 16 + i]  & 0xFF));
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }

    public byte[][] convertRoundKey(byte[] roundKey, int round){
        byte[][] nexKey = new byte[4][4];
        int start = round * 16;
        for(int col = 0; col <4; col++){
            for(int row = 0; row < 4; row++){
                nexKey[row][col] = roundKey[start + (col * 4 + row)];
            }
        }
        return nexKey;
    }
}
