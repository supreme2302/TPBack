#!/usr/bin/env bash

echo xmlstarlet ed -u '/resources/'$2'[@name="'$3'"]' -v "$4" $1

xmlstarlet ed --inplace -u '/resources/'$2'[@name="'$3'"]' -v "$4" $1


