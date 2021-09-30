package com.example.seckill.utils;

import org.springframework.util.DigestUtils;

/**
 * @author zhongyikang
 * @create 2021-09-30 1:05
 */
public class MD5Utils {

    //这个salt应该是前台传的
    public static final String SALT = "asdpiudsfwerq";

    //第一次加密
    public static String passToMD5Password(String password) {
        String str = SALT.charAt(5) + SALT.charAt(7) + password + SALT.charAt(2);

        //password + salt加密
        return DigestUtils.md5DigestAsHex(str.getBytes());
    }

    //第二次加密,这里的salt从前台传来，二者保持一致。
    public static String formpassToDBpass(String password,String salt) {
        String str = salt.charAt(3) + password + salt.charAt(1);

        return DigestUtils.md5DigestAsHex(str.getBytes());
    }




    //模拟两次加密的过程
    public static String inputpassToDBpass(String password, String salt) {
        String str = passToMD5Password(password);
        String dbPass = formpassToDBpass(str, salt);
        return dbPass;

    }

    public static void main(String[] args) {
        System.out.println(inputpassToDBpass("123456", "asdfeeeqwr"));
    }

}
