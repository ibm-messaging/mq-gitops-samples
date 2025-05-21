ARG mqVersion="9.4.2.0-r1"

ARG mqSrcImage="image-registry.openshift-image-registry.svc:5000/mq-pipeline/mq-with-metrics"

FROM ${mqSrcImage}:${mqVersion}

USER 0

RUN mkdir /opt/mqm/myexits

COPY queue-manager-custom-otel/mqtracingexit.conf /opt/mqm/myexits/mqtracingexit.conf

COPY mq-exit/mqtracingexit_r /opt/mqm/myexits/mqtracingexit_r
COPY mq-exit/tracelibrary.so /opt/mqm/myexits/tracelibrary.so

RUN chmod 770 /opt/mqm/myexits/*

USER 1001
