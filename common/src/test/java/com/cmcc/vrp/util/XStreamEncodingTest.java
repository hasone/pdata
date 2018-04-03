package com.cmcc.vrp.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.commons.io.Charsets;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Created by sunyiwei on 2016/7/6.
 */
public class XStreamEncodingTest {
    @Test
    public void testEncoding() throws Exception {
        XStream xStream = new XStream();
        xStream.alias("Human", Person.class);
        xStream.autodetectAnnotations(true);

        OutputStream os = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(os, Charsets.UTF_8);

        xStream.toXML(build(), writer);
        System.out.println(os.toString());
    }

    private Person build() {
        Person person = new Person();
        person.setName("sunyiwei");
        person.setAge(18); //always 18!

        return person;
    }

    private class Person {
        @XStreamAlias("Name")
        private String name;

        @XStreamAlias("Age")
        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
