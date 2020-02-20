package com.KOndziu.usercarservice.payload;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrackCarDTO {

    private Integer trackId;
    private String announcementURL;
}
