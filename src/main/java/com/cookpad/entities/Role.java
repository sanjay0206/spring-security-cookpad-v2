package com.cookpad.entities;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Data
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
