FROM centos:7

RUN curl -o /etc/yum.repos.d/bintray-mjelen-centos.repo https://bintray.com/mjelen/centos/rpm

RUN yum install -y ruby gcc \
    && yum clean all -y \
    && gem install bundler

RUN useradd -m workshopper && mkdir /workshopper \
    && chown workshopper:workshopper /workshopper && chmod 777 /workshopper

USER workshopper
WORKDIR /workshopper
ENV HOME /workshopper

RUN mkdir -p cache && chmod 777 cache

COPY Gemfile Gemfile.lock ./

RUN bundle install --deployment && chmod 777 .bundle

COPY . ./

ENV LC_ALL en_US.UTF-8
ENV ENABLE_CONTENT_CACHE true

EXPOSE 8080

CMD bundle exec puma -p 8080
