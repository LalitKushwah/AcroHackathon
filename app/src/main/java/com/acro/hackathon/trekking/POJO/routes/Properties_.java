
package com.acro.hackathon.trekking.POJO.routes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Properties_ {

    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("timestamp")
    @Expose
    private Object timestamp;
    @SerializedName("begin")
    @Expose
    private Object begin;
    @SerializedName("end")
    @Expose
    private Object end;
    @SerializedName("altitudeMode")
    @Expose
    private Object altitudeMode;
    @SerializedName("tessellate")
    @Expose
    private Integer tessellate;
    @SerializedName("extrude")
    @Expose
    private Integer extrude;
    @SerializedName("visibility")
    @Expose
    private Integer visibility;
    @SerializedName("drawOrder")
    @Expose
    private Object drawOrder;
    @SerializedName("icon")
    @Expose
    private Object icon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    public Object getBegin() {
        return begin;
    }

    public void setBegin(Object begin) {
        this.begin = begin;
    }

    public Object getEnd() {
        return end;
    }

    public void setEnd(Object end) {
        this.end = end;
    }

    public Object getAltitudeMode() {
        return altitudeMode;
    }

    public void setAltitudeMode(Object altitudeMode) {
        this.altitudeMode = altitudeMode;
    }

    public Integer getTessellate() {
        return tessellate;
    }

    public void setTessellate(Integer tessellate) {
        this.tessellate = tessellate;
    }

    public Integer getExtrude() {
        return extrude;
    }

    public void setExtrude(Integer extrude) {
        this.extrude = extrude;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }

    public Object getDrawOrder() {
        return drawOrder;
    }

    public void setDrawOrder(Object drawOrder) {
        this.drawOrder = drawOrder;
    }

    public Object getIcon() {
        return icon;
    }

    public void setIcon(Object icon) {
        this.icon = icon;
    }

}
