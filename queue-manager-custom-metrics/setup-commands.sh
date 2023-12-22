#!/bin/bash
oc create secret docker-registry my-docker-registry-creds --docker-server=docker.io --docker-username=username --docker-password=password --docker-email=email
oc secrets link pipeline my-docker-registry-creds
oc create secret docker-registry internal-registry-creds --docker-server=image-registry.openshift-image-registry.svc:5000 --docker-username=kubeadmin --docker-password=`oc whoami -t` --docker-email=email
oc create -f pipeline-run-template.yaml
oc process mq-metrics-pipeline-run-template --param=runNumber=02 | oc appy -f
