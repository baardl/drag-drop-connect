#!/usr/bin/env bash
echo stopping dropwizard-drag-drop-connect
docker stop dropwizard-drag-drop-connect
echo removing dropwizard-drag-drop-connect
docker rm dropwizard-drag-drop-connect
echo list active docker containers
docker ps
