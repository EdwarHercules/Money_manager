import React, { useState, useEffect } from 'react';
import { useAuth } from '../components/auth/AuthContext';
import { obtenerNombrePersona, obtenerCuentas } from "../api";
import { getUltimosMovimientos } from '../components/Movimientos/MovimientoService';
import { Link } from 'react-router-dom';

const Home = () => {
  const { authData } = useAuth();
  const [nombrePersona, setNombrePersona] = useState('');
  const [cuentas, setCuentas] = useState([]);
  const [selectedCuenta, setSelectedCuenta] = useState('');
  const [movimientos, setMovimientos] = useState([]);

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

  useEffect(() => {
    if (selectedCuenta && authData.token) {
      getUltimosMovimientos(selectedCuenta, authData.token)
        .then(movimientos => {
          setMovimientos(movimientos);
        })
        .catch(error => {
          console.error('Error al obtener los movimientos:', error);
        });
    }
  }, [selectedCuenta, authData.token]);

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
          <br />
          <Link to="/crear-cuenta">
            <button>Crear Nueva Cuenta</button>
          </Link>
          <br />
          {selectedCuenta && (
            <div>
              <h3>Últimos Movimientos</h3>
              {movimientos.length > 0 ? (
                <table>
                  <thead>
                    <tr>
                      <th>Fecha</th>
                      <th>Cliente</th>
                      <th>Numero Cuenta</th>
                      <th>Tipo</th>
                      <th>Saldo Inicial</th>
                      <th>Estado</th>
                      <th>Movimiento</th>
                      <th>Saldo Disponible</th>
                    </tr>
                  </thead>
                  <tbody>
                    {movimientos.map(movimiento => (
                      <tr key={movimiento.id}>
                        <td>{movimiento.fecha}</td>
                        <td>{movimiento.cliente}</td>
                        <td>{movimiento.numeroCuenta}</td>
                        <td>{movimiento.tipo}</td>
                        <td>{movimiento.saldoInicial}</td>
                        <td>{movimiento.estado ? 'Activo' : 'Inactivo'}</td>
                        <td>{movimiento.movimiento}</td>
                        <td>{movimiento.saldoDisponible}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              ) : (
                <p>No hay movimientos para esta cuenta.</p>
              )}
            </div>
          )}
        </div>
      ) : (
        <p>Redirigiendo al login...</p>
      )}
    </div>
  );
};

export default Home;
