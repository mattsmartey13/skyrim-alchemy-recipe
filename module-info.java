module skyrim.alchemy.recipe {
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.media;
    requires javafx.web;
    requires javafx.swing;
    requires javafx.fxml;

    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jdk8;

    requires org.apiguardian.api;
    requires org.junit.jupiter.api;
    requires org.junit.platform.commons;
    requires org.junit.platform.runner;
    requires org.junit.platform.launcher;
    requires org.junit.platform.suite.api;
    requires org.junit.platform.suite.commons;
    requires org.junit.platform.engine;
    requires org.opentest4j;

    exports model;

    opens application;
}