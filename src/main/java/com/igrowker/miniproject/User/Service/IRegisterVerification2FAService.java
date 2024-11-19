package com.igrowker.miniproject.User.Service;

import java.util.List;
import java.util.Optional;

import com.igrowker.miniproject.User.Dto.TwoFARegisterVerificationDTO;
import com.igrowker.miniproject.User.Model.RegisterVerification2FA;

public interface IRegisterVerification2FAService {

    public void sendEmailForVerification2FA(String email) throws Exception;
    public String verificate2FAtoken(TwoFARegisterVerificationDTO fa) throws Exception;
    public Optional<RegisterVerification2FA> validateEmail(String email);
    public List<RegisterVerification2FA> getAllRegisters();
}