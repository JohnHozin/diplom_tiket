package chistTravel.tiket.service;

import chistTravel.tiket.db.entity.Bus;
import chistTravel.tiket.db.entity.RoleJPA;
import chistTravel.tiket.db.entity.UserJPA;
import chistTravel.tiket.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class UserJPAService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserJPA findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserJPA findUserById(Long id){
        Optional<UserJPA> user = userRepository.findById(id);
        return user.orElse(null);
    }

    public void deleteUserById(Long id){
        Optional<UserJPA> deleted = userRepository.findById(id);
        deleted.ifPresent(user -> userRepository.delete(user));
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserJPA user = findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                mapRolesToAuthorities(user.getRolesJPA()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<RoleJPA> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }

    public UserJPA saveUserJPA(UserJPA user){
        return userRepository.save(user);
    }

    public UserJPA updateUserJPA(Long id, UserJPA user){
        user.setId(id);
        return saveUserJPA(user);
    }

    public List<UserJPA> listAllUsers(){
        return userRepository.findAll();
    }
}
