package com.frx.discstalker.ioc;

import com.frx.discstalker.fs.LiveDirectoryTreeFactory;
import com.frx.discstalker.fs.ILiveDirectoryTreeFactory;
import com.google.inject.AbstractModule;

/**
 * Created by surja on 28.11.2020
 */
public class DIModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(ILiveDirectoryTreeFactory.class).to(LiveDirectoryTreeFactory.class);
  }
}
