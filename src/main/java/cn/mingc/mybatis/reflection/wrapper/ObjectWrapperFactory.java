package cn.mingc.mybatis.reflection.wrapper;

public interface ObjectWrapperFactory {

    boolean hasWrapperFor(Object object);

    ObjectWrapper getWrapperFor(Object object);

}
