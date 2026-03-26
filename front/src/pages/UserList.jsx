import React, { useEffect, useState } from 'react';
import api from '../api/axios';

const UserList = () => {
  const [users, setUsers] = useState([]);
  const [error, setError] = useState("");

  const fetchUsers = async () => {
    try {
      // Nota: Tu controlador tiene @RequestMapping("users") y el método @GetMapping("get-users")
      const { data } = await api.get('/users/get-users');
      setUsers(data);
    } catch (err) {
      setError("No se pudieron cargar los usuarios. ¿Estás logueada?");
      console.error(err);
    }
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  return (
    <div style={{ padding: '20px' }}>
      <h2>Lista de Usuarios (Desde Spring Boot)</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <button onClick={fetchUsers}>Actualizar Lista</button>
      
      <table border="1" style={{ marginTop: '10px', width: '100%', textAlign: 'left' }}>
        <thead>
          <tr>
            <th>ID</th>
            <th>Email</th>
          </tr>
        </thead>
        <tbody>
          {users.map((user) => (
            <tr key={user.id}>
              <td>{user.id}</td>
              <td>{user.email}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default UserList;