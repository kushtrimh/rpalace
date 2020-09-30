package org.kushtrimhajrizi.rpalace.oauth.client.registration;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @author Kushtrim Hajrizi
 */
@Entity
@Table(name = "client_registration", indexes = {
        @Index(columnList = "registration_token", name = "client_registration_registration_token_idx")
})
public class ClientRegistration {
    
    @Id
    @Column(length = 64)
    @GenericGenerator(name = "id_generator", strategy = "org.kushtrimhajrizi.rpalace.utils.IdGenerator")
    @GeneratedValue(generator = "id_generator", strategy = GenerationType.SEQUENCE)
    private String id;
}
