#!/bin/bash
#
# Copyright 2023 IBM Corp.
#
# Licensed under the Apache License, Version 2.0 (the 'License');
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
# A simple MVP script that will run MQSC against a queue manager.

ckksum=""

# Outer loop that keeps the MQ service running
while true; do

   tmpCksum=`cksum /dyn-mq-config-mqsc/dynamic.mqsc | cut -d" " -f1`

   if (( tmpCksum != cksum ))
   then
      cksum=$tmpCksum
      echo "Applying MQSC"
      runmqsc $1 < /dyn-mq-config-mqsc/dynamic.mqsc
   else
      sleep 3
   fi

done
