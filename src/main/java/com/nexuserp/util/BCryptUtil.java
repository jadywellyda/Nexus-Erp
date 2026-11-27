package com.nexuserp.util;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptUtil {

    private static final int LOG_ROUNDS = 10;

    public static String hash(String senha) {
        return BCrypt.hashpw(senha, BCrypt.gensalt(LOG_ROUNDS));
    }

    public static boolean check(String senha, String hash) {
        if (hash == null || !hash.startsWith("$2a$")) {
            return false;
        }
        return BCrypt.checkpw(senha, hash);
    }
}
