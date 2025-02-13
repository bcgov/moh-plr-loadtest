package ca.bc.gov.health.plr.testharness.requestbuilder.view.component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.health.plr.testharness.requestbuilder.config.AppConfig;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;

import ca.bc.gov.health.plr.testharness.requestbuilder.code.MessageType;
import org.apache.commons.configuration2.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageComponent extends VerticalLayout{

    private static final Logger log = LoggerFactory.getLogger(MessageComponent.class);
    ComboBox<String> environmentName = new ComboBox<>();
    ComboBox<String> messageType = new ComboBox<>();
    NumberField numRequests = new NumberField("Maximum # Records");
    NumberField numUsers = new NumberField("Maximum # Concurrent Users");
    NumberField pauseDuration = new NumberField("Pause Duration (milliseconds)");
    Button completedTag = new Button("Completed");
    Button failedTag = new Button("Failed");
    QueryProviderIdentifierSearchComponent queryProviderIdentifierSearchComponent = new QueryProviderIdentifierSearchComponent();

    protected final Configuration config = AppConfig.getConfig();

    public MessageComponent(){
        addClassName("message-container");

        environmentName.setLabel("Choose Environment:");
        environmentName.setItems(getEnvironmentNames());

        environmentName.addClassName("bordered");
        environmentName.setErrorMessage("Required");
        environmentName.setWidth("210px");
        
        queryProviderIdentifierSearchComponent.setPadding(false);
        queryProviderIdentifierSearchComponent.setSpacing(false);

        messageType.setLabel("Message Type to Fetch");
        messageType.setItems(getMessageTypeNames());
        messageType.setWidth("350px");
        messageType.addClassName("bordered");
        messageType.setErrorMessage("Required");
        messageType.addValueChangeListener(event -> {
            System.out.println("EventValue:" +event.getValue());
            if (event.getValue().equals(String.valueOf(MessageType.QUERY_PROVIDER_BY_IDENTIFIER))) {
                System.out.println("Query Provider Identifier Search selected");
                numRequests.setVisible(true);
                numRequests.setErrorMessage("Required");
                numUsers.setVisible(true);
                numUsers.setErrorMessage("Required");
                pauseDuration.setVisible(true);
                pauseDuration.setErrorMessage("Required");
                add(queryProviderIdentifierSearchComponent);
            } 
            else{
                System.out.println("Not implemented yet.");
                remove(queryProviderIdentifierSearchComponent);         
             }
        });

        numRequests.setWidth("350px");
        numRequests.addClassName("bordered");
        numRequests.setErrorMessage("Required");

        numUsers.setWidth("350px");
        numUsers.addClassName("bordered");
        numUsers.setErrorMessage("Required");

        pauseDuration.setWidth("350px");
        pauseDuration.addClassName("bordered");
        pauseDuration.setErrorMessage("Required");

        completedTag.addClassNames("extractionIndicatorHidden","completed");
        failedTag.addClassNames("extractionIndicatorHidden","failed");

        HorizontalLayout viewStatus = new HorizontalLayout(completedTag,failedTag);
        
        add(viewStatus, environmentName, messageType, numRequests, numUsers, pauseDuration);
    }

    public String[] getEnvironmentNames(){
        String envs = config.getString("env.names");
        log.info("getEnvironmentNames {}", envs);
        return envs.split(",");
    }

    public ComboBox<String> getEnvironmentName() {
        return environmentName;
    }

    public void setEnvironmentName(ComboBox<String> environmentName) {
        this.environmentName = environmentName;
    }

    public String[] getMessageTypeNames(){
        Class<?> msgClass = MessageType.class;
        Field[] fields = msgClass.getDeclaredFields();
        List<String> msgTypes = new ArrayList<>();
        for(Field field: fields){
            field.setAccessible(true);
            try {
                msgTypes.add((String)field.get(null));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return msgTypes.toArray(new String[fields.length]);
    }

    public ComboBox<String> getMessageType() {
        return messageType;
    }

    public void setMessageType(ComboBox<String> messageType) {
        this.messageType = messageType;
    }

    public NumberField getNumRequests() {
        return numRequests;
    }

    public NumberField getNumUsers() {
        return numUsers;
    }

    public NumberField getPauseDuration() {
        return pauseDuration;
    }

    public Button getCompletedTag() {
        return completedTag;
    }

    public void setCompletedTag(Button completedTag) {
        this.completedTag = completedTag;
    }

    public Button getFailedTag() {
        return failedTag;
    }

    public void setFailedTag(Button failedTag) {
        this.failedTag = failedTag;
    }

    public void setNumRequests(NumberField numRequests) {
        this.numRequests = numRequests;
    }

    public void setNumUsers(NumberField numUsers) {
        this.numUsers = numUsers;
    }

    public void setPauseDuration(NumberField pauseDuration) {
        this.pauseDuration = pauseDuration;
    }

    public QueryProviderIdentifierSearchComponent getQueryProviderIdentifierSearchComponent(){
        return queryProviderIdentifierSearchComponent;
    }
}
