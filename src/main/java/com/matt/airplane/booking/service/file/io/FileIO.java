package com.matt.airplane.booking.service.file.io;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

@Component
public class FileIO {

  public File[] listFiles(String directory, FilenameFilter filenameFilter) {
    return new File(directory).listFiles(filenameFilter);
  }

  public File createFile(String directory, String filename) {
    return new File(directory, filename);
  }
  
  public File createFile(String filePath) {
    return new File(filePath);
  }

  public void saveToFile(File file, String contents) throws IOException {
    FileUtils.writeStringToFile(file, contents, true);
  }

  public void saveCollectionToFile(String filePath, Collection<?> lines) throws IOException {
    File destination = new File(filePath);
    FileUtils.writeLines(destination, lines, true);
  }
  
  public void saveCollectionToFile(File file, Collection<?> lines) throws IOException {
    FileUtils.writeLines(file, lines, true);
  }
  
  public List<String> readFromFile(File file) throws IOException {
	  return FileUtils.readLines(file);
  }
}
