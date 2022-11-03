package cn.mingc.mybatis.type;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TypeAliasRegistry {

    private Map<String, Class<?>> typeAliasMap = new HashMap<>();

    public TypeAliasRegistry() {
    }

    public void registerAlias(String alias, Class<?> type) {
        String key = alias.toLowerCase(Locale.ENGLISH);
        this.typeAliasMap.put(key, type);
    }

    public Class<?> resolveAlias(String alias) {
        String key = alias.toLowerCase(Locale.ENGLISH);
        return this.typeAliasMap.get(key);
    }
}
