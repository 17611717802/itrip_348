package com.itrip.text;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class TextFreemacket {
    public static void main(String[] args) throws Exception {
        Map map=new HashMap();
        map.put("a","你好Java");
        Configuration
                configuration=new Configuration();
        configuration.setDefaultEncoding("utf-8");
        configuration.setDirectoryForTemplateLoading(
                new File("F:\\idea GZKJ\\ItriProject\\Itriauth\\src\\main\\resources"));
        Template
                template=configuration.getTemplate("1.flt");
        template.process(map,new FileWriter("e:\\1.text"));
    }
}
