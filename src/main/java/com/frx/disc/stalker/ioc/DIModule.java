package com.frx.disc.stalker.ioc;

import com.frx.disc.stalker.fs.LiveDirectoryTreeCreator;
import com.frx.disc.stalker.fs.LiveDirectoryTreeFactory;
import com.google.inject.AbstractModule;

/**
 * Created by surja on 28.11.2020
 */
public class DIModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(LiveDirectoryTreeFactory.class).to(LiveDirectoryTreeCreator.class);
  }
}
