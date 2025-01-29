package ca.bc.gov.health.test.model;

import java.sql.Timestamp;

public class MessageResponse {
    private Timestamp messageReceived;
    private Timestamp messageSent;
    private String statusCode; //can be http or internal status
    private Integer msResponseTime;
    private String response;
    private String responseGuid;
    private Integer msgId;

    //TODO: add more fields from response table

    public Timestamp getMessageReceived() {
        return messageReceived;
    }
    public void setMessageReceived(Timestamp messageReceived) {
        this.messageReceived = messageReceived;
    }
    public Timestamp getMessageSent() {
        return messageSent;
    }
    public void setMessageSent(Timestamp messageSent) {
        this.messageSent = messageSent;
    }
    public String getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
    public Integer getMsResponseTime() {
        return msResponseTime;
    }
    public void setMsResponseTime(Integer msResponseTime) {
        this.msResponseTime = msResponseTime;
    }
    public String getResponse() {
        return response;
    }
    public void setResponse(String response) {
        this.response = response;
    }
    public void setMsgId(int msgId) {
       this.msgId = msgId;
    }
    public Integer getMsgId(){
        return msgId;
    }
    public String getResponseGuid() {
        return responseGuid;
    }
    public void setResponseGuid(String responseGuid) {
        this.responseGuid = responseGuid;
    }

}
