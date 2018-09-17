package io.github.xiaoyureed;

import io.github.xiaoyureed.util.UUIDUtils;
import org.junit.Test;

/**
 * @Auther: xiaoyu
 * @Date: 2018/9/13 15:41
 * @Description: test UUIDUtils
 */
public class UUIDUtilsTest {

    @Test
    public void testUU32() {
        String uu32 = UUIDUtils.UU32();
        System.out.println(uu32);
    }
}
