package com.studygenai;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.*;
@RestController @RequestMapping("/api/auth") @CrossOrigin(origins={"http://localhost:5173","http://127.0.0.1:5173"})
public class AuthController {
  private final AppUserRepository users; private final BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
  public AuthController(AppUserRepository users){this.users=users;}
  @PostMapping("/signup") public ResponseEntity<?> signup(@RequestBody Map<String,String> body){String name=body.getOrDefault("name","").trim(),email=body.getOrDefault("email","").trim().toLowerCase(),password=body.getOrDefault("password",""); if(name.isBlank()||email.isBlank()||password.length()<6)return ResponseEntity.badRequest().body(Map.of("message","Name, email, and a 6+ character password are required.")); if(users.findByEmailIgnoreCase(email).isPresent())return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message","An account with this email already exists.")); AppUser user=users.save(new AppUser(name,email,encoder.encode(password))); return ResponseEntity.ok(Map.of("name",user.getName(),"email",email));}
  @PostMapping("/signin") public ResponseEntity<?> signin(@RequestBody Map<String,String> body){String email=body.getOrDefault("email","").trim().toLowerCase(),password=body.getOrDefault("password",""); if(email.equals("demo@studygen.ai")&&password.equals("demo123"))return ResponseEntity.ok(Map.of("name","Demo Student","email",email,"demo",true)); Optional<AppUser> match=users.findByEmailIgnoreCase(email); if(match.isEmpty()||!encoder.matches(password,match.get().getPasswordHash()))return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message","Email or password is incorrect.")); return ResponseEntity.ok(Map.of("name",match.get().getName(),"email",email,"demo",false));}
}
