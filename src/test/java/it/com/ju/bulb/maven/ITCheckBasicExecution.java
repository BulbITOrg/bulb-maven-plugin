package it.com.ju.bulb.maven;

import java.util.List;
import java.util.ArrayList;

import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

public class ITCheckBasicExecution extends AbstractMojoTestCase {
    private static final String BASE_PATH = "src/test/resources/unit/testreport";

    Verifier verifier = new Verifier( new File(BASE_PATH).getAbsolutePath() );

    public ITCheckBasicExecution() throws VerificationException {
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        verifier.executeGoal("clean");
    }

    @Test
    public void testReportMojo() throws Exception {
        verifier.setDebug(true);

        List cliOptions = new ArrayList();
        cliOptions.add( "-N" );
        verifier.executeGoal("clean");
        verifier.executeGoal( "install" );

        verifier.verifyErrorFreeLog();

        verifier.resetStreams();

        File targetDir = new File(BASE_PATH + "/target");
        assertTrue(targetDir.exists());

        File testOutput = new File(BASE_PATH + "/target/trackerTestReport.xml");
        assertTrue(testOutput.exists());
    }
}
