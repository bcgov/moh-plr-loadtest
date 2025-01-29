package ca.bc.gov.health.plr.testharness.requestbuilder.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * A sample Vaadin view class.
 * <p>
 * To implement a Vaadin view just extend any Vaadin component and use @Route
 * annotation to announce it in a URL as a Spring managed bean.
 * <p>
 * A new instance of this class is created for every new user and every browser
 * tab/window.
 */

public class MainView extends AppLayout {

    /**
     * Construct a new Vaadin view.
     * Build the initial UI state for the user accessing the application.
     *
     * 
     * The message service. Automatically injected Spring managed bean.
     */
    public MainView() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 title = new H1("PLR Test Harness");
        title.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.MEDIUM, LumoUtility.FontWeight.BOLD, "headerTitle");
        Div logo = new Div(new Image("/images/gov3_bc_logo.png", "logo"));

        var header = new HorizontalLayout(new DrawerToggle(), logo, title);
        header.addClassName("headerPanel");
        
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidthFull();
        header.addClassNames(LumoUtility.Padding.Vertical.NONE, LumoUtility.Padding.Horizontal.MEDIUM);

        addToNavbar(header);
    }

    private void createDrawer() {
        RouterLink simulationView = new RouterLink("Simulation Test", SimulationTestView.class);
        Anchor refreshLink = new Anchor("", "Reload Page");
        
        simulationView.addClassName("drawerLink");
        refreshLink.addClassNames("drawerLink");

        refreshLink.getElement().setAttribute("router-ignore", true);

        VerticalLayout drawer = new VerticalLayout(
            simulationView,
            refreshLink
        );

        drawer.addClassName("sideDrawer");
        
        addToDrawer(drawer);
    }
}