package com.cmcc.vrp.boss.shangdong.email.model;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * EmailAuthenticator
 * @author luozuwu,qihang
 *
 */
public class EmailAuthenticator extends Authenticator {  
  
    private String username = null;  
  
    private String password = null;  
  
    public EmailAuthenticator(String username, String password) {  
        this.username = username;  
        this.password = password;  
    }  
  
    public PasswordAuthentication getPasswordAuthentication() {  
        return new PasswordAuthentication(username, password);  
    }  
}  