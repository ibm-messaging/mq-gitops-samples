ARG sourceVersion="9.4.2.0-r1"

ARG sourceImage="image-registry.openshift-image-registry.svc:5000/mq-pipeline/mq-with-metrics"

FROM ${sourceImage}:${sourceVersion}

USER 0

COPY queue-manager-custom-otel/mqtracingexit.conf /var/mqm/exits64/

COPY /workspace/mq-exit/mqtracingexit_r /var/mqm/exits64/

RUN chmod 770 /var/mqm/exits64/*

USER 1001
