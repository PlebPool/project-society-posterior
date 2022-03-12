open module web {
    requires data;
    requires security;
    requires spring.webflux;
    requires spring.context;
    requires reactor.core;
    requires spring.web;
    requires org.slf4j;
    requires spring.beans;
    exports project.society.web.apis.mock;
}