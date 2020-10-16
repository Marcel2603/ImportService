#!/usr/bin/env bash
mkdir "lastTest"
cp "Camt.053/2013-12-27_C53_DE14740618130000033626_EUR_A000IH.xml" "lastTest/origin.xml"
cd "lastTest"
for (( i = 0; i < 10000; i++ )); do
  cp "origin.xml" "sample-${i}.xml"
done