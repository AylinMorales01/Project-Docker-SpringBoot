import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import Login from './pages/Login';
import Register from './pages/Register';
import UserList from './pages/UserList';
import Navbar from './components/Navbar'; // Importar el Navbar

function App() {
  return (
    <AuthProvider>
      <Router>
        <Navbar /> {/* El menú siempre visible */}
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/UserList" element={<UserList />} />
          <Route path="/" element={<Login />} />
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;