package cn.mingc.mybatis.reflection.invoker;

import java.lang.reflect.Field;

public class GetFieldInvoker implements Invoker {

    private Field field;

    public GetFieldInvoker(Field field) {
        this.field = field;
    }

    @Override
    public Object invoke(Object target, Object[] args) throws ReflectiveOperationException {
        return field.get(target);
    }

    @Override
    public Class<?> getType() {
        return null;
    }
}
