# Basic Queue Manager Deployment

This repository contains samples that can be used to deploy a single instance (no High Availability or MQ Clustering) queue manager running on Red Hat OpenShift, the samples use the IBM MQ Developer edition which does not require an IBM entitlement key. The repository also contains sample producer and consumer Java applications that can be built using Source to Image (S2I), these samples can be used to demonstrate sending and receiving messages to a queue. All the necessary components for the producer and consumer, queues, authority records etc. should be built for you. In addition to the queue manager, the sample will setup the IBM MQ Console, the MQ Operator will create a Route/URL for the Console UI which you can find by selecting the QMDEMO queue manager under the MQ Operator in the OpenShift console.  
 
  
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
2. Apply the YAML files, in the following order, by adding them to your cluster through the OpenShift UI or use the commandline e.g.
  
```
oc apply -f qmdemo-mqsc-config-map.yaml  
oc apply -f qmdemo-cert.yaml  
oc apply -f qmdemo-qm.yaml  
```

You should now have a running queue manager.  

### Optional

3. Clone or fork this repository if you want to deploy the sample producer and consumer Java applications, you need to take a copy if you are changing the passwords (strongly recommended).

4. Change the passwords in these files: Producer.java and Consumer.java to the same password you set in the queue manager YAML, they are set to 'newpassword' by default. Ensure the passwords in the Java code are not placed in a publicly accessible place.  


5. Deploy the producer and consumer Java applications, you will need to replace this repository with the location of your files if you have changed the passwords.  

```
oc new-app registry.redhat.io/redhat-openjdk-18/openjdk18-openshift~https://github.com/ibm-messaging/mq-gitops-samples#main --context-dir=/queue-manager-basic-deployment/code/qmdemo-producer --env='JAVA_APP_JAR=producer-1.0-SNAPSHOT-jar-with-dependencies.jar' --name=mq-producer -n mq-demo  
  
oc new-app registry.redhat.io/redhat-openjdk-18/openjdk18-openshift~https://github.com/ibm-messaging/mq-gitops-samples#main --context-dir=/queue-manager-basic-deployment/code/qmdemo-consumer --env='JAVA_APP_JAR=consumer-1.0-SNAPSHOT-jar-with-dependencies.jar' --name=mq-consumer -n mq-demo  
```

