package com.frx.discstalker.ioc;

import com.frx.discstalker.fs.LiveDirectoryTreeCreator;
import com.frx.discstalker.fs.LiveDirectoryTreeFactory;
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
