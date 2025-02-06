package ca.bc.gov.health.plr.testharness.requestbuilder.dto;

public class MessageTypeModel {

    private String id;
    private String messageTypeName;
    private String environmentName;
    private int numberOfRecords;
    private int numberOfUsers;
    private int pauseDuration;
    private boolean hasIdentifierCPN;
    private boolean hasIdentifierIPC;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessageTypeName() {
        return messageTypeName;
    }

    public void setMessageTypeName(String messageTypeName) {
        this.messageTypeName = messageTypeName;
    }

    public String getEnvironmentName() {
        return environmentName;
    }

    public void setEnvironmentName(String environmentName) {
        this.environmentName = environmentName;
    }

    public int getNumberOfRecords() {
        return numberOfRecords;
    }

    public void setNumberOfRecords(int numberOfRecords) {
        this.numberOfRecords = numberOfRecords;
    }

    public int getNumberOfUsers() {
        return numberOfUsers;
    }

    public void setNumberOfUsers(int numberOfUsers) {
        this.numberOfUsers = numberOfUsers;
    }

    public int getPauseDuration() {
        return pauseDuration;
    }

    public void setPauseDuration(int pauseDuration) {
        this.pauseDuration = pauseDuration;
    }

    public boolean getHasIdentifierCPN() {
        return hasIdentifierCPN;
    }

    public void setHasIdentifierCPN(boolean hasIdentifierCPN) {
        this.hasIdentifierCPN = hasIdentifierCPN;
    }

    public boolean getHasIdentifierIPC() {
        return hasIdentifierIPC;
    }

    public void setHasIdentifierIPC(boolean hasIdentifierIPC) {
        this.hasIdentifierIPC = hasIdentifierIPC;
    }
}
