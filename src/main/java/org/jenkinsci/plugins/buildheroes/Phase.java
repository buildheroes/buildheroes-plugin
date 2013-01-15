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

import hudson.model.Run;
import hudson.model.TaskListener;

import java.io.IOException;
import java.util.List;


public enum Phase {
	COMPLETED;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void handlePhase(Run run, String status, TaskListener listener) {
		BuildheroesProperty property = (BuildheroesProperty) run.getParent().getProperty(BuildheroesProperty.class);
		if (property != null) {
			String token = property.getToken();
			String url;
			if(token != null){
				Post.sendMessage(token);
			}
		}
	}
}