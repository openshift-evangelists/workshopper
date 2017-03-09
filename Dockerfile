FROM jboss/wildfly

ADD target/ROOT.war /opt/jboss/wildfly/standalone/deployments/

USER root

RUN chmod 777 -R /opt

USER jboss
