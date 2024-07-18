import React, { useState, useEffect } from 'react';
import { useAuth } from '../auth/AuthContext';
import { obtenerCuentas } from '../../api';
import { crearMovimiento } from './MovimientoService';
import { Link } from 'react-router-dom';

const Gestion = () => {
  const { authData } = useAuth();
  const [cuentas, setCuentas] = useState([]);
  const [selectedCuenta, setSelectedCuenta] = useState('');
  const [nuevoMovimiento, setNuevoMovimiento] = useState({
    tipoMovimiento: '',
    valor: '',
    cuentaDestinoId: '',
  });
  const [mensaje, setMensaje] = useState('');

  useEffect(() => {
    if (authData.token && authData.nombre) {
      obtenerCuentas(authData.nombre, authData.token)
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

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setNuevoMovimiento({
      ...nuevoMovimiento,
      [name]: value,
    });
  };

  const handleSubmit = (event) => {
    event.preventDefault();

    if (!nuevoMovimiento.tipoMovimiento) {
      setMensaje('Por favor, selecciona un tipo de movimiento.');
      return;
    }

    if (!nuevoMovimiento.valor) {
      setMensaje('Por favor, ingresa el valor del movimiento.');
      return;
    }

    // Determinar cuenta destino e origen según el tipo de movimiento
    let cuentaDestinoId = null;
    let cuentaOrigenId = selectedCuenta;

    if (nuevoMovimiento.tipoMovimiento === 'Transferencia') {
      if (!nuevoMovimiento.cuentaDestinoId) {
        setMensaje('Por favor, selecciona una cuenta destino para la transferencia.');
        return;
      }
      cuentaDestinoId = nuevoMovimiento.cuentaDestinoId;
    } else if (nuevoMovimiento.tipoMovimiento === 'Deposito') {
      cuentaDestinoId = selectedCuenta;
    } else if (nuevoMovimiento.tipoMovimiento === 'Retiro') {
      cuentaDestinoId = null; // No se necesita cuenta destino para retiros
    }

    const movimiento = {
      cuentaOrigenId: cuentaOrigenId,
      cuentaDestinoId: cuentaDestinoId,
      tipoMovimiento: nuevoMovimiento.tipoMovimiento,
      valor: nuevoMovimiento.valor,
    };

    crearMovimiento(movimiento, authData.token)
      .then(response => {
        setMensaje('Movimiento creado con éxito.');
        // Limpiar formulario después de la creación
        setNuevoMovimiento({
          tipoMovimiento: '',
          valor: '',
          cuentaDestinoId: '',
        });
        setSelectedCuenta(''); // Limpiar selección de cuenta
      })
      .catch(error => {
        console.error('Error:', error);
        setMensaje('Error al crear el movimiento.');
      });
  };

  return (
    <div>
      {authData.token ? (
        <div>
          <h2>Gestión de Movimientos</h2>

          <div>
            <label htmlFor="cuenta">Selecciona una Cuenta:</label>
            <select
              id="cuenta"
              value={selectedCuenta}
              onChange={handleCuentaChange}
            >
              <option value="">Selecciona una cuenta</option>
              {cuentas.map(cuenta => (
                <option key={cuenta.id} value={cuenta.id}>{cuenta.numeroCuenta}</option>
              ))}
            </select>
          </div>

          {selectedCuenta && (
            <div>
              <h3>Crear Nuevo Movimiento</h3>
              <form onSubmit={handleSubmit}>
                <div>
                  <label htmlFor="tipoMovimiento">Tipo de Movimiento:</label>
                  <select
                    id="tipoMovimiento"
                    name="tipoMovimiento"
                    value={nuevoMovimiento.tipoMovimiento}
                    onChange={handleInputChange}
                    required
                  >
                    <option value="">Selecciona un tipo</option>
                    <option value="Deposito">Depósito</option>
                    <option value="Retiro">Retiro</option>
                    <option value="Transferencia">Transferencia</option>
                  </select>
                </div>
                <div>
                  <label htmlFor="valor">Valor:</label>
                  <input
                    type="number"
                    id="valor"
                    name="valor"
                    value={nuevoMovimiento.valor}
                    onChange={handleInputChange}
                    required
                  />
                </div>
                {nuevoMovimiento.tipoMovimiento === 'Transferencia' && (
                  <div>
                    <label htmlFor="cuentaDestino">Cuenta Destino:</label>
                    <select
                      id="cuentaDestino"
                      name="cuentaDestinoId"
                      value={nuevoMovimiento.cuentaDestinoId}
                      onChange={handleInputChange}
                      required
                    >
                      <option value="">Selecciona una cuenta destino</option>
                      {cuentas.filter(cuenta => cuenta.id !== selectedCuenta).map(cuenta => (
                        <option key={cuenta.id} value={cuenta.id}>{cuenta.numeroCuenta}</option>
                      ))}
                    </select>
                  </div>
                )}
                <button type="submit">Crear Movimiento</button>
              </form>
              {mensaje && <p>{mensaje}</p>}
            </div>
          )}
          
          <br />
          <Link to="/crear-cuenta">
            <button>Crear Nueva Cuenta</button>
          </Link>
        </div>
      ) : (
        <p>Redirigiendo al login...</p>
      )}
    </div>
  );
};

export default Gestion;
