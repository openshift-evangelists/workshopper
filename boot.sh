#!/bin/bash

export SECRET_KEY_BASE="$(openssl rand -hex 64)"
export RAILS_SERVE_STATIC_FILES=true

bundle exec rackup -p 8080 -E $RAILS_ENV