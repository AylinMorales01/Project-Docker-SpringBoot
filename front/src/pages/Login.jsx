import { useState, useContext } from 'react';
import api from '../api/axios';
import { AuthContext } from '../context/AuthContext';

const Login = () => {
  const [form, setForm] = useState({ email: '', password: '' });
  const { login } = useContext(AuthContext);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const { data } = await api.post('/auth/login', form);
      if (data.jwt) {
        login(data.jwt);
        alert(data.message);
        window.location.href = '/UserList';
      }
    } catch (err) {
      alert("Error: " + err.response?.data?.message || "Fallo el login");
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h2>Login</h2>
      <input type="email" placeholder="Email" onChange={e => setForm({...form, email: e.target.value})} />
      <input type="password" placeholder="Pass" onChange={e => setForm({...form, password: e.target.value})} />
      <button type="submit">Entrar</button>
    </form>
  );
};

export default Login;