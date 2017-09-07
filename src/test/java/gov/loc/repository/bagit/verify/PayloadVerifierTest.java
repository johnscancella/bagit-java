package gov.loc.repository.bagit.verify;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

import gov.loc.repository.bagit.domain.Bag;
import gov.loc.repository.bagit.exceptions.FileNotInManifestException;
import gov.loc.repository.bagit.exceptions.FileNotInPayloadDirectoryException;
import gov.loc.repository.bagit.hash.StandardBagitAlgorithmNameToSupportedAlgorithmMapping;
import gov.loc.repository.bagit.reader.BagReader;

public class PayloadVerifierTest {
  
  private Path rootDir = Paths.get(new File("src/test/resources/bags/v0_97/bag").toURI());
  private BagReader reader = new BagReader();
  
  private PayloadVerifier sut;
  
  @Before
  public void setup(){
    sut = new PayloadVerifier(new StandardBagitAlgorithmNameToSupportedAlgorithmMapping());
  }

  @Test(expected=FileNotInPayloadDirectoryException.class)
  public void testErrorWhenManifestListFileThatDoesntExist() throws Exception{
    rootDir = Paths.get(new File("src/test/resources/filesInManifestDontExist").toURI());
    Bag bag = reader.read(rootDir);
    
    sut.verifyPayload(bag, true);
  }
  
  @Test(expected=FileNotInManifestException.class)
  public void testErrorWhenFileIsntInManifest() throws Exception{
    rootDir = Paths.get(new File("src/test/resources/filesInPayloadDirAreNotInManifest").toURI());
    Bag bag = reader.read(rootDir);
    
    sut.verifyPayload(bag, true);
  }
  
  @Test
  public void testBagWithTagFilesInPayloadIsValid() throws Exception{
    rootDir = Paths.get(new File("src/test/resources/bags/v0_96/bag-with-tagfiles-in-payload-manifest").toURI());
    Bag bag = reader.read(rootDir);
    
    sut.verifyPayload(bag, true);
  }
  
  @Test(expected=FileNotInManifestException.class)
  public void testNotALlFilesListedInAllManifestsThrowsException() throws Exception{
    Path bagDir = Paths.get(new File("src/test/resources/notAllFilesListedInAllManifestsBag").toURI());
    Bag bag = reader.read(bagDir);
    sut.verifyPayload(bag, true);
  }
}
