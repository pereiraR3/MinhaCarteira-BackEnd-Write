package com.minhaCarteira.crud.infra.security.service;

import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class KeycloakUserService {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakUserService.class);

    @Autowired
    private Keycloak keycloak;

    @Value("${keycloak.admin.realm}")
    private String realm;

    public void createUser(String username, String email, String firstName, String lastName, String password) {

        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmailVerified(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);

        user.setCredentials(Collections.singletonList(credential));
        user.setRealmRoles(java.util.List.of("USER"));

        UsersResource usersResource = keycloak.realm(realm).users();

        try (Response response = usersResource.create(user)) {
            if (response.getStatus() == 201) {
                logger.info("Usuário '" + username + "' criado com sucesso!");
            } else if (response.getStatus() == 409) {
                logger.info("Erro: Usuário '" + username + "' ou email '" + email + "' já existe.");
            } else {
                logger.info("Erro ao criar usuário. Status: " + response.getStatusInfo().getReasonPhrase());
            }
        }
    }
}