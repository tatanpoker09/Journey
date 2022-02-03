#!/bin/sh
export $(cat ./integrations/*/.env | xargs) &&  python -u ./app.py