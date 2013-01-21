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
import hudson.model.AbstractBuild;
import hudson.model.Hudson;
import hudson.model.Run;
import hudson.scm.ChangeLogSet;
import hudson.scm.ChangeLogSet.Entry;

import java.util.HashMap;
import java.util.logging.Logger;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import com.google.gson.Gson;


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
				
				String payload = payloadAsJson(run, status);
				log.info("Token: "   + token);
				log.info("Payload: " + payload);
				
				Post.sendMessage(token, payload);
			}
		}
	}

	private String payloadAsJson(Run run, String status) {
		
		// just to have a build instance beside the run
		AbstractBuild build = (AbstractBuild) run;
		
		Gson jsonConverter = new Gson();
		HashMap<String, Object> jsonModel = new HashMap<String, Object>();
		
		/**
		 * 
		 * JSON shall look like this:
		 * 
		 * {
			 Ê"build_url": "https://some-jenkins.com/bla",
			 Ê"build_number": "unique_build_number",
			 Ê"result": "success/failed",
			 Ê"started_at": "2013-01-23 T17:34:13+01:00",
			 Ê"finished_at": "2013-01-23 T17:41:43+01:00",
			 Ê"latest_commit": {
			 Ê Ê"id": "5eec2c0c8a2f49ef5bfc774677b7392c0ea6037d",
			 Ê Ê"url": "https://github.com/thyphoon/buildheroes/commit/5eec2c0c8a2f49ef5bfc774677b7392c0ea6037d",
			 Ê Ê"author_name": "Andi Bade",
			 Ê Ê"author_email": "andi@galaxycats.com",
			 Ê Ê"message": "added owner to projects",
			 Ê Ê"timestamp": "2012-11-8T17:33:47+01:00"
			 Ê}
			}
		 * 
		 */
		
		jsonModel.put("build_url", Hudson.getInstance().getRootUrl() + build.getUrl());
		
		jsonModel.put("build_number", build.getNumber());
		jsonModel.put("result", status);
		jsonModel.put("started_at", run.getTime());
		jsonModel.put("finished_at", getFinishedTime(run));
		
		jsonModel.put("latest_commit", getLatestCommitInfo(build.getChangeSet()));

		return jsonConverter.toJson(jsonModel);
	}

	private Object getLatestCommitInfo(ChangeLogSet<Entry> changeSet) {
		Entry latestEntry;
		log.info("Size of ChangeLogSet: " + changeSet.getItems().length);
		if(!changeSet.isEmptySet()) {
			latestEntry = (Entry) changeSet.getItems()[0];
			for (Entry entry : changeSet) {
				if(entry.getTimestamp() > latestEntry.getTimestamp()) {
					latestEntry = entry;
				}
			}			
		} else return "";
		
		return createLatestCommitInfoMap(latestEntry);
	}

	private HashMap<String, String> createLatestCommitInfoMap(Entry entry) {
		HashMap<String, String> commitInfosMap = new HashMap<String, String>();
		
		commitInfosMap.put("id", entry.getCommitId());
		commitInfosMap.put("author", entry.getAuthor().getDisplayName());
		commitInfosMap.put("timestamp", new DateTime(entry.getTimestamp()).toString(ISODateTimeFormat.dateTime()));
		commitInfosMap.put("message", entry.getMsg());
		
		return commitInfosMap;
	}
	
	private Object getFinishedTime(Run run) {
		long finishedAt = run.getTimeInMillis()+run.getDuration();
		DateTime time = new DateTime(finishedAt);
		return time.toString(ISODateTimeFormat.dateTime());
	}
}