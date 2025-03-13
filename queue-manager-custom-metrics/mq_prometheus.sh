#!/bin/bash

# This is used to start the IBM MQ monitoring service for Prometheus

# The queue manager name comes in from the service definition as the
# only command line parameter
qMgr=$1

# Set the environment to ensure we pick up libmqm.so etc
# Try to run it for a local qmgr; if that fails fallback to a
# default
# If this is a client connection, then deal with no known qmgr of the given name.
. /opt/mqm/bin/setmqenv -m $qMgr -k >/dev/null 2>&1
if [ $? -ne 0 ]
then
  . /opt/mqm/bin/setmqenv -s -k
fi

# A list of queues to be monitored is given here.
# It is a set of names or patterns ('*' only at the end, to match how MQ works),
# separated by commas. When no queues match a pattern, it is reported but
# is not fatal.
# The set can also include negative patterns such as "!SYSTEM.*".
queues="APP.*,MYQ.*"

# An alternative is to have a file containing the patterns, and named
# via the ibmmq.monitoredQueuesFile option.

# Do similar for channels
channels="TO.*,SYSTEM.DEF.SVRCONN"

# See config.go for all recognised flags
ARGS="-ibmmq.queueManager=$qMgr"
ARGS="$ARGS -ibmmq.monitoredQueues=$queues"
ARGS="$ARGS -ibmmq.monitoredChannels=$channels"
ARGS="$ARGS -ibmmq.monitoredTopics=#"
ARGS="$ARGS -ibmmq.monitoredSubscriptions=*"
ARGS="$ARGS -rediscoverInterval=1h"

ARGS="$ARGS -ibmmq.useStatus=true"
ARGS="$ARGS -log.level=error"

# This may help with some issues if the program has a SEGV. It
# allows Go to do a better stack trace.
export MQS_NO_SYNC_SIGNAL_HANDLING=true


# Start via "exec" so the pid remains the same. The queue manager can
# then check the existence of the service and use the MQ_SERVER_PID value
# to kill it on shutdown.
exec /usr/local/bin/mqgo/mq_prometheus $ARGS
