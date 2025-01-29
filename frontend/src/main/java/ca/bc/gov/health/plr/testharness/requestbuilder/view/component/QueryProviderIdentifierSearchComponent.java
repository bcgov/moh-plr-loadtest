package ca.bc.gov.health.plr.testharness.requestbuilder.view.component;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;

public class QueryProviderIdentifierSearchComponent extends VerticalLayout {
    Checkbox identifier = new Checkbox("Identifier");
    RadioButtonGroup<String> identifierType = new RadioButtonGroup<>();
    H4 attributesLabel = new H4("Select Message Attributes");
    H4 identifierTypesLabel = new H4("Select Identifier Type");

    public QueryProviderIdentifierSearchComponent() {


        identifier.setValue(true);
        identifier.setEnabled(false);

        identifierType.setItems( "IPC", "CPN");
        identifierType.setErrorMessage("Required");

        add(attributesLabel, identifier, identifierTypesLabel, identifierType);
    }

    public boolean getIdentifier() {
        return identifier.getValue();
    }

    public RadioButtonGroup<String> getIdentifierType() {
        return identifierType;
    }

    public void setMessageSpec(RadioButtonGroup<String> identifierType) {
        this.identifierType = identifierType;
    }

    public void disableHeaders(boolean disable){
        if(disable){
            attributesLabel.addClassName("disabled-effect-headers");
            identifierTypesLabel.addClassName("disabled-effect-headers");
        }
        else{
            attributesLabel.removeClassName("disabled-effect-headers");
            identifierTypesLabel.removeClassName("disabled-effect-headers");
        }
    }
}
