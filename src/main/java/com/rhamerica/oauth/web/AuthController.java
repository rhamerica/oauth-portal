package com.rhamerica.oauth.web;

import com.rhamerica.oauth.SecurityService;
import com.rhamerica.oauth.SessionService;
import com.rhamerica.oauth.UserService;
import com.rhamerica.oauth.data.ClientRepository;
import com.rhamerica.oauth.data.model.AuthRequest;
import com.rhamerica.oauth.data.model.Client;
import com.rhamerica.oauth.data.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;
import org.thymeleaf.util.StringUtils;

import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    SessionService sessionService;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    UserService userService;


    @Autowired
    SecurityService securityService;

    @GetMapping("/auth")
    public ModelAndView authentication(@ModelAttribute("auth") AuthRequest authRequest) {
        if ("code".equals(authRequest.getResponseType())) {
            sessionService.setAuthRequest(authRequest);
            if (sessionService.isLoggedIn()) {
                return respondAuthenticated();
            }
            return loginForm(authRequest.getLoginHint());
        }
        return invalidRequest();
    }

    @PostMapping("/auth/dologin")
    public ModelAndView doLogin(
            @RequestParam(value = "email", defaultValue = "") String email,
            @RequestParam(value = "password", defaultValue = "") String password
    ) {
        Optional<User> optionalUser = userService.findUserByEmailAndPassword(email, password);
        if (!optionalUser.isPresent()) {
            return loginForm(email);
        }
        User user = optionalUser.get();

        // setup session
        sessionService.setLoggedIn(true);
        sessionService.setUserId(user.getUserId());
        return respondAuthenticated();
    }

    private ModelAndView respondAuthenticated() {
        if (!sessionService.isLoggedIn()) {
            return invalidRequest();
        }

        AuthRequest authRequest = sessionService.getAuthRequest();
        if (authRequest == null) {
            return invalidRequest();
        }

        // TODO: check if client is associated with the user
        // for now just assume it is
        Optional<Client> optionalClient = clientRepository.findById(authRequest.getClientId());
        if (!optionalClient.isPresent()) {
            return invalidRequest();
        }
        Client client = optionalClient.get();

        Optional<User> userOptional = userService.findById(sessionService.getUserId());
        if (!userOptional.isPresent()) {
            return invalidRequest();
        }
        User user = userOptional.get();

        String code = securityService.generateAuthCode(client, user);

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(client.getRedirectUri());

        // state
        if (!StringUtils.isEmpty(authRequest.getState())) {
            uriComponentsBuilder.queryParam("state", authRequest.getState());
        }

        // code
        uriComponentsBuilder.queryParam("code", code);

        String redirectTo = uriComponentsBuilder.build().toUriString();

        return new ModelAndView(String.format("redirect:%s", redirectTo));
    }

    public ModelAndView loginForm(String loginHint) {
        ModelAndView mav = new ModelAndView("tpl/auth");
        mav.addObject("email", loginHint);
        return mav;
    }

    @ModelAttribute("auth")
    protected AuthRequest loadAuthRequest(
            @RequestParam(value = "client_id", defaultValue = "") String client_id,
            @RequestParam(value = "response_type", defaultValue = "") String response_type,
            @RequestParam(value = "scope", defaultValue = "") String scope,
            @RequestParam(value = "redirect_uri", defaultValue = "") String redirect_uri,
            @RequestParam(value = "login_hint", defaultValue = "") String loginHint,
            @RequestParam(value = "state", defaultValue = "") String state
    ) {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setClientId(client_id);
        authRequest.setResponseType(response_type);
        authRequest.setScope(scope);
        authRequest.setRedirectUri(redirect_uri);
        authRequest.setLoginHint(loginHint);
        authRequest.setState(state);
        return authRequest;
    }

    private ModelAndView invalidRequest() {
        ModelAndView mav = new ModelAndView("tpl/invalidrequest");
        mav.setStatus(HttpStatus.BAD_REQUEST);
        return mav;
    }

}