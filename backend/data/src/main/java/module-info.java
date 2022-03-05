open module data {
    requires spring.data.r2dbc;
    requires spring.context;
    requires spring.boot.autoconfigure;
    requires reactor.core;
    requires spring.data.relational;
    requires spring.data.commons;
    exports project.society.data.config;
    exports project.society.data.dto;
    exports project.society.data.dao;
}