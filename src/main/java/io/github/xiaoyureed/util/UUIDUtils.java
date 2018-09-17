package io.github.xiaoyureed.util;

import java.util.UUID;

/**
 * @Auther: xiaoyu
 * @Date: 2018/9/13 14:15
 * @Description: UUID utils
 */
public class UUIDUtils {

    private static final char[] _UU64 = "-0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final char[] _UU32 = "0123456789abcdefghijklmnopqrstuv".toCharArray();


    private UUIDUtils() {}

    /**
     * generate random uuid, length： 32
     * @return
     */
    public static String UU32() {
        return UU32(UUID.randomUUID());
    }

    /**
     * generate uuid string (length: 32) order by specific UUID object
     * @param uu
     * @return
     */
    public static String UU32(UUID uu) {
        StringBuilder sb = new StringBuilder();
        long m = uu.getMostSignificantBits();
        long l = uu.getLeastSignificantBits();
        for (int i = 0; i < 13; i++) {
            sb.append(_UU32[(int) (m >> ((13 - i - 1) * 5)) & 31]);
        }
        for (int i = 0; i < 13; i++) {
            sb.append(_UU32[(int) (l >> ((13 - i - 1)) * 5) & 31]);
        }
        return sb.toString();

    }
}
