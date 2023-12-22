ARG mqSrcImage="icr.io/ibm-messaging/mq"

ARG mqVersion="9.3.3.1-r1"

ARG nameSpace="mq-demo"

FROM image-registry.openshift-image-registry.svc:5000/${nameSpace}/mq-metrics:${mqVersion} as mq-metrics

FROM ${mqSrcImage}:${mqVersion}

USER 0

RUN mkdir -p /opt/mqm/metrics

COPY --from=mq-metrics /go/bin/mq_prometheus /usr/local/bin/mqgo/mq_prometheus

RUN chmod 770 /usr/local/bin/mqgo/mq_prometheus

COPY --from=mq-metrics /go/src/github.com/ibm-messaging/mq-metric-samples/mq_prometheus.sh /opt/mqm/metrics

RUN chmod 770 /opt/mqm/metrics/*

USER 1001
