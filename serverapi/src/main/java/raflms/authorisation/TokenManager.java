package raflms.authorisation;


import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TokenManager {

    private final TokenRepository tokenRepository;

    public TokenManager(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public String generateToken() {
        String tokenValue = UUID.randomUUID().toString();
        return tokenValue;
    }

    /*

    public String  deleteToken(String studentId, String tokenValue) {
        var result = tokenRepository.findById(studentId);
        if (result.isEmpty()){
            return String.format("Token for the student with id: %s doesn't exist in the db", studentId);
        }
        if (!tokenValue.equals(result.get().getValue())) {
            return String.format("Wrong token value provided: %s", tokenValue);
        }
        tokenRepository.deleteById(studentId);
        return String.format("Token for the student with id: %s has been successfully deleted", studentId);
    }

    public boolean verifyToken(String studentId, String tokenValue) {
        var result = tokenRepository.findById(studentId);
        if (result.isEmpty()){
            return false;
        }

        if (!tokenValue.equals(result.get().getValue())) {
            return false;
        }


        */

}
