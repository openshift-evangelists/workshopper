FROM registry.redhat.io/ubi8/ruby-27:1-95
#FROM registry.redhat.io/ubi8/ruby-25:1-220

# LABEL io.openshift.s2i.scripts-url="image:///usr/libexec/s2i" \
#       io.s2i.scripts-url="image:///usr/libexec/s2i"

# RUN curl -sL -o /etc/yum.repos.d/centos-essentials.repo https://gist.githubusercontent.com/marekjelen/c08a3c3a548820144f2da01d2bad6279/raw/centos-essentials.repo
# ENV PATH=/opt/essentials/bin:$PATH

# RUN yum update -y && \
#     yum install --setopt=tsflags=nodocs -y essentials-ruby bundler \
#     gcc gcc-c++ libxml2-devel sqlite-devel git && \
#     yum clean all && \
#     rm -rf /var/cache/yum

USER root
# RUN gem update --system --no-document && \ 
#     gem update bundler --no-document

# RUN mkdir -p /usr/libexec/s2i

# COPY s2i/assemble s2i/run /usr/libexec/s2i/

# RUN chmod 777 /usr/libexec/s2i/{assemble,run}

ENV RAILS_ENV=production

# RUN useradd -u 1001 -g 0 -M -d /workshopper workshopper

RUN mkdir -p /workshopper && chown default:root /workshopper && chmod 777 /workshopper

USER default
WORKDIR /workshopper

#ADD --chown=default:root Gemfile Gemfile.lock ./
ADD --chown=default:root Gemfile ./


RUN bundle lock
RUN bundle lock --add-platform x86_64-linux
RUN bundle config set --local deployment 'true'
RUN bundle install
#RUN bundle install --deployment
#RUN
#RUN gem install liquid -v '5.4.0' --source 'https://rubygems.org/'
#RUN gem install rexml -v '3.2.5' --source 'https://rubygems.org/'
#RUN gem install ruby2_keywords -v '0.0.5' --source 'https://rubygems.org/'
#RUN gem install faraday-net_http -v '3.0.2' --source 'https://rubygems.org/'
#RUN gem install excon -v '0.99.0' --source 'https://rubygems.org/'
#RUN gem install connection_pool -v '2.4.0' --source 'https://rubygems.org/'
#RUN gem install coffee-script-source -v '1.12.2' --source 'https://rubygems.org/'
#RUN gem install coderay -v '1.1.3' --source 'https://rubygems.org/'
#RUN gem install byebug -v '11.1.3' --source 'https://rubygems.org/'
#RUN gem install tilt -v '2.1.0' --source 'https://rubygems.org/'
#RUN gem install ffi -v '1.15.5' --source 'https://rubygems.org/'
#RUN gem install zeitwerk -v '2.6.7' --source 'https://rubygems.org/'
#RUN gem install thor -v '1.2.1' --source 'https://rubygems.org/'
#RUN gem install method_source -v '1.0.0' --source 'https://rubygems.org/'
#RUN gem install popper_js -v '1.16.1' --source 'https://rubygems.org/'
#RUN gem install bindex -v '0.8.1' --source 'https://rubygems.org/'
#RUN gem install execjs -v '2.8.1' --source 'https://rubygems.org/'
#RUN gem install asciidoctor -v '2.0.18' --source 'https://rubygems.org/'
#RUN gem install timeout -v '0.3.2' --source 'https://rubygems.org/'
#RUN gem install date -v '3.3.3' --source 'https://rubygems.org/'
#RUN gem install mini_mime -v '1.1.2' --source 'https://rubygems.org/'
#RUN gem install marcel -v '1.0.2' --source 'https://rubygems.org/'
#RUN gem install websocket-extensions -v '0.1.5' --source 'https://rubygems.org/'
#RUN gem install nio4r -v '2.5.9' --source 'https://rubygems.org/'
#RUN gem install rack -v '2.2.6.4' --source 'https://rubygems.org/'
#RUN gem install i18n -v '1.12.0' --source 'https://rubygems.org/'
#RUN gem install crass -v '1.0.6' --source 'https://rubygems.org/'
#RUN gem install racc -v '1.6.2' --source 'https://rubygems.org/'
#RUN gem install erubi -v '1.12.0' --source 'https://rubygems.org/'
#RUN gem install builder -v '3.2.4' --source 'https://rubygems.org/'
#RUN gem install minitest -v '5.18.0' --source 'https://rubygems.org/'
#RUN gem install concurrent-ruby -v '1.2.2' --source 'https://rubygems.org/'
#RUN gem install rake -v '13.0.6' --source 'https://rubygems.org/'

ADD --chown=workshopper:root . .

RUN bundle exec rake assets:precompile

RUN rm -rf tmp log && mkdir -p tmp log && chmod -R 0777 tmp log
ENV HOME=/workshopper

EXPOSE 8080

CMD ./boot.sh
