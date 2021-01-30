package com.itrip.text;

import com.itrip.entity.People;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextFreemacket_1 {
    public static void main(String[] args) throws Exception {
        Map map=new HashMap();
        List list=new ArrayList();
        for (int i=0;i<=10;i++){
            People people=new People("name"+i,i);
            list.add(people);
        }
        map.put("l",list);
        Configuration
                configuration=new Configuration();
        configuration.setDefaultEncoding("utf-8");
        configuration.setDirectoryForTemplateLoading(
                new File("F:\\idea GZKJ\\ItriProject\\Itriauth\\src\\main\\resources"));
        Template
                template=configuration.getTemplate("2.flt");
        template.process(map,new FileWriter("e:\\2.html"));
    }
}
