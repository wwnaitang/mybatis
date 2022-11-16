package cn.mingc.mybatis.reflection.invoker;

public interface Invoker {

    Object invoke(Object target, Object[] args) throws ReflectiveOperationException;

    Class<?> getType();

}
