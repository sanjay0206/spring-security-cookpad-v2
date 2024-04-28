package com.cookpad.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Column(columnDefinition = "varchar(255) default 'USER'")
    private String roleName;

    public Role(String roleName) {
        this.roleName = roleName;
    }
}
