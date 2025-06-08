package com.alex.web.microservices.songs.lib.author.util;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserContext {
    private String authorizationToken;
    private String tracingId;

}
