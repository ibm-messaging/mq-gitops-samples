This is a work in progress/rough sample.  

It clones this repo which has a simple Dockerfile that will build an image with an MQ Exit taken from a workspace that contains the exit code.  

Step 1 - clone this repo.  

Step 2 - copy the exit binaries from a source host, this version grabs a previously downloaded zipped tar file and copies the contents to the temporary workspace.

Step 3 - use buildah to run the Dockerfile and copy the binaries into the new image along with the exit's configuration file.  
