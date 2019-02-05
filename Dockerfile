FROM centos

LABEL io.openshift.s2i.scripts-url="image:///usr/libexec/s2i" \
      io.s2i.scripts-url="image:///usr/libexec/s2i"

RUN curl -sL -o /etc/yum.repos.d/centos-essentials.repo https://gist.githubusercontent.com/marekjelen/c08a3c3a548820144f2da01d2bad6279/raw/centos-essentials.repo
ENV PATH=/opt/essentials/bin:$PATH

RUN yum update -y && \
    yum install --setopt=tsflags=nodocs -y essentials-ruby bundler \
    gcc gcc-c++ libxml2-devel sqlite-devel git && \
    yum clean all && \
    rm -rf /var/cache/yum
    
RUN gem update --system --no-document && \ 
    gem update bundler --no-document

RUN mkdir -p /usr/libexec/s2i

COPY s2i/assemble s2i/run /usr/libexec/s2i/

RUN chmod 777 /usr/libexec/s2i/{assemble,run}

ENV RAILS_ENV=production

RUN useradd -u 1001 -g 0 -M -d /workshopper workshopper

RUN mkdir -p /workshopper && chown workshopper:root /workshopper && chmod 777 /workshopper

USER workshopper
WORKDIR /workshopper

ADD --chown=workshopper:root Gemfile Gemfile.lock ./


RUN bundle install --deployment

ADD --chown=workshopper:root . .

RUN bundle exec rake assets:precompile

RUN rm -rf tmp log && mkdir -p tmp log && chmod -R 0777 tmp log
ENV HOME=/workshopper

EXPOSE 8080

CMD ./boot.sh