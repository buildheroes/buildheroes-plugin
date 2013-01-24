package org.jenkinsci.plugins.buildheroes;

import hudson.model.AbstractBuild;
import hudson.model.Hudson;
import hudson.model.Run;
import hudson.model.User;
import hudson.plugins.git.GitChangeLogParser;
import hudson.plugins.git.GitChangeSet;
import hudson.plugins.git.GitChangeSetList;
import hudson.scm.ChangeLogSet;
import hudson.scm.ChangeLogSet.Entry;
import hudson.tasks.Mailer;
import hudson.tasks.Mailer.UserProperty;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.xml.sax.SAXException;

import com.google.gson.Gson;

public class Payload {
	
	private final static Logger log = Logger.getLogger(Payload.class.getName());
	
	private Run run;
	private String status;
	private AbstractBuild build;
	
	public Payload(Run run, String status) {
		this.run = run;
		this.status = status;
		// just to have a build instance beside the run
		build = (AbstractBuild) run;
	}

	public String asJson() {
		
		Gson jsonConverter = new Gson();
		HashMap<String, Object> jsonModel = new HashMap<String, Object>();
		
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
		commitInfosMap.put("author_name", entry.getAuthor().getDisplayName());
		commitInfosMap.put("author_email", getEmailFromLogFileForAuthor(entry.getAuthor().getDisplayName()));
		commitInfosMap.put("timestamp", new DateTime(entry.getTimestamp()).toString(ISODateTimeFormat.dateTime()));
		commitInfosMap.put("message", entry.getMsg());
		
		return commitInfosMap;
	}
	

	// hell! why do I need to do this? Because
	// email is not a first-class citizen in Jenkins.
	// git-plugin retrieves that information but is later
	// omitted. So I'm going to bind myself to git-plugin
	// and reuse their parser magic.
	private String getEmailFromLogFileForAuthor(String authorsName) {
		File changelogFile = new File(run.getRootDir(), "changelog.xml");
		GitChangeLogParser parser = new GitChangeLogParser(true);
		User author;
		try {
			GitChangeSetList changeSet = parser.parse(build, changelogFile);
			for (GitChangeSet change : changeSet) {
				author = change.getAuthor();
				if(author.getDisplayName().equals(authorsName)) {
					UserProperty obj = author.getProperty(Mailer.UserProperty.class);
					String email = obj.getAddress();
					return email;
				}
			}
		} catch (IOException e) {
			log.severe(e.getMessage());
			e.printStackTrace();
		} catch (SAXException e) {
			log.severe(e.getMessage());
			e.printStackTrace();
		}
		return "";
	}

	private Object getFinishedTime(Run run) {
		long finishedAt = run.getTimeInMillis()+run.getDuration();
		DateTime time = new DateTime(finishedAt);
		return time.toString(ISODateTimeFormat.dateTime());
	}

}
