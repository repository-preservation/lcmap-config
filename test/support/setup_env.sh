#!/usr/bin/env bash

cd /tmp && \
    git clone https://github.com/USGS-EROS/lcmap-system.git && \
    cd lcmap-system && \
    lein install
    cd -
mkdir ~/.usgs/
cp test/support/sample_config.ini ~/.usgs/lcmap.ini

