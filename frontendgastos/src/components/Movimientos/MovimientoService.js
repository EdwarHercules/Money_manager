import axios from 'axios';

const API_URL = 'http://localhost:8081/api/movimientos';

export const getAllMovimientos = async () => {
    try {
        const response = await axios.get(API_URL);
        return response.data;
    } catch (error) {
        console.error('Error fetching movimientos:', error);
        throw error;
    }
};

export const crearMovimiento = async (movimientoDTO, token) => {
    const response = await fetch(`${API_URL}/crear`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
      body: JSON.stringify(movimientoDTO),
    });
  
    if (!response.ok) {
      throw new Error('Error al crear el movimiento');
    }
  
    return response.json();
  };

export const getMovimientoById = async (id) => {
    try {
        const response = await axios.get(`${API_URL}/${id}`);
        return response.data;
    } catch (error) {
        console.error('Error fetching movimiento by ID:', error);
        throw error;
    }
};

export const updateMovimiento = async (id, movimientoData) => {
    try {
        const response = await axios.put(`${API_URL}/${id}`, movimientoData);
        return response.data;
    } catch (error) {
        console.error('Error updating movimiento:', error);
        throw error;
    }
};

export const deleteMovimiento = async (id) => {
    try {
        await axios.delete(`${API_URL}/${id}`);
    } catch (error) {
        console.error('Error deleting movimiento:', error);
        throw error;
    }
};

export const getUltimosMovimientos = async (cuentaId, token) => {
    try {
      const response = await axios.get(`${API_URL}/ultimos/${cuentaId}`, {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });
      return response.data;
    } catch (error) {
      console.error('Error al obtener los movimientos:', error);
      throw error;
    }
  };
  



export const obtenerDetallesCuenta = async (cuentaId, token) => {
    const response = await fetch(`/api/cuentas/${cuentaId}`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
      },
    });
  
    if (!response.ok) {
      throw new Error('Error al obtener los detalles de la cuenta');
    }
  
    const data = await response.json();
    return data;
  };