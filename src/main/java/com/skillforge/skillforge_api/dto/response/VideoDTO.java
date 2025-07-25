package com.skillforge.skillforge_api.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoDTO {
    private Long id;
    private int videoLibraryId;
    private String guid;
    private String title;
    private boolean isPublic;
    private double length;
    private int status;
    private int rotation;
    private String availableResolutions;
    private boolean hasMP4Fallback;
    private String collectionId;
    private String thumbnailFileName;
    private String thumbnailUrl;
    private String videoPlaylistUrl;
    private String category;
    private boolean jitEncodingEnabled;
    private Instant createdAt;

    @Override
    public String toString() {
        return "VideoDTO{" +
                "id=" + id +
                ", videoLibraryId=" + videoLibraryId +
                ", guid='" + guid + '\'' +
                ", title='" + title + '\'' +
                ", isPublic=" + isPublic +
                ", length=" + length +
                ", status=" + status +
                ", rotation=" + rotation +
                ", availableResolutions='" + availableResolutions + '\'' +
                ", hasMP4Fallback=" + hasMP4Fallback +
                ", collectionId='" + collectionId + '\'' +
                ", thumbnailFileName='" + thumbnailFileName + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", videoPlaylistUrl='" + videoPlaylistUrl + '\'' +
                ", category='" + category + '\'' +
                ", jitEncodingEnabled=" + jitEncodingEnabled +
                ", createdAt=" + createdAt +
                '}';
    }
}
