package cn.mingc.mybatis.reflection.invoker;

import java.lang.reflect.Field;

public class SetFieldInvoker implements Invoker {

    private Field field;

    public SetFieldInvoker(Field field) {
        this.field = field;
    }

    @Override
    public Object invoke(Object target, Object[] args) throws ReflectiveOperationException {
        field.set(target, args[0]);
        return null;
    }

    @Override
    public Class<?> getType() {
        return null;
    }
}
