open module security {
    requires spring.context;
    requires spring.data.relational;
    requires spring.data.commons;
    requires spring.session.core;
    requires reactor.core;
    requires data;
    requires utility;
    requires spring.boot.autoconfigure;
    requires spring.web;
    requires spring.core;
    requires io.netty.codec.http;
    requires spring.security.web;
    requires spring.security.oauth2.client;
    requires spring.security.config;
    requires spring.beans;
}