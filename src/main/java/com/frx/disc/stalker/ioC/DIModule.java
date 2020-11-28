package com.frx.disc.stalker.ioC;

import com.google.inject.AbstractModule;

/**
 * Created by surja on 28.11.2020
 */
public class DIModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(String.class).toInstance("ala");
    }
}
