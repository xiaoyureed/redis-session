package other;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Auther: xiaoyu
 * @Date: 2018/9/13 00:09
 * @Description:
 */
public class Demo {

    private static Logger LOGGER = LoggerFactory.getLogger(Demo.class);

    public static void main(String[] args) {
        throw new RuntimeException(""); // program exit
        //LOGGER.error("error"); // program run well
        //System.out.println("after error");
    }
}
