import groovy.transform.Synchronized

class JenkinsParallelExecutor {
    public static void main(String[] args) {
        def agents = ['AT-1', 'AT-2', 'AT-3', 'AT-4']
        def browsers = ["Chrome", "Firefox", "Internet_Explorer"]

        ParallelExecutionRules rules = new ParallelExecutionRules();
        rules.doNotAllowToExecuteTheSameJobNameOnDifferentBrowsers = true
        rules.doNotAllowToExecuteMoreThanOneIESessionOnTheSameMachine = true
        rules.jobsDependencies.add(new JobsDependency("Positive tests/05. Collateral", "Positive tests/02. Customer"))

        def jobNames = [
                '01. Positive tests/01. Authorization/All tests',
                '01. Positive tests/02. Customer/All tests',
                '01. Positive tests/03. Account/All tests',
                '01. Positive tests/04. Transfer/All tests',
                '01. Positive tests/05. Collateral/All tests',
                '01. Positive tests/06. Limit/All tests',
                '01. Positive tests/07. Loan Contract/All tests',
                '01. Positive tests/08. Clearing/All tests',
                '01. Positive tests/09. User for T24/All tests',

                '02. Negative tests/01. Customer/All tests',
                '02. Negative tests/02. Account/All tests',
                '02. Negative tests/03. Transfer/All tests',
                '02. Negative tests/04. Collateral/All tests',
                '02. Negative tests/05. Limit/All tests',
                '02. Negative tests/06. Loan/All tests',
                '02. Negative tests/07. Clearing/All tests'
        ]

        println jobNames

        //Collect all combinations of job names + browsers
        def jobs = []
        for(String browser : browsers){
            for(String jobName : jobNames){
                jobs.add new Job(jobName, browser, "")
            }
        }

        for(String agent : agents){
            JobUtils.startAgent(jobs, rules, agent)
        }
    }
}

class JobUtils{
    static void startAgent(ArrayList<Job> jobs, ParallelExecutionRules rules, String agent){
        Thread thread = new Thread(new Runnable() {
            @Override
            void run() {
                println "[" + new Date().getTimeString() + "][" + agent + "] agent is started"
                boolean continueSearch = true
                while (continueSearch){
                    JobSearchResult jobSearchResult = getNextJobToExecute(jobs, rules, agent)
                    if(jobSearchResult.searchStatus.equals(JobSearchResult.SearchStatus.JOB_IS_FOUND)){
                        jobSearchResult.job.status = Job.Status.STARTED
                        build(jobSearchResult.job.name, jobSearchResult.job.browser, agent, false)
                        jobSearchResult.job.status = Job.Status.FINISHED
                    } else if (jobSearchResult.searchStatus.equals(JobSearchResult.SearchStatus.ALL_AVAILABLE_JOBS_BLOCKED)) {
                        //Option 1:
//                        println "[" + new Date().getTimeString() + "][" + agent + "] all non-executed jobs are blocked by parallel execution rules (will wait and try again later)"
//                        sleep(5000)

                        //Option 2:
                        continueSearch = false
                        println "[" + new Date().getTimeString() + "][" + agent + "] all non-executed jobs are blocked by parallel execution rules (agent is stopped)"
                    } else {
                        continueSearch = false
                        println "[" + new Date().getTimeString() + "][" + agent + "] no jobs left to execute (agent is stopped)"
                    }
                }
            }
        })
        thread.start()
    }

    @Synchronized
    static JobSearchResult getNextJobToExecute(ArrayList<Job> jobs, ParallelExecutionRules rules, String agent){
        JobSearchResult jobSearchResult = new JobSearchResult(null, JobSearchResult.SearchStatus.NO_JOBS_LEFT)
        for (Job job : jobs){
            if(job.executingAgent.equals("")){
                //checking all rules (to find out if this job is allowed to be executed right now in this browser and on this agent)
                if(rules.doNotAllowToExecuteTheSameJobNameOnDifferentBrowsers && jobWithTheSameNameIsRunningInAnotherBrowser(job.name, jobs)){
                    jobSearchResult.searchStatus = JobSearchResult.SearchStatus.ALL_AVAILABLE_JOBS_BLOCKED
//                    println "[" + new Date().getTimeString() + "][" + agent + "] Job [" + job.name + "] cannot be executed in the browser [" + job.browser + "] because job with the same name is already running in another browser (parallel execution rules does not allow to start this job)"
                    continue
                }
                if(jobExecutionIsBlockedByJobsDependencies(job.name, job.browser, agent, jobs, rules.jobsDependencies)){
                    jobSearchResult.searchStatus = JobSearchResult.SearchStatus.ALL_AVAILABLE_JOBS_BLOCKED
                    println "[" + new Date().getTimeString() + "][" + agent + "] Job [" + job.name + "] cannot be executed in the browser '" + job.browser + "' on the agent '" + agent + "' because job dependencies have not been met yet (parallel execution rules does not allow to start this job)"
                    continue
                }

                //no parallel execution rules are broken
                job.executingAgent = agent
                job.status = Job.Status.TAKEN
                jobSearchResult.searchStatus = JobSearchResult.SearchStatus.JOB_IS_FOUND
                jobSearchResult.job = job
                println "[" + new Date().getTimeString() + "][" + agent + "] Job [" + job.name + "] has been picked (browser = " + job.browser + ")"
                break
            }
        }
        return jobSearchResult
    }

    static build(String job, String browser, String agent, boolean propagate){
        //random sleep time (1-9 seconds)
        sleep((System.currentTimeMillis() % 10) * 1000)
        println "[" + new Date().getTimeString() + "][" + agent + "] Job [" + job + "] is finished (browser = " + browser + ")"
    }


    static boolean jobWithTheSameNameIsRunningInAnotherBrowser(String jobNameToCheck, ArrayList<Job> jobs){
        for(Job job : jobs){
            if(job.name.equals(jobNameToCheck) && (job.status.equals(Job.Status.TAKEN) || job.status.equals(Job.Status.STARTED))){
                return true
            }
        }
        return false
    }

    static boolean jobExecutionIsBlockedByJobsDependencies(String jobName, String jobBrowser, String agent, ArrayList<Job> jobs,  ArrayList<JobsDependency> jobsDependencies){
        for(JobsDependency jobsDependency : jobsDependencies){
            //Block child job if parent job is not finished OR if parent job has not been executed by the same agent
            if(jobName.contains(jobsDependency.jobName_Contains)){
                for(Job parentJob : jobs){
                    if(parentJob.name.contains(jobsDependency.dependsOnJobName_Contains) && parentJob.browser.equals(jobBrowser)){
                        if(!parentJob.status.equals(Job.Status.FINISHED) || !parentJob.executingAgent.equals(agent)){
                            return true
                        }
                    }
                }
            }
            //Block parent job if the same job has been finished on another browser, but child job has not been finished on this browser yet
            if(jobName.contains(jobsDependency.dependsOnJobName_Contains)){
                for(Job job : jobs){
                    if(job.name.contains(jobsDependency.dependsOnJobName_Contains) && !job.browser.equals(jobBrowser)){
                        if(job.status.equals(Job.Status.FINISHED)) {
                            String browserOfFinishedParentJob = job.browser
                            for(Job childJob : jobs){
                                if(childJob.name.contains(jobsDependency.jobName_Contains) && childJob.browser.equals(browserOfFinishedParentJob)){
                                    if(!childJob.status.equals(Job.Status.FINISHED) || !childJob.executingAgent.equals(agent)){
                                        return true
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false
    }
}

class ParallelExecutionRules {
    boolean doNotAllowToExecuteTheSameJobNameOnDifferentBrowsers;
    boolean doNotAllowToExecuteMoreThanOneIESessionOnTheSameMachine;
    ArrayList<JobsDependency> jobsDependencies = new ArrayList<>();
}

class JobSearchResult{
    Job job
    SearchStatus searchStatus

    JobSearchResult(Job job, SearchStatus searchStatus){
        this.job = job
        this.searchStatus = searchStatus
    }

    enum SearchStatus{JOB_IS_FOUND, NO_JOBS_LEFT, ALL_AVAILABLE_JOBS_BLOCKED}
}

class Job {
    String name
    String browser
    String executingAgent
    Status status

    public Job(String name, String browser, String executingAgent){
        this.name = name
        this.browser = browser
        this.executingAgent = executingAgent
        this.status = Status.NOT_TAKEN
    }

    public Job(Status status){
        this.status = status
    }

    enum Status{NOT_TAKEN, TAKEN, STARTED, FINISHED}
}

class JobsDependency{
    String jobName_Contains
    String dependsOnJobName_Contains

    JobsDependency(String jobName_Contains, String dependsOnJobName_Contains){
        this.jobName_Contains = jobName_Contains
        this.dependsOnJobName_Contains = dependsOnJobName_Contains
    }
}


class Tmp{
    public void Run() {
        def agent1 = 'AT-153-1'
        def agent2 = 'AT-105-2'
        def agent3 = 'AT-153-2'
        def agent4 = 'AT-105-1'

        def browsers = ["Chrome", "Firefox", "Internet_Explorer"]
        def agents = []

//        stage('Tests') = {
            def executionTimeList = []
            def start = System.currentTimeMillis();
            def tasks = [
                    agent1: {
                        def agent = agent1
                        agents.add(agent)
                        for (String browser : browsers) {
                            build job: '01. Positive tests/01. Authorization/All tests', parameters: [string(name: 'BROWSER', value: browser), string(name: 'NODE', value: agent)], propagate: false
                            build job: '01. Positive tests/02. Customer/All tests', parameters: [string(name: 'BROWSER', value: browser), string(name: 'NODE', value: agent)], propagate: false
                            build job: '01. Positive tests/05. Collateral/All tests', parameters: [string(name: 'BROWSER', value: browser), string(name: 'NODE', value: agent)], propagate: false
                            build job: '01. Positive tests/06. Limit/All tests', parameters: [string(name: 'BROWSER', value: browser), string(name: 'NODE', value: agent)], propagate: false
                            build job: '01. Positive tests/07. Loan Contract/All tests', parameters: [string(name: 'BROWSER', value: browser), string(name: 'NODE', value: agent)], propagate: false
                        }
                        executionTimeList.add('[' + agent + ']: ' + (int) ((System.currentTimeMillis() - start) / 60000) + ' min, ' + (int) (((System.currentTimeMillis() - start) % 60000) / 1000) + ' sec')
                    },
                    agent2: {
                        def agent = agent2
                        agents.add(agent)
                        for (String browser : browsers) {
                            build job: '01. Positive tests/03. Account/All tests', parameters: [string(name: 'BROWSER', value: browser), string(name: 'NODE', value: agent)], propagate: false
                            build job: '01. Positive tests/04. Transfer/All tests', parameters: [string(name: 'BROWSER', value: browser), string(name: 'NODE', value: agent)], propagate: false
                            build job: '01. Positive tests/08. Clearing/All tests', parameters: [string(name: 'BROWSER', value: browser), string(name: 'NODE', value: agent)], propagate: false
                            build job: '01. Positive tests/09. User for T24/All tests', parameters: [string(name: 'BROWSER', value: browser), string(name: 'NODE', value: agent)], propagate: false

                            build job: '02. Negative tests/01. Customer/All tests', parameters: [string(name: 'BROWSER', value: browser), string(name: 'NODE', value: agent)], propagate: false
                            build job: '02. Negative tests/02. Account/All tests', parameters: [string(name: 'BROWSER', value: browser), string(name: 'NODE', value: agent)], propagate: false
                            build job: '02. Negative tests/03. Transfer/All tests', parameters: [string(name: 'BROWSER', value: browser), string(name: 'NODE', value: agent)], propagate: false
                            build job: '02. Negative tests/04. Collateral/All tests', parameters: [string(name: 'BROWSER', value: browser), string(name: 'NODE', value: agent)], propagate: false
                            build job: '02. Negative tests/05. Limit/All tests', parameters: [string(name: 'BROWSER', value: browser), string(name: 'NODE', value: agent)], propagate: false
                            build job: '02. Negative tests/06. Loan/All tests', parameters: [string(name: 'BROWSER', value: browser), string(name: 'NODE', value: agent)], propagate: false
                            build job: '02. Negative tests/07. Clearing/All tests', parameters: [string(name: 'BROWSER', value: browser), string(name: 'NODE', value: agent)], propagate: false
                        }
                        executionTimeList.add('[' + agent + ']: ' + (int) ((System.currentTimeMillis() - start) / 60000) + ' min, ' + (int) (((System.currentTimeMillis() - start) % 60000) / 1000) + ' sec')
                    }

            ]

            parallel tasks

            println '--------------------------------------------------------'
            println 'Agents execution time:'
            for (String executionTime : executionTimeList) {
                println executionTime
            }
//        }

//        stage('Allure Report') {
            build job: 'Allure Report (aggregated)', parameters: [string(name: 'NODES', value: agents.join(","))], propagate: false
//        }
    }

}