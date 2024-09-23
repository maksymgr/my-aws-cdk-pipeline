package com.myorg;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

import java.util.Arrays;

public class MyPipelineApp {
    public static void main(final String[] args) {
        App app = new App();

        new MyPipelineStack(app, "MyPipelineStack", StackProps.builder()
                .env(Environment.builder()
                        .account("211125707905")
                        .region("us-west-1")
                        .build())
                .build());

        app.synth();
    }
}

