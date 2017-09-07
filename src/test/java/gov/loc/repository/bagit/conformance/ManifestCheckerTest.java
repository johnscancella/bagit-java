package gov.loc.repository.bagit.conformance;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import gov.loc.repository.bagit.PrivateConstructorTest;
import gov.loc.repository.bagit.exceptions.InvalidBagitFileFormatException;

public class ManifestCheckerTest extends PrivateConstructorTest{
  @Rule
  public TemporaryFolder folder= new TemporaryFolder();
  
  private final Path rootDir = Paths.get("src","test","resources","linterTestBag");
  
  @Test
  public void testClassIsWellDefined() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException{
    assertUtilityClassWellDefined(ManifestChecker.class);
  }
  
  @Test
  public void testCheckManifests() throws Exception{
    Set<BagitWarning> warnings = new HashSet<>();

    ManifestChecker.checkManifests(rootDir, StandardCharsets.UTF_16, warnings, Collections.emptyList());
    
    assertTrue(warnings.contains(BagitWarning.WEAK_CHECKSUM_ALGORITHM));
    assertTrue(warnings.contains(BagitWarning.DIFFERENT_CASE));
    if(FileSystems.getDefault().getClass().getName() != "sun.nio.fs.MacOSXFileSystem"){ //don't test normalization on mac
      assertTrue(warnings.contains(BagitWarning.DIFFERENT_NORMALIZATION));
    }
    assertTrue(warnings.contains(BagitWarning.BAG_WITHIN_A_BAG));
    assertTrue(warnings.contains(BagitWarning.LEADING_DOT_SLASH));
    assertTrue(warnings.contains(BagitWarning.NON_STANDARD_ALGORITHM));
    assertTrue(warnings.contains(BagitWarning.OS_SPECIFIC_FILES));
    assertTrue(warnings.contains(BagitWarning.MISSING_TAG_MANIFEST));
  }
  
  @Test
  public void testCheckTagManifest() throws Exception{
    folder.newFile("tagmanifest-md5.txt");
    Set<BagitWarning> warnings = new HashSet<>();

    ManifestChecker.checkManifests(folder.getRoot().toPath(), StandardCharsets.UTF_16, warnings, Collections.emptyList());
    assertFalse(warnings.contains(BagitWarning.MISSING_TAG_MANIFEST));
  }
  
  @Test
  public void testLinterIgnoreWeakChecksum() throws Exception{
    Set<BagitWarning> warnings = new HashSet<>();

    ManifestChecker.checkManifests(rootDir, StandardCharsets.UTF_16, warnings, Arrays.asList(BagitWarning.WEAK_CHECKSUM_ALGORITHM));
    
    assertFalse(warnings.contains(BagitWarning.WEAK_CHECKSUM_ALGORITHM));
  }
  
  @Test
  public void testLinterIgnoreCase() throws Exception{
    Set<BagitWarning> warnings = new HashSet<>();

    ManifestChecker.checkManifests(rootDir, StandardCharsets.UTF_16, warnings, Arrays.asList(BagitWarning.DIFFERENT_CASE));
    
    assertFalse(warnings.contains(BagitWarning.DIFFERENT_CASE));
  }
  
  @Test
  public void testLinterNormalization() throws Exception{
    Set<BagitWarning> warnings = new HashSet<>();

    ManifestChecker.checkManifests(rootDir, StandardCharsets.UTF_16, warnings, Arrays.asList(BagitWarning.DIFFERENT_NORMALIZATION));
    
    assertFalse(warnings.contains(BagitWarning.DIFFERENT_NORMALIZATION));
  }
  
  @Test
  public void testLinterIgnoreBagWithinABag() throws Exception{
    Set<BagitWarning> warnings = new HashSet<>();

    ManifestChecker.checkManifests(rootDir, StandardCharsets.UTF_16, warnings, Arrays.asList(BagitWarning.BAG_WITHIN_A_BAG));
    
    assertFalse(warnings.contains(BagitWarning.BAG_WITHIN_A_BAG));
  }
  
  @Test
  public void testLinterIgnoreRelativePath() throws Exception{
    Set<BagitWarning> warnings = new HashSet<>();

    ManifestChecker.checkManifests(rootDir, StandardCharsets.UTF_16, warnings, Arrays.asList(BagitWarning.LEADING_DOT_SLASH));
    
    assertFalse(warnings.contains(BagitWarning.LEADING_DOT_SLASH));
  }
  
  @Test
  public void testLinterIgnoreNonStandardChecksumAlgorithm() throws Exception{
    Set<BagitWarning> warnings = new HashSet<>();

    ManifestChecker.checkManifests(rootDir, StandardCharsets.UTF_16, warnings, Arrays.asList(BagitWarning.NON_STANDARD_ALGORITHM));
    
    assertFalse(warnings.contains(BagitWarning.NON_STANDARD_ALGORITHM));
  }
  
  @Test
  public void testLinterIgnoreOSSpecificFiles() throws Exception{
    Set<BagitWarning> warnings = new HashSet<>();

    ManifestChecker.checkManifests(rootDir, StandardCharsets.UTF_16, warnings, Arrays.asList(BagitWarning.OS_SPECIFIC_FILES));
    
    assertFalse(warnings.contains(BagitWarning.OS_SPECIFIC_FILES));
  }

  @Test
  public void testOSSpecificFilesRegex(){
    String regex = ManifestChecker.getOsFilesRegex();
    String[] osFilesToTest = new String[]{"data/Thumbs.db", "data/.DS_Store", "data/.Spotlight-V100", "data/.Trashes", 
        "data/._.Trashes", "data/.fseventsd"};
    
    for(String osFileToTest : osFilesToTest){
      assertTrue(osFileToTest + " should match regex but it doesn't", osFileToTest.matches(regex));
    }
  }
  
  @Test(expected=InvalidBagitFileFormatException.class)
  public void testParsePath() throws InvalidBagitFileFormatException{
    ManifestChecker.parsePath("foobarham");
  }
  
  @Test
  public void testCheckAlgorthm(){
    Set<BagitWarning> warnings;
    String[] algorithms = new String[]{"md5", "sha", "sha1", "sha224", "sha256", "sha384"};
    for(String algorithm : algorithms){
     warnings = new HashSet<>();
     ManifestChecker.checkAlgorthm(algorithm, warnings, Collections.emptyList());
       assertTrue(warnings.contains(BagitWarning.WEAK_CHECKSUM_ALGORITHM));
    }
  }
}
