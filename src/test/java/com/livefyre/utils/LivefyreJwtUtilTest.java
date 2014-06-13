package com.livefyre.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import net.oauth.jsontoken.JsonToken;

import org.junit.Test;

import com.google.gson.JsonObject;
import com.livefyre.utils.LivefyreJwtUtil;

public class LivefyreJwtUtilTest {
    private static final String CHECKSUM = "323f0074333c0c8c01951c0b3bf5f794";
    private static final String TEST_SECRET = "testkeytest";

    @Test
    public void testUserAuthToken() {
        String token = null;
        JsonToken json = null;
        try {
            token = LivefyreJwtUtil.getJwtUserAuthToken("test.fyre.com", TEST_SECRET, "some", "user", 86400);
            json = LivefyreJwtUtil.decodeJwt(TEST_SECRET, token);
        } catch (InvalidKeyException e) {
            fail("shouldn't be an issue encoding/decoding");
        } catch (SignatureException e) {
            fail("shouldn't be an issue encoding/decoding");
        }
        assertNotNull(token);
        assertNotNull(json);
        JsonObject jsonObj = json.getPayloadAsJsonObject();
        assertEquals("test.fyre.com", jsonObj.get("domain").getAsString());
        assertEquals("some", jsonObj.get("user_id").getAsString());
        assertEquals("user", jsonObj.get("display_name").getAsString());
        assertNotNull(jsonObj.get("expires"));
    }
    
    @Test
    public void testCollectionMetaToken() {
        String token = null;
        JsonToken json = null;
        try {
            token = LivefyreJwtUtil.getJwtCollectionMetaToken(TEST_SECRET, "title", "tags", "url", "id", "reviews");
            json = LivefyreJwtUtil.decodeJwt(TEST_SECRET, token);
        } catch (InvalidKeyException e) {
            fail("shouldn't be an issue encoding/decoding");
        } catch (SignatureException e) {
            fail("shouldn't be an issue encoding/decoding");
        }
        
        assertNotNull(token);
        assertNotNull(json);
        JsonObject jsonObj = json.getPayloadAsJsonObject();
        assertEquals("title", jsonObj.get("title").getAsString());
        assertEquals("url", jsonObj.get("url").getAsString());
        assertEquals("tags", jsonObj.get("tags").getAsString());
        assertEquals("id", jsonObj.get("articleId").getAsString());
        assertEquals("reviews", jsonObj.get("type").getAsString());
    }
    
    @Test
    public void testChecksum() {
        String checksum = null;
        try {
            checksum = LivefyreJwtUtil.getChecksum("title", "url", "tags");
        } catch (NoSuchAlgorithmException e) {
            fail("shouldn't fail to create the checksum");
        }
        
        assertNotNull(checksum);
        assertEquals(CHECKSUM, checksum);
    }
}