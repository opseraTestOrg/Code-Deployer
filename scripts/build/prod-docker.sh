#!/bin/bash
set -xe
docker build -t opsera-apigateway:kube-prod  ../../
docker run --rm \
        440953937617.dkr.ecr.us-east-2.amazonaws.com/kubectl \
        aws ecr get-login-password \
        --region us-east-2 \
        | docker login --username AWS \
        --password-stdin 440953937617.dkr.ecr.us-east-2.amazonaws.com

date_tag=`date +%-d-%m-%Y-%T | sed 's/:/-/g'`
docker tag opsera-apigateway:kube-prod 440953937617.dkr.ecr.us-east-2.amazonaws.com/apigateway:kube-prod
docker tag opsera-apigatewayr:kube-prod 440953937617.dkr.ecr.us-east-2.amazonaws.com/apigateway:kube-prod-${date_tag}

docker push 440953937617.dkr.ecr.us-east-2.amazonaws.com/apigateway:kube-prod
docker push 440953937617.dkr.ecr.us-east-2.amazonaws.com/apigateway:kube-prod-${date_tag}
