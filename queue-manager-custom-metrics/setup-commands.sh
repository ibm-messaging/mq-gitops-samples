#!/bin/bash

# create secrets for the image registries
oc create secret docker-registry my-docker-registry-creds --docker-server=docker.io --docker-username=username --docker-password=password --docker-email=email
oc secrets link pipeline my-docker-registry-creds

# create credentials for the buildah task for pushing to a registry
oc create secret docker-registry internal-registry-creds --docker-server=image-registry.openshift-image-registry.svc:5000 --docker-username=`oc whoami` --docker-password=`oc whoami -t` --docker-email=email

# run using a template
oc create -f pipeline-run-template.yaml
oc process mq-metrics-pipeline-run-template --param=runNumber=02 | oc create -f -
