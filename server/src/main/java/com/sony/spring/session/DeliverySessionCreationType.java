package com.sony.spring.session;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "DeliverySession")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class DeliverySessionCreationType {
    @XmlAttribute(name = "Version")
    private String version;
    @XmlElement(name = "DeliverySessionId")
    private long id;
    @XmlElement(name = "Action")
    @XmlJavaTypeAdapter(value = ActionTypeAdapter.class)
    private ActionType action;
//    @XmlElement(name = "TMGLPool")
//    private String tmglPool;
//    @XmlElement(name = "TMGL")
//    private String tmgl;
    @XmlElement(name = "StartTime")
    private long startTime;
    @XmlElement(name = "StopTime")
    private long stopTime;

    public DeliverySessionCreationType(String version, long id, ActionType action, long startTime, long stopTime) {
        this.version = version;
        this.id = id;
        this.action = action;
        this.startTime = startTime;
        this.stopTime = stopTime;
    }

    public DeliverySessionCreationType() {

    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ActionType getAction() {
        return action;
    }

    public void setAction(ActionType action) {
        this.action = action;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStopTime() {
        return stopTime;
    }

    public void setStopTime(long stopTime) {
        this.stopTime = stopTime;
    }

    @Override
    public String toString() {
        return "DeliverySessionCreationType{" +
                "version='" + version + '\'' +
                ", id=" + id +
                ", action=" + action.getValue() +
                ", startTime=" + startTime +
                ", stopTime=" + stopTime +
                '}';
    }
}
