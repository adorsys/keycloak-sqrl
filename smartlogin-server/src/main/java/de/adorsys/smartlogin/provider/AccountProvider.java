package de.adorsys.smartlogin.provider;

/**
 * Created by alexg on 07.12.16.
 */
public interface AccountProvider {
    
    byte[] fetchServerUnlockKey(byte[] idk);

    byte[] fetchVerifyUnlockKey(byte[] idk);

    boolean insertSqrlKeys(String userLogin, byte[] idk, byte[] serverUnlockKey, byte[] verifyUnlockKey);

    boolean updateSqrlKeys(byte[] previousIdentityKey, byte[] idk, byte[] serverUnlockKey, byte[] verifyUnlockKey);

    boolean updateSqrlServerAndVerifyUnlockKey(byte[] idk, byte[] serverUnlockKey, byte[] verifyUnlockKey);

    String checkIdentity(byte[] identityKey);

    boolean accountExists(String userId);

    boolean sqrlIdentityExists(String userId);

    void deleteSqrlIdentityIfExists(String userId);
}
