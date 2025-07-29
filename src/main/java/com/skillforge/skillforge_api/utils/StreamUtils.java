package com.skillforge.skillforge_api.utils;

import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

public class StreamUtils {

    private final BunnyTokenSigner tokenSigner;

    @Value("${bunny.stream.bunny-cdn-host}")
    private String bunnyCdnHost;

    //    @Value("${bunny.stream.bunny-auth-key}")
    private static String securityKey = "7aec43d5-f3be-440f-b740-55fd5e455cf4";

    public StreamUtils(BunnyTokenSigner tokenSigner) {
        this.tokenSigner = tokenSigner;
    }

    public String getVideoLink(String videoId) {
        var allowedPath = "/" + videoId + "/";
        var videoUrl = String.format("https://%s/%s/playlist.m3u8", bunnyCdnHost, videoId);
        return tokenSigner.signUrl(videoUrl, securityKey, allowedPath);
    }



//    public static String generateSignedUrl(String streamHostname, String path, String securityKey, long validDurationSeconds) throws Exception {
//        long expires = Instant.now().getEpochSecond() + validDurationSeconds;
//
//        // token_path được encode
//        String tokenPath = URLEncoder.encode(path, StandardCharsets.UTF_8.toString());
//
//        // Tạo chuỗi để hash: securityKey + path + expires
//        String baseToHash = securityKey + path + expires;
//
//        // SHA256 tạo raw bytes
//        Mac sha256 = Mac.getInstance("HmacSHA256");
//        sha256.init(new SecretKeySpec(new byte[0], "HmacSHA256")); // không dùng HMAC key, dùng raw SHA256: reset key
//        sha256.update(baseToHash.getBytes(StandardCharsets.UTF_8));
//        byte[] rawHash = sha256.doFinal();
//
//        // chuyển sang Base64 URL-safe
//        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(rawHash);
//
//        // Build final URL
//        return "https://" + streamHostname
//                + "/bcdn_token=" + token
//                + "&expires=" + expires
//                + "&token_path=" + tokenPath
//                + path;
//    }
}
