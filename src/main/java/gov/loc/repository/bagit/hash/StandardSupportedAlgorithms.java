package gov.loc.repository.bagit.hash;

/**
 * The standard algorithms that are supported "out of the box" in bagit
 */
public enum StandardSupportedAlgorithms implements SupportedAlgorithm{
  MD5("MD5"),
  SHA1("SHA-1"),
  SHA256("SHA-256"),
  SHA512("SHA-512");

  private final String messageDigestName;
  
  private StandardSupportedAlgorithms(final String messageDigestName){
    this.messageDigestName = messageDigestName;
  }

  @Override
  public String getMessageDigestName() {
    return messageDigestName;
  }

  @SuppressWarnings({"PMD.UseLocaleWithCaseConversions"})
  @Override
  public String getBagitName() {
    return name().toLowerCase();
  }
}
