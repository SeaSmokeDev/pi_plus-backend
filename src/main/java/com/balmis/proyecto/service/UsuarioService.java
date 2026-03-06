
package com.balmis.proyecto.service;

import com.balmis.proyecto.model.Usuario;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.balmis.proyecto.repository.UsuarioRepository;


@Service
public class UsuarioService {
    
    @Autowired
    public UsuarioRepository usuarioRepository;
    
    // ************************
    // CONSULTAS
    // ************************  
    @Transactional(readOnly = true) 
    public List<Usuario> findAll() {
        return usuarioRepository.findSqlAll();
    }

    @Transactional(readOnly = true) 
    public Usuario findById(int depId) {
        return usuarioRepository.findSqlById(depId);
    }

    @Transactional(readOnly = true) 
    public Long count() {
        return usuarioRepository.count();
    }
    
    @Transactional (readOnly = true)
    public List<Usuario> findByNombre(String usuarioNombre){
        return usuarioRepository.findSqlLikeNombre(usuarioNombre);
    }
    
    // ************************
    // ACTUALIZACIONES
    // ************************  

    @Transactional
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
    
    @Transactional
    public Usuario update(int id, Usuario usuarioDetails) {
        Usuario findUsuario = usuarioRepository.findSqlById(id);
        if (findUsuario==null) {
            throw new RuntimeException("Usuario no encontrado");
        }
        
        if (usuarioDetails.getNombre()!= null) {
            findUsuario.setNombre(usuarioDetails.getNombre());
        }
        
        if (usuarioDetails.getApellido()!= null) {
            findUsuario.setApellido(usuarioDetails.getApellido());
        }
        
//        if (usuarioDetails.getNombreUsuario()!= null) {
//            findUsuario.setNombreUsuario(usuarioDetails.getNombreUsuario());
//        }
//        
//        if (usuarioDetails.getHashPassword()!= null) {
//            findUsuario.setHashPassword(usuarioDetails.getHashPassword());
//        }
        
        if (usuarioDetails.getRol()!= null) {
            findUsuario.setRol(usuarioDetails.getRol());
        }
        
        if (usuarioDetails.getLugarTrabajo()!= null) {
            findUsuario.setLugarTrabajo(usuarioDetails.getLugarTrabajo());
        }
        
        return usuarioRepository.save(findUsuario);
    }
    
    @Transactional
    public void deleteById(int id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }    
}
