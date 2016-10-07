mvn clean install
rm -r /wso2dev/env/servers/apache-tomcat-7.0.53/webapps/loyalty-portal*
cp target/loyalty-portal.war /wso2dev/env/servers/apache-tomcat-7.0.53/webapps/
