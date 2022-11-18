package cn.mingc.mybatis.reflection.wrapper;

import java.util.Map;

public class MapWrapper extends BaseWrapper {

    private Map<String, Object> map;

    public MapWrapper(Map<String, Object> map) {
        this.map = map;
    }

    @Override
    public Object get(String name) {
        return map.get(name);
    }

    @Override
    public void set(String name, Object value) {
        map.put(name, value);
    }
}
