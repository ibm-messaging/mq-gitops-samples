ARG mqVersion="9.4.2.0-r1"

ARG mqSrcImage="image-registry.openshift-image-registry.svc:5000/mq-pipeline/mq-with-metrics"

FROM ${mqSrcImage}:${mqVersion}

USER 0

COPY queue-manager-custom-otel/mqtracingexit.conf /opt/mqm/myexits

COPY mq-exit/mqtracingexit_r /opt/mqm/myexits
COPY mq-exit/tracelibrary.so /opt/mqm/myexits

RUN chmod 770 /opt/mqm/myexits*

USER 1001
