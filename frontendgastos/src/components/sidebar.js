// src/components/Sidebar.js
import React from 'react';
import { Link } from 'react-router-dom';

const Sidebar = () => {
  return (
    <nav className="sidebar">
      <ul>
        <li>
          <Link to="/home">Home</Link>
        </li>
        <li>
          <Link to="/cuentas">Cuentas</Link>
        </li>
        <li>
          <Link to="/gestion">Gestiones</Link>
        </li>
        <li>
          <Link to="/transferencias">Transferencias</Link>
        </li>
        <li>
          <Link to="/compras">Compras</Link>
        </li>
      </ul>
    </nav>
  );
};

export default Sidebar;
