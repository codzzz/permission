package com.kuzan.permission.util;

import java.util.Random;

/**
 * Created by sui on 2019/3/22.
 */
public class PasswordUtil {
    private final static String[] word = {"a","b","c","d","e","f","g","h","j","k","m","n","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H","J","K","L","M","N","P","Q","R",
                                          "S","T","U","V","W","X","Y","Z"};

    private final static String[] num = {"2","3","4","5","6","7","8","9","0"};

    public static String randomPassword(){
        StringBuffer sb = new StringBuffer();
        Random random = new Random(System.currentTimeMillis());
        boolean flag = true;
        int length = random.nextInt(3)+8;
        for (int i = 0; i < length; i++){
            if (flag){
                sb.append(word[random.nextInt(word.length)]);
            }else {
                sb.append(num[random.nextInt(num.length)]);
            }
            flag = !flag;
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(randomPassword());
    }
}
