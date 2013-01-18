package org.jenkinsci.plugins.buildheroes;

import hudson.model.FreeStyleBuild;
import hudson.model.JobProperty;
import hudson.model.FreeStyleProject;
import hudson.model.listeners.RunListener;
import hudson.tasks.Shell;

import org.jvnet.hudson.test.FakeChangeLogSCM;
import org.jvnet.hudson.test.FakeChangeLogSCM.EntryImpl;
import org.jvnet.hudson.test.HudsonTestCase;


public class PluginTest extends HudsonTestCase {

	@Override protected void setUp() throws Exception {
        super.setUp();
        RunListener.all().add(new JobListener());
    }
	
	public void test1() throws Exception {
        FreeStyleProject project = createFreeStyleProject();

        // add our plugin to the project config
        project.getBuildersList().add(new Shell("echo hello"));
        JobProperty<? super FreeStyleProject> bhProps = new BuildheroesProperty("test_token");
		project.addProperty(bhProps);
		
		// add fake scm changesets
		FakeChangeLogSCM scm = new FakeChangeLogSCM();
		EntryImpl change = scm.addChange().withAuthor("Matthias").withMsg("useless commit message!");
		project.setScm(scm);
		
		FreeStyleBuild build = project.scheduleBuild2(0).get();
        System.out.println(build.getDisplayName()+" completed");
    }
	
}
