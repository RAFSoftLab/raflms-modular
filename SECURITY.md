# RAF LMS — Bezbednosna konfiguracija

Ovaj dokument opisuje sve bezbednosne mehanizme uvedene u sistemu i šta je potrebno podesiti na serveru da bi sistem ispravno radio.

---

## Obavezne environment varijable

Pre pokretanja aplikacija, na serveru moraju biti postavljene sledeće env varijable.

### serverapi

| Varijabla | Opis | Primer |
|-----------|------|--------|
| `RAF_DB_PASSWORD` | Lozinka za MySQL bazu (`raflms`) | `RAF_DB_PASSWORD=MojaJakaLozinka123!` |
| `RAF_AUTH_TOKEN` | Bearer token koji plugin i teacher dashboard šalju u `Authorization` headeru | `RAF_AUTH_TOKEN=neki-tajni-token-uuid` |
| `RAF_ALLOWED_ORIGIN` | Origin sa kog teacher dashboard šalje zahteve (CORS) | `RAF_ALLOWED_ORIGIN=http://192.168.124.24:3000` |

### activitytrackingapi

| Varijabla | Opis | Primer |
|-----------|------|--------|
| `RAF_DB_PASSWORD` | Lozinka za MySQL bazu (`raflmstracking`) | `RAF_DB_PASSWORD=MojaJakaLozinka123!` |
| `RAF_AUTH_TOKEN` | Isti token kao za serverapi | `RAF_AUTH_TOKEN=neki-tajni-token-uuid` |
| `RAF_ALLOWED_ORIGIN` | Origin sa kog teacher dashboard šalje zahteve (CORS) | `RAF_ALLOWED_ORIGIN=http://192.168.124.24:3000` |
| `RAF_RETENTION_DAYS` | Broj dana koliko se čuvaju tracking podaci pre automatskog brisanja (opciono, default: 730) | `RAF_RETENTION_DAYS=730` |

---

## Postavljanje env varijabli na serveru

### Opcija A — systemd servis (preporučeno)

Ako se aplikacije pokreću kao systemd servisi, dodaj env varijable u servis fajl:

```bash
sudo systemctl edit raflms-serverapi
```

U editor koji se otvori dodaj:

```ini
[Service]
Environment="RAF_DB_PASSWORD=MojaJakaLozinka123!"
Environment="RAF_AUTH_TOKEN=neki-tajni-token-uuid"
Environment="RAF_ALLOWED_ORIGIN=http://192.168.124.24:3000"
```

Zatim restartuj servis:

```bash
sudo systemctl daemon-reload
sudo systemctl restart raflms-serverapi
```

Ponovi isto za `raflms-activitytracking` servis, uz dodatak `RAF_RETENTION_DAYS`.

### Opcija B — `/etc/environment` (globalno za ceo sistem)

```bash
sudo nano /etc/environment
```

Dodaj linije:

```
RAF_DB_PASSWORD=MojaJakaLozinka123!
RAF_AUTH_TOKEN=neki-tajni-token-uuid
RAF_ALLOWED_ORIGIN=http://192.168.124.24:3000
RAF_RETENTION_DAYS=730
```

Varijable postaju aktivne pri sledećem logovanju ili:

```bash
source /etc/environment
```

### Opcija C — ručno pokretanje (razvoj/testiranje)

```bash
export RAF_DB_PASSWORD=MojaJakaLozinka123!
export RAF_AUTH_TOKEN=neki-tajni-token-uuid
export RAF_ALLOWED_ORIGIN=http://192.168.124.24:3000
export RAF_RETENTION_DAYS=730

java -jar serverapi/build/libs/serverapi.jar
```

---

## Generisanje auth tokena

Token treba da bude nepredvidiv. Preporučen način generisanja:

```bash
openssl rand -hex 32
```

Isti token se podešava i u IntelliJ pluginu — u konfiguracionom fajlu plugina (ili `RafConfig`).

---

## Bezbednosni mehanizmi — pregled

### Autentifikacija (serverapi + activitytrackingapi)

Svi zaštićeni endpointi zahtevaju `Authorization: Bearer <token>` header.  
Token se proverava u `TokenAuthenticationFilter` i mora da odgovara vrednosti `RAF_AUTH_TOKEN` env varijable.

Zaštićeni endpointi:
- `serverapi`: `/upload/project`, `/download/studentassignment/**`, `/student/**`
- `activitytrackingapi`: svi endpointi

### Rate limiting (serverapi)

`RateLimitFilter` ograničava svakog klijenta (po IP adresi) na **30 zahteva u 60 sekundi**.  
Kada se limit prekorači, server vraća `HTTP 429 Too Many Requests`.

### Path traversal zaštita (serverapi)

Sve operacije sa fajlovima (`upload`, `download`) validiraju da putanja ostaje unutar `projectrootdir` direktorijuma.  
Sanitizacija imena fajla uklanja sve osim alfanumeričkih znakova, tački, crtica i podvlaka.

### IDOR zaštita — download endpoint (serverapi)

Endpoint `/download/studentassignment/{publicId}` prima UUID (`publicId`) umesto sekvencijalnog `Long` ID-a.  
UUID se automatski generiše pri kreiranju svake `StudentSubmission` i nije predvidiv.

### CORS (serverapi + activitytrackingapi)

Zahtevi iz browsera se prihvataju samo sa origina definisanog u `RAF_ALLOWED_ORIGIN`.  
IntelliJ plugin nije browser — na njega se CORS ne primenjuje.

### Sigurnosni HTTP headeri

Oba servisa dodaju sledeće headere na svaki odgovor:

| Header | Vrednost |
|--------|----------|
| `X-Content-Type-Options` | `nosniff` |
| `X-Frame-Options` | `DENY` |
| `X-XSS-Protection` | `1; mode=block` |
| `Referrer-Policy` | `no-referrer` |
| `Cache-Control` | `no-store` |

### GDPR — zadržavanje podataka (activitytrackingapi)

`DataRetentionService` automatski briše tracking podatke starije od `RAF_RETENTION_DAYS` dana.  
Brisanje se izvršava jednom dnevno u ponoć.  
Default vrednost je 730 dana (2 godine).

### Hibernate ddl-auto

Oba servisa koriste `ddl-auto=validate` — Hibernate samo proverava da li šema odgovara modelu, ali je **ne menja**.  
Promene šeme se rade isključivo ručnim migracijama.

---

## IntelliJ plugin — student

Plugin (`student-plugin`) šalje tracking podatke na `activitytrackingapi`.  
Konfiguracija URL-a i tokena se podešava u `RafConfig` klasi plugina.

### Šta plugin prikuplja

| Podatak | Šta se šalje |
|---------|-------------|
| Greške u kodu | Kategorija (`UNRESOLVED_SYMBOL`, `TYPE_ERROR`, itd.) — **ne tekst greške** |
| Autocomplete | Tip predloga (`METHOD`, `FIELD`, `CLASS`, itd.) — **ne sadržaj** |
| Putanje fajlova | Relativna putanja unutar projekta — **ne apsolutna putanja** |
| Lepljenje teksta | Broj karaktera — **ne sadržaj clipboarda** |

Pre početka praćenja, student mora da da eksplicitnu saglasnost u dijalogu koji se prikazuje pri prijavi.
