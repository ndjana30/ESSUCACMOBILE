package com.ess.lms.Controllers;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ess.lms.Models.AuthResponseDto;
import com.ess.lms.Models.LoginDto;
import com.ess.lms.Models.RegisterDto;
import com.ess.lms.Models.Role;
import com.ess.lms.Models.UserEntity;
import com.ess.lms.Repositories.RoleRepository;
import com.ess.lms.Repositories.UserRepository;
import com.ess.lms.Security.JWTGenerator;

@RestController
@RequestMapping("api/v1/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JWTGenerator jwtGenerator;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JWTGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping("user/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken
                        (loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Authentication usr = SecurityContextHolder.getContext().getAuthentication();
        String token = jwtGenerator.generateToken(authentication);
        return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);
    }


@GetMapping("user/get")
public Object getCurrentUser()
{
    // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Authentication user = SecurityContextHolder.getContext().getAuthentication();

    // Role lecturer=roleRepository.findByName("LECTURER").get();
    // Role student=roleRepository.findByName("STUDENT").get();
    // Role admin=roleRepository.findByName("ADMIN").get();
    
    return new ResponseEntity<>(user.getAuthorities().toArray()[0].toString(),HttpStatus.OK);



}
@GetMapping("user/{id}/name")
public Object getCurrentUserById(@PathVariable long id)
{
    Optional<UserEntity> user = userRepository.findById(id);
    if(user.isPresent())
    {
        return new ResponseEntity<String>(user.get().getUsername(),HttpStatus.OK);
    }
    return new ResponseEntity<>("User not existing",HttpStatus.BAD_REQUEST);
    


}

    @PostMapping("admin/register")
    public ResponseEntity<String> adminRegister(@RequestBody RegisterDto registerDto) 
    {
        if(userRepository.existsByUsername(registerDto.getUsername()))
        {
            return new ResponseEntity<>("Username already taken", HttpStatus.BAD_REQUEST);
        }
        try{
        UserEntity user = new UserEntity();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        Role roles= roleRepository.findByName("ADMIN").get();
        user.setRoles(Collections.singletonList(roles));
        // emailService.sendEmail(registerDto.getUsername(), "CM CHICKEN REGISTRATION","<HTML><body><h1>Sucessfully registered as admin </h1>"+registerDto.getUsername()+"</body></HTML>" );
        userRepository.save(user);

        return new ResponseEntity<>("User registered success with Admin role",
                HttpStatus.OK);
        }
        catch(Exception ex)
        {
            return new ResponseEntity<>(ex.getLocalizedMessage(),HttpStatus.OK);
        }
    }
    @PostMapping("student/register")
    public ResponseEntity<String> clientRegister(@RequestBody RegisterDto registerDto) 
    {
        if(userRepository.existsByUsername(registerDto.getUsername()))
        {
            return new ResponseEntity<>("Username already taken", HttpStatus.BAD_REQUEST);
        }
        UserEntity user = new UserEntity();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Role roles= roleRepository.findByName("STUDENT").get();
        user.setRoles(Collections.singletonList(roles));
        userRepository.save(user);

        return new ResponseEntity<>("User registered success with STUDENT status",
                HttpStatus.OK);
    }

    @PostMapping("lecturer/register")
    public ResponseEntity<String> lecturerRegister(@RequestBody RegisterDto registerDto) 
    {
        if(userRepository.existsByUsername(registerDto.getUsername()))
        {
            return new ResponseEntity<>("Username already taken", HttpStatus.BAD_REQUEST);
        }
        UserEntity user = new UserEntity();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Role roles= roleRepository.findByName("LECTURER").get();
        user.setRoles(Collections.singletonList(roles));
        userRepository.save(user);

        return new ResponseEntity<>("User registered success with LECTURER status",
                HttpStatus.OK);
    }
}