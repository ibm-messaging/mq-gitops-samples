# Basic Queue Manager Deployment (Developer edition)

This repository contains samples that can be used to deploy a single instance (no High Availability or MQ Clustering) queue manager running on Red Hat OpenShift, the samples use the IBM MQ Developer edition which does not require an IBM entitlement key and will setup some default objects and access. The repository also contains sample producer and consumer Java applications that can be built using Source to Image (S2I), these samples can be used to demonstrate sending and receiving messages to a queue. All the necessary components for the producer and consumer, queues, authority records etc. should be built for you. In addition to the queue manager, the sample will setup the IBM MQ Console, the MQ Operator will create a Route/URL for the Console UI which you can find by selecting the QMDEMO queue manager under the MQ Operator in the OpenShift console.  
 
  
> [!WARNING]  
> These samples have passwords in the queue manager YAML file and the Java source code files, these should not be used as-is and you should not store passwords in repositories in the clear, especially in public Git repositories. The samples use 'newpassword' for the 'admin' user to be used in the MQ Console and for the 'app' user in the Java samples. Change the password when you add the queue manager YAML to your OpenShift cluster and then change the passwords in the Java code to match. The passwords used in this repository are there solely to show the format of the YAML files and Java code.  
>
> The sample queue manager uses an ephemeral (temporary) storage volume, it will be deleted upon removal of the queue manager custom resource. Do not use this sample as a template for a production deployment it is intended use is for demonstration purposes only.  

> [!IMPORTANT]
> ## Dependencies
> Ensure you have setup the following resources on your OpenShift cluster 
>
<details>
<summary>The IBM MQ Operator</summary>  
  
&NewLine;

**1.** Use the YAML below to add the IBM Operator catalog, full details can be found here,  
https://www.ibm.com/docs/en/SSFKSJ_9.3.0/container/ctr-add-catalog-source.html

&NewLine;
  
```yaml
apiVersion: operators.coreos.com/v1alpha1
kind: CatalogSource
metadata:
  name: ibm-operator-catalog
  namespace: openshift-marketplace
spec:
  displayName: IBM Operator Catalog
  image: icr.io/cpopen/ibm-operator-catalog:latest
  publisher: IBM
  sourceType: grpc
  updateStrategy:
    registryPoll:
      interval: 45m
```

&NewLine;

**2.** When you have installed the catalog source go to the OpenShift OperatorHub and search for 'IBM MQ', select the IBM MQ tile and accept the defaults to install the operator.

</details>

<details>

<summary>Certificate Manager</summary>

Install the certificate manager by serching for 'certificate-manager' in the OperatorHub, take the defaults and then create a Cluster Issuer once the operator is installed, a self-signed is fine for test/demonstration purposes.  

Example self-signed ClusterIssuer YAML 

```yaml
apiVersion: cert-manager.io/v1
kind: ClusterIssuer
metadata:
  name: selfsigned-issuer
spec:
  selfSigned: {}
```

</details>

## Usage

1. Log into you OpenShft cluster and create a project called 'mq-demo'. 

2. Using a terminal window create a secret for the MQ Console and the application user.  

```
oc create secret generic qmdemo-passwords --from-literal=dev-admin-password=change-this-password --from-literal=dev-app-password=change-this-password
```

3. Apply the YAML files in the order below, add them to your cluster through the OpenShift UI or use the commandline e.g.
  
```
oc apply -f qmdemo-mqsc-config-map.yaml  
oc apply -f qmdemo-cert.yaml  
oc apply -f qmdemo-qm.yaml  
```

You should now have a running queue manager.  

### Optional - deploy the sample applications

1. Use a terminal window to deploy the producer and consumer Java applications, make sure you change the value of MQ_APP_PASSWORD to be the same as what you set when creating the secret in step 2.

```
oc new-app registry.redhat.io/redhat-openjdk-18/openjdk18-openshift~https://github.com/ibm-messaging/mq-gitops-samples#main --context-dir=/queue-manager-basic-deployment/code/qmdemo-producer --env='JAVA_APP_JAR=producer-1.0-SNAPSHOT-jar-with-dependencies.jar' --env="MQ_APP_PASSWORD=change-this-password" --name=mq-producer -n mq-demo  
  
oc new-app registry.redhat.io/redhat-openjdk-18/openjdk18-openshift~https://github.com/ibm-messaging/mq-gitops-samples#main --context-dir=/queue-manager-basic-deployment/code/qmdemo-consumer --env='JAVA_APP_JAR=consumer-1.0-SNAPSHOT-jar-with-dependencies.jar' --env="MQ_APP_PASSWORD=change-this-password" --name=mq-consumer -n mq-demo  
```

