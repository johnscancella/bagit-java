package gov.loc.repository.bagit.domain;

import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;

/**
 * An individual item to fetch as specified by 
 * <a href="https://tools.ietf.org/html/draft-kunze-bagit-13#section-2.2.3">https://tools.ietf.org/html/draft-kunze-bagit-13#section-2.2.3</a>
 */
public final class FetchItem {
  /**
   * The url from which the item can be downloaded
   */
  public final URL url;
  
  /**
   * The length of the file in octets
   */
  public final Long length; 
  
  /**
   * The path where the fetched item should be put
   */
  public final Path path;
  
  private transient String cachedString;
  
  /**
   * 
   * @param url the {@link URL} of the file
   * @param length the file length in bytes, -1 or null to not specify the length
   * @param path the path in the bag where the file belongs
   */
  public FetchItem(final URL url, final Long length, final Path path){
    this.url = url;
    this.length = length;
    this.path = path;
  }
  
  private String internalToString() {
    final StringBuilder sb = new StringBuilder();
    sb.append(url).append(' ');
    
    if(length == null || length < 0){
      sb.append("- ");
    }
    else{
      sb.append(length).append(' ');
    }
    
    sb.append(path);
      
    return sb.toString();
  }

  @Override
  public String toString() {
    if(cachedString == null){
      cachedString = internalToString();
    }
    
    return cachedString;
  }

  public URL getUrl() {
    return url;
  }

  public Long getLength() {
    return length;
  }

  public Path getPath() {
    return path;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(url) + Objects.hash(length) + Objects.hash(path);
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj){
      return true;
    }
    if (obj == null){
      return false;
    }
    if (!(obj instanceof FetchItem)){
      return false;
    }
    
    final FetchItem other = (FetchItem) obj;
    
    return Objects.equals(url, other.getUrl()) && Objects.equals(length, other.getLength()) && Objects.equals(path, other.getPath()); 
  }
}
