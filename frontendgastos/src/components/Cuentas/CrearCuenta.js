import React, { useState } from 'react';
import { useAuth } from '../auth/AuthContext';
import CuentaService from './CuentaService';
import { useNavigate } from 'react-router-dom';

const CrearCuenta = () => {
  const [numeroCuenta, setNumeroCuenta] = useState('');
  const [tipoCuenta, setTipoCuenta] = useState('');
  const [saldoInicial, setSaldoInicial] = useState('');
  const { authData } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    const nuevaCuenta = {
      numeroCuenta,
      tipoCuenta,
      saldoInicial,
    };
    try {
      await CuentaService.crearCuenta(authData.nombre, nuevaCuenta, authData.token);
      alert('Cuenta creada con éxito');
      navigate('/home');
    } catch (error) {
      console.error('Error al crear la cuenta', error);
      alert('Hubo un error al crear la cuenta');
    }
  };

  return (
    <div>
      <h2>Crear Nueva Cuenta</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Número de Cuenta:</label>
          <input
            type="text"
            value={numeroCuenta}
            onChange={(e) => setNumeroCuenta(e.target.value)}
            required
          />
        </div>
        <div>
          <label>Tipo de Cuenta:</label>
          <input
            type="text"
            value={tipoCuenta}
            onChange={(e) => setTipoCuenta(e.target.value)}
            required
          />
        </div>
        <div>
          <label>Saldo Inicial:</label>
          <input
            type="number"
            value={saldoInicial}
            onChange={(e) => setSaldoInicial(e.target.value)}
            required
          />
        </div>
        <button type="submit">Crear Cuenta</button>
      </form>
    </div>
  );
};

export default CrearCuenta;
