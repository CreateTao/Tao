package com.xwy.tao_work.mytaowork.utils;

import java.util.regex.Pattern;

/**
 * 判断输入手机号格式，邮箱格式等工具栏
 */
public class EditUtil {

    //判断邮箱格式
    public static boolean Exame_mail(String text){
        return text.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    }

    //判断手机格式
    public static boolean ExamPhone(String text){
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        if(text.length()==6 ||  text.length() == 11 || pattern.matcher(text).matches()){
            return true;
        }
        return false;
    }
}
