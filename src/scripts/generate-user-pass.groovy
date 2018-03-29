#!/usr/bin/env groovy

@Grab('org.springframework.security:spring-security-core')

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

def bcrypt = new BCryptPasswordEncoder()

print "Password: "
Scanner scanner = new Scanner(System.in)
def pass = scanner.nextLine()
println bcrypt.encode(pass)

