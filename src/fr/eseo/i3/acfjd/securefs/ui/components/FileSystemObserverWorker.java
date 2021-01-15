package fr.eseo.i3.acfjd.securefs.ui.components;

import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.SwingWorker;

public class FileSystemObserverWorker extends SwingWorker<Void, PropertyChangeEvent> {

	
	private final WatchService watcher;
	
	private final File rootFolder;
	
	public FileSystemObserverWorker(File rootFolder) throws IOException{
		this.rootFolder = rootFolder;
		this.watcher = FileSystems.getDefault().newWatchService();
	}
	
	@Override
	protected Void doInBackground() throws Exception{
		final Map<WatchKey,Path> keys = new HashMap<>();
		final Consumer<Path> register = path -> {
			if(!path.toFile().exists() || !path.toFile().isDirectory()) {
				throw new RuntimeException("Folder "+path+" either does not exits or is not actually a folder!");
			}
			try {
				Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
					@Override
					public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)throws IOException {
						final WatchKey watchKey = dir.register(FileSystemObserverWorker.this.watcher, new WatchEvent.Kind[] {StandardWatchEventKinds.ENTRY_CREATE,StandardWatchEventKinds.ENTRY_DELETE,StandardWatchEventKinds.ENTRY_MODIFY});
						keys.put(watchKey,dir);
						FileSystemObserverWorker.this.publish(FileSystemObserverWorker.this.createChangeEvent(dir,watchKey));
						return FileVisitResult.CONTINUE;
					}
				});
			}catch(IOException ioe) {
				throw new RuntimeException("Error registering "+path+".",ioe);
			}
		};
		register.accept(this.rootFolder.toPath());
		
		final boolean infini = true;
		while(infini) {
			final WatchKey key;
			try {
				key = this.watcher.take();
			}catch(InterruptedException ie) {
				throw new RuntimeException("Error recuperating a file system event",ie);
			}
			final Path dir = keys.get(key);
			if(dir==null) {
				continue;
			}
			key.pollEvents().stream().filter(event->(event.kind()!=StandardWatchEventKinds.OVERFLOW))
			.forEach(event -> {
				if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE){
				final Path context = (Path) event.context();
	              final Path absPath = dir.resolve(context);
	              if (!absPath.toFile().isHidden()) {
	                if (absPath.toFile().isDirectory()) {
	                  register.accept(absPath);
	                } else {
	                  this.publish(this.createChangeEvent(event, key));
	                }
	              }
	            } else if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
	              final Path context = (Path) event.context();
	              final Path absPath = dir.resolve(context);
	              if (!absPath.toFile().isHidden()) {
	                if (absPath.toFile().isDirectory()) {
	                  key.cancel();
	                  keys.remove(key);
	                }
	                this.publish(this.createChangeEvent(event, key));
	              }
	            } else { // if (event.kind()==StandardWatchEventKinds.ENTRY_MODIFIED) {
	              // Do nothing for the moment...
	            }
	          });
	      final boolean valid = key.reset();
	      if (!valid) {
	        break; // Problem in resetting key
	      }
	    }
	    return null;
	  }

	  protected PropertyChangeEvent createChangeEvent(WatchEvent event, WatchKey key) {
	    final Path name = (Path) event.context();
	    final Path child = this.rootFolder.toPath().resolve(name);
	    final PropertyChangeEvent pce =
	        new PropertyChangeEvent(this, event.kind().name(), null, child.toFile());
	    return pce;
	  }

	  protected PropertyChangeEvent createChangeEvent(Path name, WatchKey key) {
	    final Path child = this.rootFolder.toPath().resolve(name);
	    final PropertyChangeEvent pce = new PropertyChangeEvent(this, "init", null, child.toFile());
	    return pce;
	  }

	  @Override
	  protected void process(List<PropertyChangeEvent> chunks) {
	    super.process(chunks);
	    for (final PropertyChangeEvent pce : chunks) {
	      this.getPropertyChangeSupport().firePropertyChange(pce);
	    }
	  }

	}
