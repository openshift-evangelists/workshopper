FROM centos:7

RUN curl -o /etc/yum.repos.d/bintray-mjelen-centos.repo https://bintray.com/mjelen/centos/rpm

RUN yum install -y ruby libyaml \
    && yum clean all -y \
    && gem install bundler

RUN yum install -y gcc make

WORKDIR /root

COPY Gemfile Gemfile.lock ./

RUN bundle install

COPY lib ./lib
COPY public ./public
COPY config.ru ./

RUN ls -la

CMD bundle exec puma -p 8080