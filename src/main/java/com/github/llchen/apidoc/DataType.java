package com.github.llchen.apidoc;

/**
 * 数据类型
 */
public enum DataType {

    BYTE("byte"),
    SHORT("short"),
    INT("integer"),
    LONG("long"),
    FLOAT("float"),
    DOUBLE("double"),
    CHAR("char"),
    BOOLEAN("boolean"),
    ARRAY("array"),
    OBJECT("object"),
    STRING("string"),
    MODEL("model"),
    REFERENCE("reference"),
    UNKNOW("");

    DataType(String v) {
        val = v;
    }

    private String val;

    public static String getType(Class<?> clazz){
        if (String.class.equals(clazz)){
            return STRING.getValue();
        }else if (Integer.class.equals(clazz)){
            return INT.getValue();
        }else if (Long.class.equals(clazz)){
            return LONG.getValue();
        }else{
            return clazz.getName();
        }
    }

    public String getValue() {
        return this.val;
    }
}
