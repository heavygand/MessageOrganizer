package de.ks.messageOrg.app;


public class Message {

    private String sender_name;
    private int timestamp_ms;
    private String content;
    private String type;
    
    
    
    public String getSender_name() {
    
        return this.sender_name;
    }
    
    public void setSender_name(String sender_name) {
    
        this.sender_name = sender_name;
    }
    
    public int getTimestamp_ms() {
    
        return this.timestamp_ms;
    }
    
    public void setTimestamp_ms(Integer timestamp_ms) {
    
        this.timestamp_ms = timestamp_ms;
    }
    
    public String getContent() {
    
        return this.content;
    }
    
    public void setContent(String content) {
    
        this.content = content;
    }
    
    public String getType() {
    
        return this.type;
    }
    
    public void setType(String type) {
    
        this.type = type;
    }
    
    
    
}
