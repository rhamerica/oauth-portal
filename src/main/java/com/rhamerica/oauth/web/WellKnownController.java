package com.rhamerica.oauth.web;

import com.rhamerica.oauth.data.model.OpenidDiscovery;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(".well-known/openid-configuration")
public class WellKnownController {

    @GetMapping("/openid-configuration")
    public ResponseEntity<OpenidDiscovery> openidConfigurationo() {
        OpenidDiscovery doc = new OpenidDiscovery();

        //doc.setIssuer("http://oauth.rha");
        doc.setAuthorizationEndpoint("http://oauth.rha/auth");
        doc.setTokenEndpoint("http://oauth.rha/token");
        //doc.setResponseTypesSupported(Arrays.asList("code", "token"));
        //doc.setResponseModesSupported(Arrays.asList("query", "form_post")); // fragment not supported

        return ResponseEntity.ok(doc);
    }

}
