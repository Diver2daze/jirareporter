package com.amirov.jirareporter;

import com.amirov.jirareporter.jira.JIRAConfig;
import com.amirov.jirareporter.teamcity.TeamCityXMLParser;
import com.atlassian.jira.rest.client.NullProgressMonitor;
import com.atlassian.jira.rest.client.domain.Comment;

import static com.amirov.jirareporter.jira.JIRAClient.*;

public class Reporter {

    public static void report(){
        System.out.println("ISSUE: "+RunnerParamsProvider.getIssueId());
        System.out.println("Title: "+getIssue().getSummary());
        System.out.println("Description: "+getIssue().getDescription());
        NullProgressMonitor pm = new NullProgressMonitor();
        getRestClient().getIssueClient().addComment(pm, getIssue().getCommentsUri(), Comment.valueOf(TeamCityXMLParser.getTestResultText()));
    }

    public static void progressIssue(){
        NullProgressMonitor pm = new NullProgressMonitor();
        if(RunnerParamsProvider.progressIssueIsEnable()){
            String teamCityBuildStatus = TeamCityXMLParser.getStatusBuild();
            getRestClient().getIssueClient().transition(getIssue().getTransitionsUri(), getTransitionInput(JIRAConfig.prepareJiraWorkflow(teamCityBuildStatus).get(getIssueStatus())), pm);
        }
    }

    private static void issueIdAlert(){
        if(RunnerParamsProvider.getIssueId() == null || RunnerParamsProvider.getIssueId().isEmpty()){
            System.out.println("Issue id is empty");
        }
    }
}