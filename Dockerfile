FROM quay.io/keycloak/keycloak:26.3.3

COPY realm-export.json /opt/keycloak/data/import/