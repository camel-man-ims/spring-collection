package com.demo.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Member {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
}
