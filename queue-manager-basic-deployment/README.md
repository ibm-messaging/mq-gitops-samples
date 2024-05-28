# Basic Queue Manager Deployment

This repository contains samples that can be used to deploy a basic (single instance - standalone, no HA or MQ Cluster) queue manager running on Red Hat OpenShift, the samples use the IBM MQ Developer edition which does not require an IBM entitlement key. The repository also contains sample producer and consumer Java applications that can be built using Source to Image (S2I) and used to demonstrate sending and receiving messages to a queue. All the necessary components for the producer and consumer, queues, authority records etc. should be built for you. In addition to the queue manager, the sample will setup the IBM MQ Console, the MQ Operator will create a Route/URL for the Console UI which you can find by selecting the QMDEMO queue manager under the MQ Operator UI.


> [!IMPORTANT]  
> The sample uses a persistent volume, but it will be deleted upon removal of the queue manager custom resource. Do not use this sample as a template for a production deployment its intended use is for demonstration purposes.  

> [!WARNING]  
> The sample passwords secret should not be used as-is and you should not store passwords in repositories in the clear, especially in public Git repositories, the passwords used in this repository are there solely to show the format of the YAML files.  The samples use 'newpassword' for the 'admin' user in the MQ Console and for the 'app' user in the Java samples.

## Dependencies

Ensure you have setup the following resources on your OpenShift cluster:  

- A default storage class that provides block storage.  
- Certificate Manager with a Cluster Issuer, self-signed is fine for test/demonstration purposes.  
- The IBM MQ Operator.  

Example self-signed ClusterIssuer:  

```
apiVersion: cert-manager.io/v1
kind: ClusterIssuer
metadata:
  name: selfsigned-issuer
spec:
  selfSigned: {}
```

## Usage

- Fork or clone this repository.
- Change the passwords in these files: qmdemo-passwords-secret.yaml, Producer.java and Consumer.java to a password of your choice, they are currently set to 'newpassword'. Alternatively use the commandline to create a secret and don't apply the qmdemo-passwords-secret.yaml, you will still need to change the Java programs to match, ensure the passwords in the Java code are not placed in a publicly accessible place.  
```
oc create secret generic qmdemo-passwords --from-literal=dev-admin-password=newpassword --from-literal=dev-app-password=newpassword
```
- Log into you OpenShft cluster and create a project called mq-demo.  
- Apply the YAML files, skip qmdemo-passwords-secret.yaml if you created the secret via the commandline.  
  
```
oc apply -f qmdemo-mqsc-config-map.yaml  
oc apply -f qmdemo-cert.yaml  
oc apply -f qmdemo-passwords-secret.yaml <skip if you created a secret using the commandline?
oc apply -f qmdemo-qm.yaml  
```
or  
```
source openshift-commands.txt
```  

Deploy the producer and consumer Java applications.  

> [!IMPORTANT]
> Change the passwords in Producer.java and Consumer.java to match what you have in the Secret qmdemo-passwords-secret and ensure you store the code securely.

> [!CAUTION]
> The samples below use this repository that has the publicaly accessible passwords.  

```
oc new-app registry.redhat.io/redhat-openjdk-18/openjdk18-openshift~https://github.com/ibm-messaging/mq-gitops-samples#main --context-dir=/queue-manager-basic-deployment/code/qmdemo-producer --env='JAVA_APP_JAR=producer-1.0-SNAPSHOT-jar-with-dependencies.jar' --name=mq-producer  
  
oc new-app registry.redhat.io/redhat-openjdk-18/openjdk18-openshift~https://github.com/ibm-messaging/mq-gitops-samples#main --context-dir=/queue-manager-basic-deployment/code/qmdemo-consumer --env='JAVA_APP_JAR=consumer-1.0-SNAPSHOT-jar-with-dependencies.jar' --name=mq-consumer  
```
