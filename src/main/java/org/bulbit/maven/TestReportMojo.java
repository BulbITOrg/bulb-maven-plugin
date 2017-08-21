package org.bulbit.maven;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import org.bulbit.maven.model.Test;
import org.bulbit.maven.model.Tests;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * Class implementing maven plugin.
 *
 * @author Janis Upitis
 */
@Mojo(name = "testreport", defaultPhase = LifecyclePhase.TEST)
public class TestReportMojo extends AbstractMojo {
    private static final String TICKETS_LIST_PREFIX = "Tickets:";

    private Tests tests;

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject mavenProject;

    public Tests getTests() {
        return tests;
    }

    public TestReportMojo() {
    }

    public void setMavenProject(MavenProject mavenProject) {
        if (mavenProject == null) {
            throw new IllegalArgumentException("Maven project can't be null.");
        }

        this.mavenProject = mavenProject;
    }

    private void storeReportAsFile(File file, Tests tests) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Tests.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        // output pretty printed
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        jaxbMarshaller.marshal(tests, file);
    }

    private String processCommentLine(String input) {
        if (input.startsWith("@")) {
            return null;
        }

        return input;
    }

    private List<Test> processSourceFile(File file) throws IOException, ParseException {
        List<Test> tests = new ArrayList<Test>();
        CompilationUnit cu = JavaParser.parse(file);

        List<TypeDeclaration> types = cu.getTypes();
        for (TypeDeclaration type : types) {
            List<BodyDeclaration> members = type.getMembers();
            for (BodyDeclaration member : members) {
                if (member instanceof MethodDeclaration) {
                    MethodDeclaration method = (MethodDeclaration) member;

                    boolean isTest = false;
                    for(AnnotationExpr a : method.getAnnotations()) {
                        isTest = isTest || "Test".equals(a.getName().getName());
                    }

                    if (isTest) {
                        this.getLog().info("Preparing report for test method:" + method.getName());
                        Test t = new Test();
                        String testKey = cu.getPackage().getPackageName() + "." + type.getName() + "." + method.getName();

                        this.getLog().info("Test key:" + testKey);

                        t.setKey(testKey);

                        if (method.getComment() != null) {
                            String content = method.getComment().getContent().trim();
                            String[] commentLines = content.split("\\n");
                            for(int i=0;i<commentLines.length;i++) {
                                commentLines[i] = commentLines[i].replace("*", "").trim();
                            }

                            int commentStartLine = 0;
                            if (commentLines[0].startsWith(TICKETS_LIST_PREFIX)) {
                                String ticketsList = commentLines[0].replace(TICKETS_LIST_PREFIX, "");
                                String[] tickets = ticketsList.split(",");

                                t.setTickets(new ArrayList<String>());
                                for(String ticket : tickets) {
                                    t.getTickets().add(ticket.trim());
                                }

                                commentStartLine = 1;
                            }

                            if (commentLines.length > 1) {
                                StringBuilder desc = new StringBuilder();
                                for(int i=commentStartLine;i<commentLines.length;i++) {
                                    String text = processCommentLine(commentLines[i]);
                                    if (text != null) {
                                        desc.append(text);

                                        if (i != commentLines.length - 1) {
                                            desc.append(System.getProperty("line.separator"));
                                        }
                                    }
                                }

                                t.setDescription(desc.toString().trim());
                            }
                        }

                        tests.add(t);
                    }
                }
            }
        }

        return tests;
    }

    /**
     *
     * @throws MojoExecutionException
     * @throws MojoFailureException
     */
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            this.getLog().info("Preparing test report.");

            Tests t = new Tests();
            List<Test> tests = new ArrayList<>();

            for (String root : this.mavenProject.getTestCompileSourceRoots()) {
                this.getLog().info("Processing test sources in : " + root);

                List<File> files = FileUtils.getFiles(new File(root), "**/*.java", null);
                for(File file : files) {
                    this.getLog().debug("Processing file " + file.getAbsolutePath());
                    tests.addAll(this.processSourceFile(file));
                }
            }

            t.setTests(tests);
            this.tests = t;

            this.storeReportAsFile(new File(Paths.get(this.mavenProject.getBuild().getOutputDirectory().toString(), "trackerTestReport.xml").toString()), t);
        } catch (IOException ex) {
            throw new MojoFailureException("Failed to process source files.", ex);
        } catch (ParseException ex) {
            throw new MojoFailureException("Failed to parse source files.", ex);
        } catch (JAXBException ex) {
            throw new MojoFailureException("Failed to store report.", ex);
        }
    }
}
