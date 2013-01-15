/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jenkinsci.plugins.buildheroes;

import hudson.Extension;
import hudson.model.JobPropertyDescriptor;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.StaplerRequest;

@Extension
public final class BuildheroesPropertyDescriptor extends JobPropertyDescriptor {
	
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