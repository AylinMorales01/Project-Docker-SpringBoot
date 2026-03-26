import { useState } from 'react';
import api from '../api/axios';

const Register = () => {
  const [form, setForm] = useState({ username: '', email: '', password: '' });

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const { data } = await api.post('/auth/register', form);
      alert(data.message);
    } catch (err) {
      alert("Error en registro");
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h2>Registro</h2>
      <input type="text" placeholder="Username" onChange={e => setForm({...form, username: e.target.value})} />
      <input type="email" placeholder="Email" onChange={e => setForm({...form, email: e.target.value})} />
      <input type="password" placeholder="Pass" onChange={e => setForm({...form, password: e.target.value})} />
      <button type="submit">Registrar</button>
    </form>
  );
};

export default Register;