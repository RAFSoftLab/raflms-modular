package raflms.authorisation;

// RISK-26 fix: klasa uklonjena - generateToken() vracala UUID koji se nigde nije cuvao
// niti proveravao, a zakomentarisana logika verifyToken/deleteToken obmanjivala je citaoca
// da autorizacija postoji. Stvarna auth logika je u TokenAuthenticationFilter.
