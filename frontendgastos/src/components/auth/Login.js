import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom'; // Importa useNavigate
import { useAuth } from '../auth/AuthContext'; // Importa el contexto de autenticación
import { login } from "../../api"; // Asegúrate de que la ruta sea correcta

const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate(); // Obtiene la función navigate
  const { login: loginContext } = useAuth(); // Obtén la función login del contexto

  const handleLogin = async (event) => {
    event.preventDefault();
    try {
      const { token } = await login(username, password);
      loginContext(token, username); // Llama a la función login del contexto
      // Redirige a la página de inicio
      navigate('/home'); // Ajusta la ruta según tu configuración de enrutamiento
    } catch (error) {
      setError(error.message);
    }
  };
  const handleRegisterClick = () => {
    navigate('/register');
  };

  return (
    <div>
      <h2>Login</h2>
      <form onSubmit={handleLogin}>
        <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <button type="submit">Login</button>
      </form>
      <button onClick={handleRegisterClick}>Registrarse</button>
      {error && <p>{error}</p>}
    </div>
  );
};

export default Login;
