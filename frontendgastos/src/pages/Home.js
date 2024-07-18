import React, { useState, useEffect } from 'react';
import { useAuth } from '../components/auth/AuthContext';
import { obtenerNombrePersona, obtenerCuentas } from "../api";

const Home = () => {
  const { authData } = useAuth();
  const [nombrePersona, setNombrePersona] = useState('');
  const [cuentas, setCuentas] = useState([]);
  const [selectedCuenta, setSelectedCuenta] = useState('');

  useEffect(() => {
    if (authData.token && authData.nombre) {
      obtenerNombrePersona(authData.nombre, authData.token)
        .then(nombre => {
          setNombrePersona(nombre);
          return obtenerCuentas(authData.nombre, authData.token);
        })
        .then(cuentas => {
          setCuentas(cuentas);
        })
        .catch(error => {
          console.error('Error:', error);
        });
    }
  }, [authData]);

  const handleCuentaChange = (event) => {
    setSelectedCuenta(event.target.value);
  };

  return (
    <div>
      {authData.token ? (
        <div>
          <h2>Bienvenido {nombrePersona}</h2>
          <select onChange={handleCuentaChange} value={selectedCuenta}>
            <option value="">Selecciona una cuenta</option>
            {cuentas.map(cuenta => (
              <option key={cuenta.id} value={cuenta.id}>{cuenta.numeroCuenta}</option>
            ))}
          </select>
        </div>
      ) : (
        <p>Redirigiendo al login...</p>
      )}
    </div>
  );
};

export default Home;
