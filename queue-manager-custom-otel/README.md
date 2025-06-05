Note: This is a work in progress/rough sample.  



The pipeline clones this repo which has a simple Dockerfile, the Dockerfile will build an image with an MQ Exit taken from a workspace that contains the exit code. The 'from' image can be any IBM MQ image that you have built already or one straight from icr.io/ibm-messaging/mq.  

Step 1 - clone this repo.  

Step 2 - copy the exit binaries from a source host, this version grabs a previously downloaded zipped tar file and copies the contents to the temporary workspace.

Step 3 - use buildah to run the Dockerfile and copy the binaries into the new image along with the exit's configuration file.  

How to run:  

Create or reuse a persistent volume, theres an example in the custom-metrics folder.  
Add the Task to your OpenShift cluster.  
Add the Pipeline.  

Parameters are, the workspace PVC, credentials Secret to insert the new image into a local repository, hostname for the source of the zip file.  
