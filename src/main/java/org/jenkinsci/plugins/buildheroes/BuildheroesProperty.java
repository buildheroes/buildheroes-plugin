package org.jenkinsci.plugins.buildheroes;

import hudson.Extension;
import hudson.model.JobProperty;
import hudson.model.JobPropertyDescriptor;
import hudson.model.AbstractProject;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

public class BuildheroesProperty extends JobProperty<AbstractProject<?, ?>> {

	// private final String token;
	final public String token;

	@DataBoundConstructor
 	public BuildheroesProperty(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	// @Override
	public BuildheroesPropertyDescriptor getDescriptor() {
		// return new BuildheroesPropertyDescriptor();
		return (BuildheroesPropertyDescriptor) super.getDescriptor();
	}

}
