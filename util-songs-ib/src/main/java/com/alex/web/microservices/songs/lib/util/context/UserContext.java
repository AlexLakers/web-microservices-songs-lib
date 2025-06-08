package com.alex.web.microservices.songs.lib.util.context;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserContext {
    private String authorizationToken;
    private String tracingId;

}
