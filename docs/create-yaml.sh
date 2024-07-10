#!/usr/bin/env bash

BASEDIR=$(dirname "$0")

curl http://localhost:8010/v3/api-docs.yaml -o $BASEDIR/api-docs.yaml

sed -i '' -e 's/'"'"'\*\/\*'"'"'/application\/json/g' $BASEDIR/api-docs.yaml
sed -i '' -e 's/ResponseTask/Task/g' -e 's/ResponseTag/Tag/g' -e 's/ResponseGroup/Group/g' $BASEDIR/api-docs.yaml
sed -i '' -e 's/task-controller/task/g' -e 's/tag-controller/tag/g' -e 's/group-controller/group/g' $BASEDIR/api-docs.yaml
