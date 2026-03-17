package com.cosama.artim.services;

import org.springframework.security.core.userdetails.UserDetails;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPublicKey;
import java.util.Map;

public interface JWTService
{
    String extractUserName(String token) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException;
    String generateToken(UserDetails userDetails) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException;
    boolean isTokenValid(String token, UserDetails userDetails) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException;
    String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException;
}
