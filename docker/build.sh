#!/bin/bash

# To be executed from project root
docker build -t iudx/gis-depl:latest -f docker/depl.dockerfile .
docker build -t iudx/gis-dev:latest -f docker/dev.dockerfile .
