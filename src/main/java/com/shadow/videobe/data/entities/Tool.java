package com.shadow.videobe.data.entities;

import com.shadow.videobe.data.Tooled;

import java.io.Serializable;

public class Tool implements Tooled, Serializable {
    private long id;
    private String name;
    private byte[] pic;
    private String details;

    public Tool(){

    }
    public Tool(int id,String name, byte[] pic, String details) {
        this.id = id;
        this.name=name;
        this.pic = pic;
        this.details = details;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
      this.id=id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name=name;
    }

    @Override
    public byte[] getPic() {
        return pic;
    }

    @Override
    public void setPic(byte[] pic) {
        this.pic=pic;
    }

    @Override
    public String getDetails() {
        return details;
    }

    @Override
    public void setDetails(String details) {
        this.details=details;
    }
}
