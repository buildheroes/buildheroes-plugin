package org.jenkinsci.plugins.buildheroes;

import hudson.Extension;
import hudson.model.JobProperty;
import hudson.model.JobPropertyDescriptor;
import hudson.model.AbstractProject;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

public class BuildheroesProperty extends JobProperty<AbstractProject<?, ?>> {
	
	private final String token;

	@DataBoundConstructor
 	public BuildheroesProperty(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}
	
	@Override
	public BuildheroesPropertyDescriptor getDescriptor() {
		return new BuildheroesPropertyDescriptor();
	}

	@Extension
	public static final class BuildheroesPropertyDescriptor extends JobPropertyDescriptor {
		
		private String token;
		
		public BuildheroesPropertyDescriptor() {
			super(BuildheroesProperty.class);
			load();
		}

		public String getDisplayName() {
			return "Buildheroes Notification";
		}

		@Override
		public boolean configure(StaplerRequest req, JSONObject formData)
				throws FormException {
			setToken(formData.getString("token"));
			save();
			return super.configure(req, formData);
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}
	}
}
