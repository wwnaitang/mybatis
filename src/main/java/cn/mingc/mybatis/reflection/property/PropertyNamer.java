package cn.mingc.mybatis.reflection.property;

import java.util.Locale;

public class PropertyNamer {

    public static final String GET_METHOD_PREFIX = "get";

    public static final String SET_METHOD_PREFIX = "set";

    public static final String IS_METHOD_PREFIX = "is";

    public static String method2Property(String name) {
        if (!isProperty(name)) {
            throw new RuntimeException(name + "不是以\"get\"、\"set\"、\"is\"开头");
        }
        String property = name;
        if (property.startsWith(SET_METHOD_PREFIX) || property.startsWith(GET_METHOD_PREFIX)) {
            property = property.substring(SET_METHOD_PREFIX.length());
        } else if (property.startsWith(IS_METHOD_PREFIX)) {
            property = property.substring(IS_METHOD_PREFIX.length());
        } else {
            throw new RuntimeException(property + "不是以\"get\"、\"set\"、\"is\"开头");
        }

        if (property.length() == 1 || !Character.isUpperCase(property.charAt(1))) {
            property = property.substring(0, 1).toLowerCase(Locale.ENGLISH) + property.substring(1);
        }
        return property;
    }

    public static boolean isProperty(String name) {
        return isSetter(name) || isGetter(name);
    }

    public static boolean isSetter(String name) {
        return name.startsWith(SET_METHOD_PREFIX) && name.length() > SET_METHOD_PREFIX.length();
    }

    public static boolean isGetter(String name) {
        return (name.startsWith(GET_METHOD_PREFIX) && name.length() > GET_METHOD_PREFIX.length())
                || (name.startsWith(IS_METHOD_PREFIX) && name.length() > IS_METHOD_PREFIX.length());
    }

}
