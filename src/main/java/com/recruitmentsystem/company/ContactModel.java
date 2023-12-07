package com.recruitmentsystem.company;

import lombok.Builder;

@Builder
public record ContactModel (
        String email,
        String phoneNumber,
        String website,
        String facebookUrl,
        String youtubeUrl,
        String linkedinUrl
) {
}
