package com.alex.web.microservices.songs.lib.author.util;

import lombok.*;

/**
 * it describes context for specific request.
 * This class not used because it moved in standalone util-service  'util-song-lib';
 */

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserContext {
    private String authorizationToken;
    private String tracingId;

}
