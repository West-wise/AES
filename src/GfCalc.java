public class GfCalc {
    private static final int GF_MOD = 0x11B; // AES의 기약 다항식 (x^8 + x^4 + x^3 + x + 1)

    public int multiply(int a, int b) {
        int result = 0;
        for (int i = 0; i < 8; i++) {
            if ((b & 1) != 0) result ^= a;
            boolean highBit = (a & 0x80) != 0;
            a = (a << 1) & 0xFF; // 8비트로 제한
            if (highBit) a ^= 0x1B; // 0x11B의 하위 8비트 사용
            b >>= 1;
        }
        return result;
    }
    private int findDegree(int poly) {
        int degree = 0;
        while (poly > 0) {
            poly >>= 1;
            degree++;
        }
        return degree - 1;
    }

    public int gfInverse(int a) {
        if(a == 0 ){
            return 0x00;
        }
        int t0 = 0, t1 = 1;
        int r0 = GF_MOD, r1 = a & 0xFF;


        while (r1 != 0) {
            int q = gfDiv(r0, r1);
            // 나머지 계산 및 모듈러 감쇄
            int r = r0 ^ multiply(q, r1);
            r0 = r1;
            r1 = r;
            if (r1 >= 0x80) r1 ^= GF_MOD; // 8비트 초과 시 감쇄

            // 계수 업데이트
            int tNew = t0 ^ multiply(q, t1);
            t0 = t1;
            t1 = tNew;
        }
        return (t0 >= 0) ? t0 & 0xFF : (t0 + 0x100) & 0xFF;
    }

    private int gfDiv(int dividend, int divisor) {
        int quotient = 0;
        int dividendDegree = findDegree(dividend);
        int divisorDegree = findDegree(divisor);

        while (dividendDegree >= divisorDegree) {
            int shift = dividendDegree - divisorDegree;
            quotient ^= (1 << shift);
            dividend ^= (divisor << shift);
            dividendDegree = findDegree(dividend);
        }
        return quotient;
    }
}