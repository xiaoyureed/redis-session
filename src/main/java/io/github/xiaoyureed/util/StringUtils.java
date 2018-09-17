package io.github.xiaoyureed.util;

/**
 * @Auther: xiaoyu
 * @Date: 2018/9/12 12:13
 * @Description: string utils
 */
public class StringUtils {
    private StringUtils() {}

    /**
     * is not blank ? blank is more stricter
     * @param str
     * @return
     */
    public static boolean isNotBlank(String str) {
        return (str != null) ? (str.trim() != "") : false;
    }

    /**
     * whether a number or not
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (isNotBlank(str)) {
            if (str.matches("\\d*")) return true;
        }
        return false;
    }

}
