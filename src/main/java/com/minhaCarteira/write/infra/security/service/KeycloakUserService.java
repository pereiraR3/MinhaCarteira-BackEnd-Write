package com.minhaCarteira.write.infra.security.service;

import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

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

        UsersResource usersResource = keycloak.realm(realm).users();

        try (Response response = usersResource.create(user)) {
            if (response.getStatus() == 201) {
                if (response.getStatus() == 201) {

                }
                    List<UserRepresentation> search = usersResource.search(username, true);
                    if (!search.isEmpty()) {
                        String userId = search.get(0).getId();

                        RoleRepresentation userRole = keycloak.realm(realm).roles().get("USER").toRepresentation();

                        usersResource.get(userId).roles().realmLevel().add(Collections.singletonList(userRole));

                        logger.info("Usuário '{}' criado e role 'USER' associada com sucesso", username);
                    } else {
                        logger.error("Usuário '{}' não encontrado após criação", username);
                    }
            } else if (response.getStatus() == 409) {
                logger.warn("Erro: Usuário '{}' ou email '{}' já existe.", username, email);
            } else {
                logger.error("Erro ao criar usuário. Status: {} - {}", response.getStatus(), response.getStatusInfo().getReasonPhrase());
            }
        }
    }
}