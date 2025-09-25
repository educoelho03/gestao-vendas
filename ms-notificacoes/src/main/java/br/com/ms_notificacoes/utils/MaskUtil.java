package br.com.ms_notificacoes.utils;

public class MaskUtil {

    public static String maskEmail(String email){
        if (email == null || email.isEmpty()){
            return null;
        }

        int atIndex = email.indexOf("@"); // encontra a posicao do @
        if(atIndex <= 2){
            return email.charAt(0) + "***" + email.substring(atIndex);
        } else {
            return email.substring(0, 2) + "***" + email.substring(atIndex);
        }
    }

}
