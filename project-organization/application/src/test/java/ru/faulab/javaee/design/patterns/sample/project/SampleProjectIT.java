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

import java.io.File;
import java.net.URL;

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
    URL applicationUrl;


    @Test
    public void dummy() {
        System.out.println("sdf" + applicationUrl);
    }
}
