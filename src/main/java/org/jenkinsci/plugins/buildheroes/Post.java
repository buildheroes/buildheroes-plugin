package org.jenkinsci.plugins.buildheroes;

import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;


public class Post {
    public static void sendMessage(String token){
        String url = "http://buildheroes.dev/api/projects/" + token + "/builds";
        InputStream in = null;

        try {
            HttpClient client = new HttpClient();
            PostMethod method = new PostMethod(url);

            //Add any parameter if u want to send it with Post req.
            method.addParameter("p", "apple");

            int statusCode = client.executeMethod(method);

            if (statusCode != -1) {
                in = method.getResponseBodyAsStream();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}