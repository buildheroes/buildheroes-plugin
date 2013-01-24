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

import hudson.model.TaskListener;
import hudson.model.Run;

import java.util.logging.Logger;


public enum Phase {
	COMPLETED;
	
	private final static Logger log = Logger.getLogger(Phase.class.getName());

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void handlePhase(Run run, String status, TaskListener listener) {
		log.info("Handling a completed build, status is " + status);
		BuildheroesProperty property = (BuildheroesProperty) run.getParent().getProperty(BuildheroesProperty.class);
		if (property != null) {
			String token = property.getToken();
			if(token != null){
				Payload payload = new Payload(run, status);
				log.info("Token: "   + token);
				log.info("Payload: " + payload.asJson());
				
				Post.sendMessage(token, payload.asJson());
			}
		}
	}
}