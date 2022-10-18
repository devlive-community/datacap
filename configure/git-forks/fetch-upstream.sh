#!/bin/sh

git remote add upstream git@github.com:EdurtIO/incubator-datacap.git
git remote -v
git fetch upstream
git merge upstream/develop
