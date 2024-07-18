import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { register } from '../../api';

const Register = () => {
    const [nombre, setNombre] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');
    const [genero, setGenero] = useState('');
    const [edad, setEdad] = useState('');
    const [identificacion, setIdentificacion] = useState('');
    const [direccion, setDireccion] = useState('');
    const [telefono, setTelefono] = useState('');
    const [estado, setEstado] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const usuarioRegistro = {
                nombre,
                password,
                email,
                genero,
                edad,
                identificacion,
                direccion,
                telefono,
                estado
            };

            await register(usuarioRegistro);
            navigate('/login'); // Redirigir al usuario al login después del registro exitoso
        } catch (error) {
            console.error('Error registrando usuario:', error);
            setError(`Error en el registro: ${error.message}`);
        }
    };

    return (
        <div>
            <h2>Registro de Usuario</h2>
            {error && <p>{error}</p>}
            <form onSubmit={handleSubmit}>
                <input 
                    type="text" 
                    value={nombre} 
                    onChange={(e) => setNombre(e.target.value)} 
                    placeholder="Nombre" 
                    required 
                />
                <input 
                    type="password" 
                    value={password} 
                    onChange={(e) => setPassword(e.target.value)} 
                    placeholder="Contraseña" 
                    required 
                />
                <input 
                    type="email" 
                    value={email} 
                    onChange={(e) => setEmail(e.target.value)} 
                    placeholder="Email" 
                    required 
                />
                <input 
                    type="text" 
                    value={genero} 
                    onChange={(e) => setGenero(e.target.value)} 
                    placeholder="Género" 
                    required 
                />
                <input 
                    type="number" 
                    value={edad} 
                    onChange={(e) => setEdad(e.target.value)} 
                    placeholder="Edad" 
                    required 
                />
                <input 
                    type="text" 
                    value={identificacion} 
                    onChange={(e) => setIdentificacion(e.target.value)} 
                    placeholder="Identificación" 
                    required 
                />
                <input 
                    type="text" 
                    value={direccion} 
                    onChange={(e) => setDireccion(e.target.value)} 
                    placeholder="Dirección" 
                    required 
                />
                <input 
                    type="text" 
                    value={telefono} 
                    onChange={(e) => setTelefono(e.target.value)} 
                    placeholder="Teléfono" 
                    required 
                />
                <label>
                    <input 
                        type="checkbox" 
                        checked={estado} 
                        onChange={(e) => setEstado(e.target.checked)} 
                    />
                    Estado
                </label>
                <button type="submit">Registrarse</button>
            </form>
        </div>
    );
};

export default Register;
