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
package com.focusit.notification;

import java.io.IOException;
import java.util.List;

import com.focusit.notification.model.BuildState;
import com.focusit.notification.model.JobState;
import com.focusit.notification.model.ScmState;
import hudson.EnvVars;
import hudson.model.*;
import jenkins.model.Jenkins;


@SuppressWarnings({ "unchecked", "rawtypes" })
public enum Phase {
    QUEUED, STARTED, COMPLETED, FINALIZED;

    @SuppressWarnings( "CastToConcreteClass" )
    public void handle(Run run, TaskListener listener, long timestamp) {

        Endpoint target = GlobalNotificationConfig.get().getEndpoint();
        {
            if (isRun(target, run.getResult())) {
                int triesRemaining = target.getRetries();
                boolean failed = false;
                do {
                    // Represents a string that will be put into the log
                    // if there is an error contacting the target.
                    String urlIdString = "url 'unknown'";
                    try {
                        EnvVars environment = run.getEnvironment(listener);
                        // Expand out the URL from environment + url.
                        String expandedUrl;
                        expandedUrl = environment.expand(target.getUrl());
                        urlIdString = String.format("url '%s'", expandedUrl);
                        listener.getLogger().println( String.format( "Notifying endpoint with %s", urlIdString));
                        JobState jobState = buildJobState(run.getParent(), run, listener, timestamp, target);
                        Protocol.HTTP.send(expandedUrl,
                                                  Format.JSON.serialize(jobState),
                                                  target.getTimeout(),
                                                  true);
                    } catch (Throwable error) {
                        failed = true;
                        error.printStackTrace( listener.error( String.format( "Failed to notify endpoint with %s", urlIdString)));
                        listener.getLogger().println( String.format( "Failed to notify endpoint with %s - %s: %s",
                                                                     urlIdString, error.getClass().getName(), error.getMessage()));
                        if (triesRemaining > 0) {
                            listener.getLogger().println( String.format( "Reattempting to notify endpoint with %s (%d tries remaining)", urlIdString, triesRemaining));
                        }
                    }
                }
                while (failed && --triesRemaining >= 0);
            }
        }
    }


    /**
     * Determines if the endpoint specified should be notified at the current job phase.
     */
    private boolean isRun( Endpoint endpoint, Result result ) {
        /*
        String event = endpoint.getEvent();
        
        String status = "";
        if ( result != null ) {
            status = result.toString();
        }
        
        boolean buildFailed = event.equals("failed") && this.toString().toLowerCase().equals("finalized") && status.toLowerCase().equals("failure");
        		
        return (( event == null ) || event.equals( "all" ) || event.equals( this.toString().toLowerCase()) || buildFailed);
        */
        return true;
    }

    private JobState buildJobState(Job job, Run run, TaskListener listener, long timestamp, Endpoint target)
        throws IOException, InterruptedException
    {

        Jenkins            jenkins      = Jenkins.getInstance();
        String             rootUrl      = jenkins.getRootUrl();
        JobState           jobState     = new JobState();
        BuildState buildState   = new BuildState();
        ScmState scmState     = new ScmState();
        Result             result       = run.getResult();
        ParametersAction   paramsAction = run.getAction(ParametersAction.class);
        EnvVars            environment  = run.getEnvironment( listener );
        StringBuilder      log          = this.getLog(run, target);

        jobState.setName( job.getName());
        jobState.setDisplayName(job.getDisplayName());
        jobState.setUrl( job.getUrl());
        jobState.setBuild( buildState );

        buildState.setNumber( run.number );
        buildState.setQueueId( run.getQueueId() );
        buildState.setUrl( run.getUrl());
        buildState.setPhase( this );
        buildState.setTimestamp( timestamp );
        buildState.setScm( scmState );
        buildState.setLog( log );

        if ( result != null ) {
            buildState.setStatus(result.toString());
        }

        if ( rootUrl != null ) {
            buildState.setFullUrl(rootUrl + run.getUrl());
        }

        buildState.updateArtifacts( job, run );

        if ( paramsAction != null ) {
            EnvVars env = new EnvVars();
            for (ParameterValue value : paramsAction.getParameters()){
                if ( ! value.isSensitive()) {
                    value.buildEnvironment( run, env );
                }
            }
            buildState.setParameters(env);
        }

        if ( environment.get( "GIT_URL" ) != null ) {
            scmState.setUrl( environment.get( "GIT_URL" ));
        }

        if ( environment.get( "GIT_BRANCH" ) != null ) {
            scmState.setBranch( environment.get( "GIT_BRANCH" ));
        }

        if ( environment.get( "GIT_COMMIT" ) != null ) {
            scmState.setCommit( environment.get( "GIT_COMMIT" ));
        }

        return jobState;
    }

    private StringBuilder getLog(Run run, Endpoint target) {
        StringBuilder log = new StringBuilder("");
        Integer loglines = 0;

        if (loglines == null || loglines == 0) {
            return log;
        }

        try {
            switch (loglines) {
                // The full log
                case -1:
                    log.append(run.getLog());
                    break;
                default:
                    List<String> logEntries = run.getLog(loglines);
                    for (String entry: logEntries) {
                        log.append(entry);
                        log.append("\n");
                    }
            }
        } catch (IOException e) {
            log.append("Unable to retrieve log");
        }
        return log;
    }
}
