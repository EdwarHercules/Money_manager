import React from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './components/auth/AuthContext'; // Asegúrate de que las rutas sean correctas
import Login from './components/auth/Login';
import Home from './pages/Home';
import Sidebar from './components/sidebar'; // Ajusta la ruta según tu estructura
import Register from './components/auth/Register'; // Importa el componente de registro
import CrearCuenta from './components/Cuentas/CrearCuenta';
import Gestion from './components/Movimientos/Gestion';

const App = () => {
  return (
    <AuthProvider>
      <Router>
        <Layout>
          <Routes>
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/home" element={<PrivateRoute><Home /></PrivateRoute>} />
            <Route path="/gestion" element={<PrivateRoute><Gestion /></PrivateRoute>} />
            <Route path='/crear-cuenta' element={<PrivateRoute><CrearCuenta/></PrivateRoute>}/>
            {/* Añade más rutas aquí según sea necesario */}
            <Route path="*" element={<Navigate to="/login" />} />
          </Routes>
        </Layout>
      </Router>
    </AuthProvider>
  );
};

const Layout = ({ children }) => {
  const { authData } = useAuth();
  return (
    <div>
      {authData.token && <Sidebar />}
      <div>{children}</div>
    </div>
  );
};

const PrivateRoute = ({ children }) => {
  const { authData } = useAuth();
  return authData.token ? children : <Navigate to="/login" />;
};

export default App;
