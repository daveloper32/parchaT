package com.developers.parchat.view.login;

public class LoginCredentialsUsuario {
    private String email;
    private String password;

    public LoginCredentialsUsuario(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
