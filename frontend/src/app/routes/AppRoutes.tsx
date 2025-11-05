import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import AuthLogin from '../../features/auth/pages/Login';

const AppRoutes: React.FC = () => {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/projects" replace />} />
      <Route path="/login" element={<AuthLogin />} />

      {/* feature routes (lazy-load later) */}
      <Route path="/projects" element={<div>Projects module (placeholder)</div>} />
      <Route path="/companies" element={<div>Companies module (placeholder)</div>} />
      <Route path="/reports" element={<div>Reports module (placeholder)</div>} />

      {/* fallback */}
      <Route path="*" element={<div>404 - Not found</div>} />
    </Routes>
  );
};

export default AppRoutes;
