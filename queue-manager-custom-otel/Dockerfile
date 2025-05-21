ARG mqVersion="9.4.2.0-r1"

ARG mqSrcImage="image-registry.openshift-image-registry.svc:5000/mq-pipeline/mq-with-metrics"

FROM ${mqSrcImage}:${mqVersion}

USER 0

COPY queue-manager-custom-otel/mqtracingexit.conf /var/mqm/myexits/Installation1

COPY mq-exit/mqtracingexit_r /var/mqm/myexits
COPY mq-exit/tracelibrary.so /var/mqm/myexits

RUN chmod 770 /var/mqm/myexits/*

USER 1001
