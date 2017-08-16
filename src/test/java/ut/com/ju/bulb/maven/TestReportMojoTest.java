package ut.com.ju.bulb.maven;

import com.ju.bulb.maven.TestReportMojo;
import com.ju.bulb.maven.model.Tests;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;
import org.junit.Test;

import java.io.File;
import java.nio.file.Paths;

public class TestReportMojoTest extends AbstractMojoTestCase {
    private static final String BASE_PATH = "src/test/resources/unit/testreport/";

    protected void setUp() throws Exception
    {
        super.setUp();
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();

        FileUtils.deleteDirectory(Paths.get(BASE_PATH, "target").toFile());
    }

    @Test
    public void testMojo() throws Exception
    {
        File pom = getTestFile( BASE_PATH + "pom.xml" );
        assertNotNull( pom );
        assertTrue( pom.exists() );

        TestReportMojo myMojo = (TestReportMojo) lookupMojo( "testreport", pom );
        assertNotNull( myMojo );

        MavenProject p = new MavenProject();
        p.getTestCompileSourceRoots().add(BASE_PATH + "src/test/java");
        p.getBuild().setOutputDirectory("target");
        myMojo.setMavenProject(p);
        myMojo.execute();

        Tests t = myMojo.getTests();
        assertNotNull(t);
        assertEquals(4, t.getTests().size());

        assertEquals("com.test.NotificationTest.claimNoteNotificationTest", t.getTests().get(0).getKey());
        assertNotNull("There should be tickets for the testcase", t.getTests().get(0).getTickets());
        assertEquals(1, t.getTests().get(0).getTickets().size());

        assertEquals("ZSA-5", t.getTests().get(0).getTickets().get(0));
        String desc = "<p>Generates 2 claims with their respective claim notes:\n" +
                "<ul>\n" +
                "<li>First claim note should be visible and has claimant contact</li>\n" +
                "<li>Second claim note shouldn't be visible</li>\n" +
                "<li>Check that email has been received only for the first claim</li>\n" +
                "</ul>\n" +
                "</p>\n" +
                "<p>\n" +
                "Check that there exists a timeline entry for the first claim\n" +
                "containing claim note\n" +
                "</p>";
        assertEquals(desc, t.getTests().get(0).getDescription());

        assertEquals("com.test.NotificationTest.testUpload", t.getTests().get(1).getKey());
        assertNotNull("There should be tickets for the testcase", t.getTests().get(1).getTickets());
        assertEquals(2, t.getTests().get(1).getTickets().size());

        assertEquals("ZSA-4", t.getTests().get(1).getTickets().get(0));
        assertEquals("ZSA-1", t.getTests().get(1).getTickets().get(1));

        String desc2 = "Create claim\n" +
                "Go to claim detail page\n" +
                "Upload claim document to it\n" +
                "Check that email has been sent to the customer";
        assertEquals(desc2, t.getTests().get(1).getDescription());


        assertEquals("com.test.NotificationTest.testWithoutComment", t.getTests().get(2).getKey());
        assertNull(t.getTests().get(2).getDescription());

        assertEquals("com.test2.SimpleTest.testMethodWithJavaDoc", t.getTests().get(3).getKey());
    }
}
