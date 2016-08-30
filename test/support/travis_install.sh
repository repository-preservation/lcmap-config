#!/usr/bin/env bash

cd /tmp && \
    git clone https://github.com/USGS-EROS/lcmap-system.git && \
    cd lcmap-system && \
    lein install
    cd ~

