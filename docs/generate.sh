#!/usr/bin/env bash

cd $(dirname "$0")

docker run --rm \
  -v $PWD/api-docs.yaml:/api-docs.yaml \
  -v $PWD/output:/output \
  openapitools/openapi-generator-cli generate \
  -i /api-docs.yaml \
  -g typescript-axios \
  -o /output
