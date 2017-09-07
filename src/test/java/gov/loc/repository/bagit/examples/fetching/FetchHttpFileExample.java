package gov.loc.repository.bagit.examples.fetching;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import gov.loc.repository.bagit.domain.FetchItem;

public class FetchHttpFileExample extends Assert {
  @Rule
  public TemporaryFolder folder= new TemporaryFolder();

  /**
   * <b> THIS IS JUST AN EXAMPLE. DO NOT USE IN PRODUCTION!</b>
   * 
   * @throws IOException if there is a problem getting the file
   */
  @Test
  public void fetchFileUsingJavaStandardLibrary() throws IOException{
    //in actual usage you would iterate over the list of FetchItem in the Bag
    FetchItem item = new FetchItem(new URL("https://en.wikipedia.org/wiki/Main_Page"), 0l, folder.newFile("Main_page.html").toPath());
    try{
      Files.copy(item.url.openStream(), item.path, StandardCopyOption.REPLACE_EXISTING);
      assertTrue(Files.exists(item.path));
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }
}
