#!/bin/bash

cd app/build/outputs/apk/productionAPI/debug/
curl -F document=@app-productionAPI-debug.apk https://api.telegram.org/bot${TL_TOKEN}/sendDocument?chat_id=@rockindev
