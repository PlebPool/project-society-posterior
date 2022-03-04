module application {
    requires data;
    requires web;
    requires spring.boot.autoconfigure;
    requires spring.boot;
    exports project.society.application.config;
}