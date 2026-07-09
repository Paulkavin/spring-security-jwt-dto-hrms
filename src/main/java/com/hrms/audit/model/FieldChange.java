package com.hrms.audit.model;
//Why record - A field change should never change after it is created.
public record FieldChange(
    String field,
    String oldValue,
    String newValue
){}

/**
 * When you declare a record, the Java compiler automatically generates the following components behind the scenes:Private, final fields for each of your components.
 * A canonical constructor that matches your parameters to initialize the fields.Public accessor methods (Note: they use the field name directly, like user.name(), 
 * instead of user.getName()).equals() and hashCode() implementations to compare values correctly.
 * A toString() implementation that clean prints the data fields
 */