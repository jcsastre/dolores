package com.dolores.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name="users")
public class User {

    @Id
    private UUID id;

    private String fbMessengerPsid;
}
