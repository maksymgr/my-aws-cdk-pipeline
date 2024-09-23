package com.myorg;

import software.amazon.awscdk.*;
import software.amazon.awscdk.pipelines.*;
import software.constructs.Construct;

import java.util.Arrays;
// import software.amazon.awscdk.Duration;
// import software.amazon.awscdk.services.sqs.Queue;

public class MyPipelineStack extends Stack {
    public MyPipelineStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public MyPipelineStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        var githubAccessToken = SecretValue.secretsManager("github-token2");

        CodePipeline pipeline = CodePipeline.Builder.create(this, "pipeline")
                .pipelineName("MyPipeline")
                .synth(ShellStep.Builder.create("Synth")
                        .input(
                                CodePipelineSource.gitHub("maksymgr/my-aws-cdk-pipeline", "main",
                                        GitHubSourceOptions.builder()
                                                .authentication(githubAccessToken)
                                                .build())
                        )
                        .commands(Arrays.asList("npm install -g aws-cdk", "cdk synth"))
                        .build())
                .build();

        StageDeployment testingStage =
                pipeline.addStage(new MyPipelineAppStage(this, "test", StageProps.builder()
                        .env(Environment.builder()
                                .account("211125707905")
                                .region("us-west-1")
                                .build())
                        .build()));

        testingStage.addPost(new ManualApprovalStep("approval"));
    }
}
