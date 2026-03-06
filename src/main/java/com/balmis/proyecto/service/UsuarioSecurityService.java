
package com.balmis.proyecto.service;

import com.balmis.proyecto.model.UsuarioSecurity;
import com.balmis.proyecto.repository.UsuarioSecurityRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class UsuarioSecurityService {
    
    @Autowired
    public UsuarioSecurityRepository userRepository;
    
    // ************************
    // CONSULTAS
    // ************************  
    @Transactional(readOnly = true) 
    public List<UsuarioSecurity> findAll() {
        return userRepository.findSqlAll();
    }
    
    @Transactional(readOnly = true) 
    public UsuarioSecurity findById(int userId) {
        return userRepository.findSqlById(userId);
    }

    @Transactional(readOnly = true) 
    public Long count() {
        return userRepository.count();
    }    
    
    @Transactional(readOnly = true) 
    public List<UsuarioSecurity> findByIdGrThan(int userId) {
        return userRepository.findSqlByIdGrThan(userId);
    }
    
    // ************************
    // ACTUALIZACIONES
    // ************************  

    @Transactional
    public UsuarioSecurity save(UsuarioSecurity user) {
        return userRepository.save(user);
    }
    
    @Transactional
    public UsuarioSecurity update(int id, UsuarioSecurity userUpdate) {
        UsuarioSecurity usuario = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        if (userUpdate.getUsername() != null) {
            usuario.setUsername(userUpdate.getUsername());
        }
        if (userUpdate.getEmail() != null) {
            usuario.setEmail(userUpdate.getEmail());
        }
        if (userUpdate.getPassword() != null) {
            usuario.setPassword(userUpdate.getPassword());
        }
        usuario.setRol(userUpdate.getRol());
//        usuario.setAdministrador(userUpdate.isAdministrador());
//        usuario.setUsuario(userUpdate.isUsuario());
//        usuario.setInvitado(userUpdate.isInvitado());
        usuario.setActivado(userUpdate.isActivado());
        
        
        return userRepository.save(usuario);
    }
    
    @Transactional
    public void deleteById(int id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        userRepository.deleteById(id);
    }        
}
