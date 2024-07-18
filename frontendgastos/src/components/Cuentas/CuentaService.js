import axios from 'axios';

const API_URL = 'http://localhost:8081/api/cuentas'; // Reemplaza con la URL de tu backend

const getHeaders = (token) => ({
  headers: {
    Authorization: `Bearer ${token}`,
    'Content-Type': 'application/json',
  }
});

const crearCuenta = async (email, cuentaDTO, token) => {
  try {
    const response = await axios.post(`${API_URL}?email=${email}`, cuentaDTO, getHeaders(token));
    return response.data;
  } catch (error) {
    throw error.response.data || 'Error al crear la cuenta';
  }
};

const obtenerCuentas = async (email, token) => {
  try {
    const response = await axios.get(`${API_URL}?email=${email}`, getHeaders(token));
    return response.data;
  } catch (error) {
    throw error.response.data || 'Error al obtener las cuentas';
  }
};

const actualizarCuenta = async (id, cuentaDTO, token) => {
  try {
    const response = await axios.put(`${API_URL}/${id}`, cuentaDTO, getHeaders(token));
    return response.data;
  } catch (error) {
    throw error.response.data || 'Error al actualizar la cuenta';
  }
};

const eliminarCuenta = async (id, token) => {
  try {
    await axios.delete(`${API_URL}/${id}`, getHeaders(token));
  } catch (error) {
    throw error.response.data || 'Error al eliminar la cuenta';
  }
};

export default {
  crearCuenta,
  obtenerCuentas,
  actualizarCuenta,
  eliminarCuenta,
};
