package com.ju.bulb.maven.model;

import com.ju.bulb.maven.AdapterCDATA;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Test implements Serializable {
    @XmlElement
    private String key;

    @XmlElement
    @XmlJavaTypeAdapter(AdapterCDATA.class)
    private String description;

    @XmlElementWrapper(name="tickets")
    @XmlElement(name="ticket")
    private List<String> tickets;

    @XmlElementWrapper(name="suites")
    @XmlElement(name="suite")
    private List<String> suites;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTickets() {
        return tickets;
    }

    public void setTickets(List<String> tickets) {
        this.tickets = tickets;
    }

    public List<String> getSuites() {
        return suites;
    }

    public void setSuites(List<String> suites) {
        this.suites = suites;
    }

    public Test(){
    }
}
