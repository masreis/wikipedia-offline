package net.marcoreis.wikipedia.vo;

import java.util.Date;

/**
 * Value Object que representa os atributos de uma página da wikipedia
 * 
 * @author marco
 * 
 */
public class PaginaWikipedia {
    private Long id;
    private String title;
    private Date timeStamp;
    private String userName;
    private String text;
    private String model;
    private String format;
    private String comment;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void clear() {
        this.comment = null;
        this.format = null;
        this.id = null;
        this.model = null;
        this.text = null;
        this.timeStamp = null;
        this.title = null;
        this.userName = null;
    }

}
