package org.jenkinsci.plugins.buildheroes;

import java.util.logging.Logger;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;


public class Post {

	private final static Logger log = Logger.getLogger(Post.class.getName());

	public static void sendMessage(String token, String payload){

        String url = "https://www.buildheroes.com/api/projects/" + token + "/builds";
		String responseBody = "";

        try {
            HttpClient client = new HttpClient();
            PostMethod method = new PostMethod(url);
			method.setRequestHeader("Content-Type", "application/json");

            method.addParameter("payload", payload);

            log.info("Sending a post message to " + url + " now.");
            int statusCode = client.executeMethod(method);

            if (statusCode != -1) {
                responseBody = method.getResponseBodyAsString();
            }

            log.info("Message has been sent, response body is: " + responseBody);

        } catch (Exception e) {
            log.severe(e.getMessage() + " STACKTRACE: " + e.getStackTrace().toString());
        }

        log.info("Notification is done, buildheroes exits now.");
    }
}
