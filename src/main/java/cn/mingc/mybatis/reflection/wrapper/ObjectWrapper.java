package cn.mingc.mybatis.reflection.wrapper;

public interface ObjectWrapper {

    Object get(String name);

    void set(String name, Object value);

//    String findProperty(String name, boolean useCamelCaseMapping);
//
//    String[] getGetterNames();
//
//    String[] getSetterNames();
//
//    Class<?> getGetterType(String name);
//
//    Class<?> getSetterType(String name);

}
