# GitOps - JSON CCDT web server on OpenShift deployment sample

This folder contains an ArgoCD application that can be used to deploy an nginx web server that will serve a JSON ccdt file, alternatively you can deploy the YAML samples in this order:

ccdt-mq-routes-configmap.yaml  
ccdt-mq-services-configmap.yaml  
ccdt-server-deployment.yaml  
ccdt-service.yaml  
ccdt-route.yaml  

Files explained,

ccdt-mq-routes-configmap.yaml
cdt-mq-services-configmap.yaml

These files contain the CCDT file in JSON format, the nginx deployment will mount them into the container.

ccdt-server-deployment.yaml

Contains the Pod deployment for an nginx web server that will host the CCDT files.

ccdt-service.yaml

Creates a Kubernetes Service that will route to the nginx Pods. e.g. "http://ccdt-service.mq-demo.svc.cluster.local:8080/ccdt.json"

ccdt-route.yaml

Creates an OpenShift Route that will allow lookups from outside the cluster.


## Deployment Overview

![alt text](https://github.com/ibm-messaging/mq-gitops-samples/blob/main/ccdt-deployment/images/nginx-deploy.png)


# Disclaimer  
All samples in this repository are provided AS-IS without warranty of any kind, express or implied.  IBM shall not be responsible for any damages arising out of the use of, or otherwise related to, these samples.

The content provided in this repository is for informational purposes only. The opinions and insights discussed are those of the author and do not necessarily represent those of the IBM Corporation.

Nothing contained in these materials or the products discussed is intended to, nor shall have the effect of, creating any warranties or representations from IBM or its suppliers, or altering the terms and conditions of any agreement you have with IBM.

The information presented is not intended to imply that any actions taken by you will result in any specific result or benefit and should not be relied on in making a purchasing decision. IBM does not warrant that any systems, products or services are immune from, or will make your enterprise immune from, the malicious or illegal contact of any party.

