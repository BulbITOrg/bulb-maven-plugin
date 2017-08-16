package com.ju.bulb.maven.model;

import javax.xml.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Tests implements Serializable {
    @XmlElement(name="test")
    private List<Test> tests;

    public List<Test> getTests() {
        return tests;
    }

    public void setTests(List<Test> tests) {
        this.tests = tests;
    }

    public Tests() {
    }
}
