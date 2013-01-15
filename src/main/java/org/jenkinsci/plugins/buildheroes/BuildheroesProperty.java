package org.jenkinsci.plugins.buildheroes;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.model.JobProperty;
import hudson.model.AbstractProject;

public class BuildheroesProperty extends JobProperty<AbstractProject<?, ?>> {
	private final String token;

	@DataBoundConstructor
 	public BuildheroesProperty(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}
}
