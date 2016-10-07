mvn clean install
rm -r /wso2dev/env/servers/apache-tomcat-7.0.53/webapps/loyalty-portal*
cp loyalty-app/target/loyalty-portal.war /wso2dev/env/servers/apache-tomcat-7.0.53/webapps/
rm -r /wso2dev/env/servers/apache-tomcat-7.0.53/webapps/helpdesk-portal*
cp helpdesk-app/target/helpdesk-portal.war /wso2dev/env/servers/apache-tomcat-7.0.53/webapps/
