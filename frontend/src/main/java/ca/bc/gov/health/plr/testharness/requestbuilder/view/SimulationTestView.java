package ca.bc.gov.health.plr.testharness.requestbuilder.view;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;

import ca.bc.gov.health.plr.testharness.requestbuilder.code.MessageType;
import ca.bc.gov.health.plr.testharness.requestbuilder.dto.MessageTypeModel;
import ca.bc.gov.health.plr.testharness.requestbuilder.service.GatlingTriggerService;
import ca.bc.gov.health.plr.testharness.requestbuilder.view.component.MessageComponent;
import ca.bc.gov.health.plr.testharness.requestbuilder.view.component.QueryProviderIdentifierSearchComponent;

@UIScope
@Route(value="", layout = MainView.class)
@Component
@PageTitle("Simulation Test")
public class SimulationTestView extends VerticalLayout {
    
    public SimulationTestView(@Autowired GatlingTriggerService service) {
        MessageComponent messageComponent = new MessageComponent();

        Notification loadNotif = new Notification();

        Div text = new Div(new Text("Loading..."));
        loadNotif.add(text);
        loadNotif.setPosition(com.vaadin.flow.component.notification.Notification.Position.TOP_STRETCH);
        loadNotif.addThemeVariants(NotificationVariant.LUMO_PRIMARY);

        Button runButton = new Button("Execute");
        runButton.addThemeVariants(ButtonVariant.LUMO_LARGE);
        runButton.addClickShortcut(Key.ENTER);
        runButton.addClassName("floatingButton");

        runButton.addClickListener(e -> {

            boolean fieldsValidated = validateFields(messageComponent);

            if(messageComponent.getNumRequests().getValue() != null){
                if(messageComponent.getNumRequests().getValue() <= 0){
                    messageComponent.getNumRequests().setValue(null); // edge case for not having a limit
                }
            }

            if(messageComponent.getNumUsers().getValue() != null){
                if(messageComponent.getNumUsers().getValue() <= 0){
                    messageComponent.getNumUsers().setValue(null); // edge case for not having a limit
                }
            }

            if(messageComponent.getPauseDuration().getValue() != null){
                if(messageComponent.getPauseDuration().getValue() <= 0){
                    messageComponent.getPauseDuration().setValue(null); // edge case for not having a limit
                }
            }

            Icon icon = VaadinIcon.CHECK_CIRCLE.create();

            if (fieldsValidated) {
                UI ui = UI.getCurrent();
                new Thread(() -> {
                    ui.access(() -> {
                        messageComponent.setEnabled(false);
                        messageComponent.addClassName("running");
                        loadNotif.open();
                        ui.push();
                    });

                    ui.access(() -> {
                        Notification complete = null;
                        String completeText = null;

                        boolean success = true;

                        messageComponent.setEnabled(false);
                        messageComponent.getQueryProviderIdentifierSearchComponent().disableHeaders(true);

                        try {
                            MessageTypeModel mtm = createMessageTypeModel(messageComponent);
                            success = service.triggerGatling(mtm);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                            completeText = "Connection error. Please check logs for details.";     
                            success = false;
                        }

                        messageComponent.removeClassName("running");
                        
                        if(success){
                            messageComponent.setEnabled(false);
                            messageComponent.getQueryProviderIdentifierSearchComponent().disableHeaders(true);
                            messageComponent.addClassName("completed");
                            messageComponent.removeClassName("failed");
                            messageComponent.getCompletedTag().removeClassName("extractionIndicatorHidden");
                            messageComponent.getFailedTag().addClassName("extractionIndicatorHidden");
                            completeText = "Success";
                            complete = Notification.show(completeText, 5000, com.vaadin.flow.component.notification.Notification.Position.BOTTOM_STRETCH);
                            complete.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                        }else{
                            messageComponent.setEnabled(true);
                            messageComponent.getQueryProviderIdentifierSearchComponent().disableHeaders(false);
                            messageComponent.addClassName("failed");
                            messageComponent.removeClassName("completed");
                            messageComponent.getFailedTag().removeClassName("extractionIndicatorHidden");
                            messageComponent.getCompletedTag().addClassName("extractionIndicatorHidden");
                            if(completeText == null) { completeText = "There was an error in the request. Please check logs for details.";}
                            completeText = "Fail: " + completeText;
                            complete = Notification.show(completeText, 5000, com.vaadin.flow.component.notification.Notification.Position.BOTTOM_STRETCH);
                            complete.addThemeVariants(NotificationVariant.LUMO_ERROR);
                        }

                        var completeLayout = new HorizontalLayout(icon, new Text(completeText));
                        complete.add(completeLayout);
                        loadNotif.close();

                        ui.push();
                    });
                }).start();
            }
        }); 

        add(runButton, new HorizontalLayout(messageComponent));
    }

    public MessageTypeModel createMessageTypeModel(MessageComponent messageComponent){
        MessageTypeModel mtm = new MessageTypeModel();
        
        if (messageComponent.getNumRequests().getValue() != null) mtm.setNumberOfRecords(messageComponent.getNumRequests().getValue().intValue());
        if (messageComponent.getNumUsers().getValue() != null) mtm.setNumberOfUsers(messageComponent.getNumUsers().getValue().intValue());
        if (messageComponent.getPauseDuration().getValue() != null) mtm.setPauseDuration(messageComponent.getPauseDuration().getValue().intValue());
        
        mtm.setId(UUID.randomUUID().toString());
        mtm.setEnvironmentName(messageComponent.getEnvironmentName().getValue());
        
        mtm.setMessageTypeName(messageComponent.getMessageType().getValue()); 
        mtm.setMsgSpec(messageComponent.getMessageSpec().getValue());
        
        if(messageComponent.getMessageType().getValue().equals((MessageType.QUERY_PROVIDER_BY_IDENTIFIER))){

            String identifierType = messageComponent.getQueryProviderIdentifierSearchComponent().getIdentifierType().getValue();

            if (identifierType.equals("CPN")) {
                mtm.setHasIdentifierCPN(true);
            }
            else if (identifierType.equals("IPC")) {
                mtm.setHasIdentifierIPC(true);
            }
        }

        return mtm;
    }

    public boolean validateFields(MessageComponent mc){
        boolean isValid = true;
        String typeMessage = mc.getMessageType().getValue();
        String specMessage = mc.getMessageSpec().getValue();
        Double requestNum = mc.getNumRequests().getValue();
        Double usersNum = mc.getNumUsers().getValue();
        Double pauseDuration = mc.getPauseDuration().getValue();

        String extEnvName = mc.getEnvironmentName().getValue();
          
        if (extEnvName == null || extEnvName == "") {
            Notification.show("Please choose a Environment.");
            mc.getEnvironmentName().setInvalid(true);
            isValid = false;
        }

        if (specMessage == null) {
            Notification.show("Please select a specific Message Type.");
            mc.getMessageSpec().setInvalid(true);
            isValid = false;
        } 
        
        if ((requestNum == null) ) { 
            Notification.show("Please fill out Maximum # Records.");
            mc.getNumRequests().setInvalid(true);
            isValid = false;
        } 
        
        if ((usersNum == null) ) { 
            Notification.show("Please fill out Maximum # Concurrent Users.");
            mc.getNumUsers().setInvalid(true);
            isValid = false;
        } 

        if ((pauseDuration == null) ) { 
            Notification.show("Please fill out Pause Duration (milliseconds).");
            mc.getPauseDuration().setInvalid(true);
            isValid = false;
        } 

        if (typeMessage == null) {
            Notification.show("Please select a Message Type.");
            mc.getMessageType().setInvalid(true);
            isValid = false;
        } 
        else{
        
            if(typeMessage.equals((MessageType.QUERY_PROVIDER_BY_IDENTIFIER))){
                QueryProviderIdentifierSearchComponent identifierComponent = mc.getQueryProviderIdentifierSearchComponent();

                if(!identifierComponent.getIdentifier()){
                    Notification.show("An error has occurred. Identifier is required to be selected.");
                    //mc.getMessageType().setInvalid(true);

                    isValid = false;
                }

                if(identifierComponent.getIdentifierType().getValue() == null){
                    Notification.show("An error has occurred. Identifier Type is required to be selected.");
                    identifierComponent.getIdentifierType().setInvalid(true);

                    isValid = false;
                }
            }        
        
        }

        return isValid;
    }
}
