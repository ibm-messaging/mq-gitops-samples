# Using a web server to serve JSON CCDT files on Red Hat OpenShift

This folder contains a sample ArgoCD application that can be used to deploy an Nginx web server to serve JSON CCDT files, alternatively you can just deploy the YAML samples in this order using **oc apply -f** :

ccdt-mq-routes-configmap.yaml  
ccdt-mq-services-configmap.yaml  
ccdt-server-deployment.yaml  
ccdt-service.yaml  
ccdt-route.yaml

Files explained,

ccdt-mq-routes-configmap.yaml  
cdt-mq-services-configmap.yaml

These files contain the CCDT information in JSON format, the Nginx deployment will mount them into the pod/container. You will need to edit these files to contain the services and routes for your cluster and MQ deployment. The MQ Routes used in the CCDT are created by the MQ Operator and can be used internally or externally but you must use the SNI approach in your MQ Client with these. The mq-services CCDT uses the Kubernetes Service to locate a queue manager and can be used with TLS or non-TLS client connections, the service can be used across namespaces if you include the full service URL.

ccdt-server-deployment.yaml

This file contains the Pod deployment for an Nginx web server that will host the CCDT files. The internal CCDT (mq-services) is in the root of the file system, the external (mq-routes) CCDT is under /external. You can increase the number of Pods for high availability within your cluster. Note that the un-privileged Nginx container image uses port 8080, previously is used port 80 by default.

ccdt-service.yaml

This file creates a Kubernetes Service and therefore an HTTP URL that will route to the Nginx Pods. e.g. "http://ccdt-service.mq-demo.svc.cluster.local:8080/ccdt.json" where mq-demo is the namespace that the web server has been deployed to.

ccdt-route.yaml

Creates an OpenShift Route that will allow lookups of the CCDT from outside or inside the cluster.

## Deployment overview

This diagram gives a high-level overview of the deployment and topology.

![alt text](https://github.com/ibm-messaging/mq-gitops-samples/blob/main/ccdt-deployment/images/nginx-deploy.png)


# Disclaimer  
All samples in this repository are provided AS-IS without warranty of any kind, express or implied.  IBM shall not be responsible for any damages arising out of the use of, or otherwise related to, these samples.

The content provided in this repository is for informational purposes only. The opinions and insights discussed are those of the author and do not necessarily represent those of the IBM Corporation.

Nothing contained in these materials or the products discussed is intended to, nor shall have the effect of, creating any warranties or representations from IBM or its suppliers, or altering the terms and conditions of any agreement you have with IBM.

The information presented is not intended to imply that any actions taken by you will result in any specific result or benefit and should not be relied on in making a purchasing decision. IBM does not warrant that any systems, products or services are immune from, or will make your enterprise immune from, the malicious or illegal contact of any party.