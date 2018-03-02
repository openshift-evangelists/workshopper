FROM centos

RUN curl -sLf 'https://dl.cloudsmith.io/public/marek-jelen/generic/cfg/install/config.rpm.txt?os=el&dist=7' > /etc/yum.repos.d/marek-jelen-generic.repo

RUN yum makecache -y && \
    yum install --setopt=tsflags=nodocs -y ruby rubygem-bundler \
    gcc gcc-c++ libxml2-devel sqlite-devel && \
    yum clean all && \
    rm -rf /var/cache/yum && \
    gem update --system --no-document

ENV RAILS_ENV=production

RUN useradd -u 1001 workshopper

USER workshopper
WORKDIR /home/workshopper

ADD --chown=workshopper Gemfile Gemfile.lock ./

RUN bundle install --deployment

ADD --chown=workshopper . .

RUN mkdir -p tmp log

RUN bundle exec rake assets:precompile

EXPOSE 8080

CMD ./boot.sh