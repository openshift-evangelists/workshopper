#!/usr/bin/env bash

ASCIIDOCTOR_VERSION="1.5.5"
LIQUID_VERSION="3.0.6"

cd src/main/resources

mkdir gems

export GEM_PATH=`pwd`/gems
export GEM_HOME=$GEM_PATH

gem install asciidoctor -v $ASCIIDOCTOR_VERSION
gem install liquid -v $LIQUID_VERSION

rm -rf asciidoctor asciidoctor.rb liquid liquid.rb

mv gems/gems/asciidoctor-$ASCIIDOCTOR_VERSION/lib/asciidoctor.rb .
mv gems/gems/asciidoctor-$ASCIIDOCTOR_VERSION/lib/asciidoctor .
mv gems/gems/liquid-$LIQUID_VERSION/lib/liquid.rb .
mv gems/gems/liquid-$LIQUID_VERSION/lib/liquid .

rm -rf gems