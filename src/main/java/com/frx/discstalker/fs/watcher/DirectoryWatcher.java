package com.frx.discstalker.fs.watcher;

import io.reactivex.rxjava3.subjects.PublishSubject;

import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * Watches the specified directories for changes and allows subscribing to them.
 */
public class DirectoryWatcher {
  private final PublishSubject<DirectoryWatcherEvent> eventSubject;
  private final WatchService watcher;
  private final Thread thread;

  public DirectoryWatcher() throws IOException {
    this.eventSubject = PublishSubject.create();
    this.watcher = FileSystems.getDefault().newWatchService();
    this.thread = new Thread(this::loop);
    this.thread.start();
  }

  /**
   * Returns the observable representing an event stream with the directory changes.
   */
  public PublishSubject<DirectoryWatcherEvent> getEventSubject() {
    return eventSubject;
  }

  /**
   * Starts watching the given path for changes.
   */
  public void watchDirectory(Path path) throws IOException {
    if (!Files.isDirectory(path)) {
      throw new NotDirectoryException(path.toString());
    }

    path.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
  }

  /**
   * Terminates the watcher thread.
   */
  public void terminate() {
    thread.interrupt();
  }

  private void loop() {
    try {
      while (true) {
        final var key = watcher.take();
        final var watchedPath = (Path) key.watchable();

        for (final var watchEvent : key.pollEvents()) {
          @SuppressWarnings("unchecked") final var event = (WatchEvent<Path>) watchEvent;

          final var relativeChildPath = event.context();
          final var childPath = watchedPath.resolve(relativeChildPath);

          final var directoryWatcherEvent = new DirectoryWatcherEvent(
            DirectoryWatcherEventType.fromWatchEventKind(event.kind()),
            watchedPath,
            childPath
          );
          eventSubject.onNext(directoryWatcherEvent);
        }

        key.reset();
      }
    } catch (InterruptedException ex) {
      eventSubject.onComplete();
      // Interrupted -> just let the task end.
    }
  }
}
