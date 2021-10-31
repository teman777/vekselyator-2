package org.voronov.bot.model;


import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "Operations")
public class Operations {
    @Id
    private Long id;

    private User userFrom;

    private User userTo;



}
