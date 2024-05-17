Dependencies

IBM MQ Operator
Certificate Manager

Usage

Clone this repository, change the passwords in the qmdemo-qm.yaml file and then Producer.java and Consumer.java files to a password of your choice, currently set to 'newpassword'  
Log into you OpenShft cluster and create a project called mq-demo  
  
oc create -f qmdemo-cert.yaml  
oc create -f qmdemo-qm.yaml  
  
Deploy the producer and consumer apps,  

oc new-app registry.redhat.io/redhat-openjdk-18/openjdk18-openshift~https://github.com/ibm-messaging/mq-gitops-samples#basic-deployment --context-dir=/queue-manager-basic-deployment/code/qmdemo-producer --env='JAVA_APP_JAR=producer-1.0-SNAPSHOT-jar-with-dependencies.jar' --name=mq-producer  
  
oc new-app registry.redhat.io/redhat-openjdk-18/openjdk18-openshift~https://github.com/ibm-messaging/mq-gitops-samples#basic-deployment --context-dir=/queue-manager-basic-deployment/code/qmdemo-consumer --env='JAVA_APP_JAR=producer-1.0-SNAPSHOT-jar-with-dependencies.jar' --name=mq-consumer  
