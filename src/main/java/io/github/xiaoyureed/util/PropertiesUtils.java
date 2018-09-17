package io.github.xiaoyureed.util;

import io.github.xiaoyureed.RedisSessionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

/**
 * @Auther: xiaoyu
 * @Date: 2018/9/13 18:06
 * @Description: Properties utils
 */
public class PropertiesUtils {

    private static final String PROTOCOL_PREFIX_CLASSPATH = "classpath:";
    private static final String PROTOCOL_PREFIX_FILE = "file:";
    private static final String SLASH = "/";

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtils.class);

    private PropertiesUtils() {}

    public static RedisSessionConfig load(String location) {
        if (location.startsWith(PROTOCOL_PREFIX_FILE)) {
            return loadWithFilePath(location.substring(PROTOCOL_PREFIX_FILE.length()));
        }
        return loadWithClasspath(asCleanPath(location));
    }

    /**
     * get config with file path
     * @param location
     * @return
     */
    private static RedisSessionConfig loadWithFilePath(String location) {
        LOGGER.debug(">>> load with file path: [{}]", location);

        Properties props = new Properties();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(new File(location));
            props.load(fileInputStream);
            return new RedisSessionConfig(props);
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            throw new RuntimeException(">>> config file not found, location: ["+location+"]", e);
        } catch (IOException e) {
            //e.printStackTrace();
            throw new RuntimeException(">>> an error occurred during load config", e);
        }finally {
            if (null != fileInputStream) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * remove prefix & first slash
     * @param location
     * @return
     */
    private static String asCleanPath(String location) {
        location = location.substring(PROTOCOL_PREFIX_CLASSPATH.length());
        if (location.startsWith(SLASH)) {
            return location.substring(1);
        }
        return location;
    }

    /**
     * get config with classpath
     * @param location
     * @return
     */
    private static RedisSessionConfig loadWithClasspath(String location) {
        LOGGER.debug(">>> load with classpath: [{}]", location);

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(location);
        Properties props = new Properties();
        try {
            props.load(inputStream);
            return new RedisSessionConfig(props);
        } catch (IOException e) {
            //e.printStackTrace();
            throw new RuntimeException(">>> sth goes wrong while loading session config with classpath ", e);
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
