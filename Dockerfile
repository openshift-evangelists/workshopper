FROM centos:7

RUN curl -o /etc/yum.repos.d/bintray-mjelen-centos.repo https://bintray.com/mjelen/centos/rpm

RUN yum install -y ruby gcc \
    && yum clean all -y \
    && gem install bundler

WORKDIR /root

COPY Gemfile Gemfile.lock ./

RUN bundle install

COPY lib ./lib
COPY public ./public
COPY config.ru ./

ENV LC_ALL en_US.UTF-8

CMD bundle exec puma -p 8080