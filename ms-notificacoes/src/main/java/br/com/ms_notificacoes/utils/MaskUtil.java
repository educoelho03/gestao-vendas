package br.com.ms_notificacoes.utils;

public class MaskUtil {

    public static String maskEmail(String email){
        if (email == null || email.isEmpty()){
            return null;
        }

        int atIndex = email.indexOf("@");
        if(atIndex <= 2){
            return email.charAt(0) + "***" + email.substring(atIndex);
        } else {
            return email.substring(0, 2) + "***" + email.substring(atIndex);
        }
    }

    public static String maskJsonEmail(String json){
        if (json == null || json.isEmpty()){
            return null;
        }

        return json.replaceAll(
                "\"email\"\\s*:\\s*\"([^\"]*)\"",
                "\"email\":\"***\""
        );
    }

}
