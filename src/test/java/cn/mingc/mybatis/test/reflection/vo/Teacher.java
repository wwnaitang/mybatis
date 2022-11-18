package cn.mingc.mybatis.test.reflection.vo;

import java.util.List;
import java.util.Map;

public class Teacher {

    private String name;

    private List<String> classes;

    private Map<String, String> curriculums;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getClasses() {
        return classes;
    }

    public void setClasses(List<String> classes) {
        this.classes = classes;
    }

    public Map<String, String> getCurriculums() {
        return curriculums;
    }

    public void setCurriculums(Map<String, String> curriculums) {
        this.curriculums = curriculums;
    }
}
