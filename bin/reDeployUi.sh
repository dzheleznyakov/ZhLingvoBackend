#!/bin/sh

if [[ $# < 1 ]]; then
    echo "Usage: ${0} <path-to-ui-folder>"
    exit 1
fi

this_folder=$(pwd)
resource_path=src/main/resources

cd $1
npm run build
rm -rf ${this_folder}/${resource_path}/public/*
cp -R build/* ${this_folder}/${resource_path}/public
