#!/bin/bash
set -xe
docker build -t opsera-code-deployer:latest  ../../
docker run --rm \
        440953937617.dkr.ecr.us-east-2.amazonaws.com/kubectl \
        aws ecr get-login-password \
        --region us-east-2 \
        | docker login --username AWS \
        --password-stdin 440953937617.dkr.ecr.us-east-2.amazonaws.com

date_tag=`date +%-d-%m-%Y-%T | sed 's/:/-/g'`
docker tag opsera-code-deployer:latest 440953937617.dkr.ecr.us-east-2.amazonaws.com/opsera-code-deployer:latest
docker tag opsera-code-deployer:latest 440953937617.dkr.ecr.us-east-2.amazonaws.com/opsera-code-deployer:kube-${date_tag}

docker push 440953937617.dkr.ecr.us-east-2.amazonaws.com/opsera-code-deployer:latest
docker push 440953937617.dkr.ecr.us-east-2.amazonaws.com/opsera-code-deployer:kube-${date_tag}


