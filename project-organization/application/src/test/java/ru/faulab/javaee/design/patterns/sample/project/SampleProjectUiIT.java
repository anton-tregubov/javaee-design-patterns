package ru.faulab.javaee.design.patterns.sample.project;

import com.google.common.base.Preconditions;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.io.File;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.jboss.arquillian.graphene.Graphene.waitGui;

@RunWith(Arquillian.class)
public class SampleProjectUiIT {
    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        String testingArtifact = System.getProperty("testingArtifact");
        Preconditions.checkNotNull(testingArtifact, "\"testingArtifact\" java property missing");
        return ShrinkWrap.createFromZipFile(WebArchive.class, new File(testingArtifact));
    }

    @ArquillianResource
    URI applicationUrl;

    @Drone
    WebDriver driver;

    @FindBy(id = "operations-tag-Note_Module")
    WebElement notesApi;

    @Test
    public void open_api_loaded() {
        driver.get(applicationUrl.toString());
        waitGui(driver).withTimeout(2, TimeUnit.SECONDS).until().element(notesApi).is().visible();
        notesApi.click();
        assertThat(notesApi.getText(), is("Note Module"));
    }
}
