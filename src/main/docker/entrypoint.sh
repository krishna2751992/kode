#!/bin/sh

echo "========================================================================"
echo "DESY APP Zone Service entrypoint.sh starting up with PID $$"
echo "${JAVA_OPTS}"
echo "JAVA_OPTS: ${JAVA_OPTS}"
echo "IIB_URL: ${IIB_URL}"
echo "IIB_ENV_KEY: ${IIB_ENV_KEY}"
echo "IIB_ENV_VALUE: ${IIB_ENV_VALUE}"
echo -n $(java -version)
echo "========================================================================"

echo "The application will start in ${JHIPSTER_SLEEP}s..." && sleep ${JHIPSTER_SLEEP}
exec java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar "${HOME}/app.war" "$@"
