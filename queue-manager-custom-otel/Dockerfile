ARG mqVersion="9.4.2.0-r1"

ARG mqSrcImage="image-registry.openshift-image-registry.svc:5000/mq-pipeline/mq-with-metrics"

FROM ${mqSrcImage}:${mqVersion}

USER 0

COPY /workspace/source/mq-exit/* /var/mqm/exits64/

COPY queue-manager-custom-otel/mqtracingexit.conf /var/mqm/exits64/

RUN chmod 770 /var/mqm/exits64/*

USER 1001
