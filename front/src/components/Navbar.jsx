import React, { useContext } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

const Navbar = () => {
  const { isAuthenticated, logout } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav style={{ padding: '10px', background: '#eee', display: 'flex', gap: '15px' }}>
      <Link to="/register">Registro</Link>
      <Link to="/login">Login</Link>
      {isAuthenticated && (
        <>
          <Link to="/UserList">Ver Usuarios</Link>
          <button onClick={handleLogout}>Cerrar Sesión</button>
        </>
      )}
    </nav>
  );
};

export default Navbar;