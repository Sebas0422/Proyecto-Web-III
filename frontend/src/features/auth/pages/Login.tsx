import React from 'react';

const AuthLogin: React.FC = () => {
  return (
    <div style={{ padding: 20 }}>
      <h2>Login (placeholder)</h2>
      <form>
        <div>
          <label>Email</label>
          <input type="email" />
        </div>
        <div>
          <label>Password</label>
          <input type="password" />
        </div>
        <button type="submit">Entrar</button>
      </form>
    </div>
  );
};

export default AuthLogin;
