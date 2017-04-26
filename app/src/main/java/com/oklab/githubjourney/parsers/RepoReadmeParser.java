package com.oklab.githubjourney.parsers;

import com.oklab.githubjourney.data.GitHubRepoContentType;
import com.oklab.githubjourney.data.RepositoryContentDataEntry;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by olgakuklina on 2017-04-18.
 */
public class RepoReadmeParser {
    private static final String TAG = RepoReadmeParser.class.getSimpleName();

    public List<RepositoryContentDataEntry> parse(JSONObject jsonObj) throws JSONException {
        String uri = jsonObj.getString("download_url");
        GitHubRepoContentType type = GitHubRepoContentType.getRepoContentType(jsonObj.getString("type"));
        RepositoryContentDataEntry entry = new RepositoryContentDataEntry("title", uri, type);
        List<RepositoryContentDataEntry> list = new ArrayList<RepositoryContentDataEntry>(1);
        list.add(entry);
        return list;
    }
}
