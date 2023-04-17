FROM registry.redhat.io/ubi8/ruby-27:1-95

USER root

ENV RAILS_ENV=production
ENV HOME=/workshopper

RUN mkdir -p /workshopper \
    && chown default:root /workshopper \
    && chmod 777 /workshopper

USER default

WORKDIR /workshopper

ADD --chown=default:root Gemfile Gemfile.lock ./

RUN bundle lock --add-platform x86_64-linux && \ 
    bundle config set --local deployment 'true' && \ 
    bundle install

ADD --chown=default:root . .

RUN bundle exec rake assets:precompile

RUN rm -rf tmp log && mkdir -p tmp log && chmod -R 0777 tmp log

EXPOSE 8080

CMD ./boot.sh
