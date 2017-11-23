package ru.faulab.javaee.design.patterns.sample.project;

import com.google.common.base.Preconditions;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.Testable;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.faulab.javaee.design.patterns.sample.project.note.Note;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.net.URI;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyIterable;

@RunWith(Arquillian.class)
public class SampleProjectIT {
    @Deployment
    public static EnterpriseArchive createDeployment() {
        String testingArtifact = System.getProperty("testingArtifact");
        Preconditions.checkNotNull(testingArtifact, "\"testingArtifact\" java property missing");
        EnterpriseArchive enterpriseArchive = ShrinkWrap.createFromZipFile(EnterpriseArchive.class, new File(testingArtifact));
        enterpriseArchive.getAsType(WebArchive.class, path -> path.get().endsWith(".war"))
                .stream().findFirst().ifPresent(webArchive -> Testable.archiveToTest(webArchive.addClass(SampleProjectIT.class)));
        return enterpriseArchive;
    }

    @ArquillianResource
    URI applicationUrl;


    @Test
    public void dummy() {
        List<Note> notes = ClientBuilder.newClient().target(applicationUrl).path("rest").path("notes").request(MediaType.APPLICATION_JSON_TYPE).get(new GenericType<List<Note>>() {
        });
        assertThat(notes, emptyIterable());
    }
}
