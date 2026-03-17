package com.cosama.artim.services;

import com.cosama.artim.dto.*;
import com.cosama.artim.models.User;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;

public interface AuthenticationService {

    User signup(SignUpDTO signUpDTO);

    User updateUser(long usr_id, SignUpDTO signUpDTO);

    User updatePassword(long usr_id, ResetPasswordDTO resetPasswordDTO);

    JwtAuthenticationResponse signin(SignInDTO signInDTO) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException;

    JwtAuthenticationResponse refreshToken(RefreshTokenDTO refreshTokenDTO) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException;
}
