package cn.mingc.mybatis.test.reflection;

import cn.mingc.mybatis.reflection.MetaObject;
import cn.mingc.mybatis.reflection.SystemMetaObject;
import cn.mingc.mybatis.test.reflection.vo.School;
import cn.mingc.mybatis.test.reflection.vo.Teacher;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class ApiTest {

    @Test
    public void test_meta_object() {
        School school = newSchool();
        MetaObject metaObject = SystemMetaObject.forObject(school);
        assert metaObject.getValue("name") == "吉水中学";
        assert metaObject.getValue("buildDate").toString().equals("1965-10-23");
        assert ((int) metaObject.getValue("level")) == 1;
        assert metaObject.getValue("teachers[0]").getClass().equals(Teacher.class);
    }

    public School newSchool() {
        School school = new School();
        school.setName("吉水中学");
        school.setBuildDate(Date.valueOf("1965-10-23"));
        school.setLevel(1);

        return school;
    }

    public Teacher newTeacher() {
        Teacher teacher = new Teacher();
        teacher.setName("张三");

        List<String> list = new ArrayList<>();
        list.add("三年级1班");
        list.add("三年级3班");
        list.add("五年级2班");
        list.add("五年级3班");
        teacher.setClasses(list);

        Map<String, String> map = new HashMap<>();
        map.put("三年级1班", "语文");
        map.put("三年级3班", "数学");
        map.put("五年级2班", "英语");
        map.put("五年级3班", "英语");
        teacher.setCurriculums(map);

        return teacher;
    }
}
