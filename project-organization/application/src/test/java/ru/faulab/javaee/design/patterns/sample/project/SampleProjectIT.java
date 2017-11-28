package ru.faulab.javaee.design.patterns.sample.project;

import com.google.common.base.Preconditions;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.faulab.javaee.design.patterns.sample.project.note.Note;
import ru.faulab.javaee.design.patterns.sample.project.web.JacksonActivator;
import ru.faulab.javaee.design.patterns.sample.project.web.JacksonConfiguration;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

@RunWith(Arquillian.class)
public class SampleProjectIT {
    @Deployment
    public static WebArchive createDeployment() {
        String testingArtifact = System.getProperty("testingArtifact");
        Preconditions.checkNotNull(testingArtifact, "\"testingArtifact\" java property missing");
        return ShrinkWrap.createFromZipFile(WebArchive.class, new File(testingArtifact));
    }

    @ArquillianResource
    URI applicationUrl;


    @Test
    public void dummy() {
        List<Note> notes = ClientBuilder.newClient()
                .register(JacksonConfiguration.class)
                .register(JacksonActivator.class)
                .target(applicationUrl).path("rest").path("notes")
                .request(MediaType.APPLICATION_JSON_TYPE).get(new GenericType<List<Note>>() {});
        assertThat(notes, contains(Note.builder().id("1").content("Privet").createdWhen(LocalDateTime.of(1984, Month.DECEMBER, 8, 2, 30)).build()));
    }
}
