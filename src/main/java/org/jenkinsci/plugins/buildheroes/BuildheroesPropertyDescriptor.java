package org.jenkinsci.plugins.buildheroes;

import hudson.Extension;
import hudson.model.Job;
import hudson.model.JobProperty;
import hudson.model.JobPropertyDescriptor;
import hudson.model.AbstractProject;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.StaplerRequest;

@Extension
public final class BuildheroesPropertyDescriptor extends JobPropertyDescriptor {

  private String token;

  public BuildheroesPropertyDescriptor() {
    super(BuildheroesProperty.class);
    load();
  }

  @Override
  public boolean isApplicable(@SuppressWarnings("rawtypes") Class<? extends Job> jobType) {
    return true;
  }

  public String getDisplayName() {
    return "Buildheroes Notification";
  }

  @Override
  public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
    save();
    return true;
  }

}
