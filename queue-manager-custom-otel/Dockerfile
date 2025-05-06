ARG mqVersion="9.4.0.0-r3"

ARG internalRegistry="image-registry.openshift-image-registry.svc:5000/mq-pipeline"

FROM ${internalRegistry}/mq-with-metrics:${mqVersion}

USER 0

COPY queue-manager-custom-otel/mqtracingexit.conf /var/mqm/exits64/

COPY /workspace/mq-exit/mqtracingexit_r /var/mqm/exits64/

RUN chmod 770 /var/mqm/exits64/*

USER 1001
