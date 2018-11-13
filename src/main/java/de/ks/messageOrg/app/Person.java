package de.ks.messageOrg.app;

import java.util.Set;
import java.util.HashSet;

public class Person {

    private String title;
    private boolean is_still_participant;
    private String thread_type;
    private String thread_path;
    
    private Participant participants;
    private Set<Message> messages;
    
    
    /**
    * TITLE
    */
    public String getTitle() {
    
        return this.title;
    }
    public void setTitle(String title) {
    
        this.title = title;
    }
    public Person withTitle(String title) {
    
        this.title = title;
        return this;
    }
    
    /**
    * IS_STILL_PARTICIPANT
    */
    public boolean getIs_still_participant() {
    
        return this.is_still_participant;
    }
    public void setIs_still_participant(Boolean is_still_participant) {
    
        this.is_still_participant = is_still_participant;
    }
    public Person withIs_still_participant(Boolean is_still_participant) {
    
        this.is_still_participant = is_still_participant;
        return this;
    }
    
    /**
    * THREAD_TYPE
    */
    public String getThread_type() {
    
        return this.thread_type;
    }
    public void setThread_type(String thread_type) {
    
        this.thread_type = thread_type;
    }
    public Person withThread_type(String thread_type) {
    
        this.thread_type = thread_type;
        return this;
    }
    
    /**
    * THREAD_PATH
    */
    public String getThread_path() {
    
        return this.thread_path;
    }
    public void setThread_path(String thread_path) {
    
        this.thread_path = thread_path;
    }
    public Person withThread_path(String thread_path) {
    
        this.thread_path = thread_path;
        return this;
    }
    
    
    public Set<Message> getMessages() {
    
        if (this.messages == null) {
        
            this.messages = new HashSet<Message>();
        }
        return this.messages;
    }
    public void setMessages(Set<Message> messages) {
    
        this.messages = messages;
    }
    
    
}
