package util;

import org.apache.catalina.realm.RealmBase;

import java.io.UnsupportedEncodingException;

public class ContactUtils {
    public static final String IMG_NOTHING = "nothing";
    public static final String IMG_UPDATE = "update";
    public static final String IMG_DELETE = "delete";

    public static final int GENDER_ANY = 0;
    public static final int GENDER_MAN = 1;
    public static final int GENDER_WOMAN = 2;

    public static final String DEFAULT_MAN_AVATAR = "/sysImages/default.png";
    public static final String DEFAULT_WOMAN_AVATAR = "/sysImages/girl.png";

    public static synchronized String getUTF8String(String target) throws UnsupportedEncodingException {
        String result = null;
        if (target != null)
            result = new String(target.getBytes("iso-8859-1"), "UTF-8");
        return result;
    }

    public static String cyr2lat(char ch){
        char upperCh = Character.toUpperCase(ch);
        switch (upperCh){
            case 'А': return "A";
            case 'Б': return "B";
            case 'В': return "V";
            case 'Г': return "G";
            case 'Д': return "D";
            case 'Е': return "E";
            case 'Ё': return "JE";
            case 'Ж': return "ZH";
            case 'З': return "Z";
            case 'И': return "I";
            case 'Й': return "Y";
            case 'К': return "K";
            case 'Л': return "L";
            case 'М': return "M";
            case 'Н': return "N";
            case 'О': return "O";
            case 'П': return "P";
            case 'Р': return "R";
            case 'С': return "S";
            case 'Т': return "T";
            case 'У': return "U";
            case 'Ф': return "F";
            case 'Х': return "KH";
            case 'Ц': return "C";
            case 'Ч': return "CH";
            case 'Ш': return "SH";
            case 'Щ': return "JSH";
            case 'Ъ': return "HH";
            case 'Ы': return "IH";
            case 'Ь': return "JH";
            case 'Э': return "EH";
            case 'Ю': return "JU";
            case 'Я': return "JA";
            default: return String.valueOf(upperCh);
        }
    }

    public static String cyr2lat(String s){
        StringBuilder sb = new StringBuilder(s.length()*2);
        for(char ch: s.toCharArray()){
            sb.append(cyr2lat(ch));
        }
        return sb.toString();
    }

    public static String getSHA256HEX(String str) {
        return RealmBase.Digest(str, "SHA-256", "UTF-8");
    }
}
