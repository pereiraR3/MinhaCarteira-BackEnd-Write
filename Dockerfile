FROM quay.io/keycloak/keycloak:latest

ENV KEYCLOAK_ADMIN=admin
ENV KEYCLOAK_ADMIN_PASSWORD=admin123

ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev"]