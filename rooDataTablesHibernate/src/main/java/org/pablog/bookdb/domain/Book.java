package org.pablog.bookdb.domain;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@RooJson(deepSerialize = true)
public class Book {

    /**
     */
    @Size(max = 50)
    private String name;

    /**
     */
    @Size(max = 3000)
    private String description;

    /**
     */
    @Size(max = 50)
    private String author;

    /**
     */
    @Size(max = 50)
    private String publisher;

    /**
     */
    @Size(max = 50)
    private String isbn;

    /**
     */
    private Integer pages;

    /**
     */
    @Past
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date datePublished;
}