package com.igrowker.miniproject.Config;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class TokenBlacklist {
    // Conjunto que almacena los tokens que han sido invalidados (en la blacklist)
    private final Set<String> blacklistedTokens = new HashSet<>();

    public void blacklistToken(String token) {
        // Agregar el token al conjunto de tokens invalidados
        blacklistedTokens.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        // Retornar true si el token está en la blacklist, false en caso contrario
        return blacklistedTokens.contains(token);
    }
}