import React from 'react';

interface Props extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'secondary';
}

const Button: React.FC<Props> = ({ variant = 'primary', children, ...rest }) => {
  const base = {
    padding: '8px 12px',
    borderRadius: 6,
    border: 'none',
    cursor: 'pointer',
  } as React.CSSProperties;

  const styles = variant === 'primary'
    ? { ...base, background: '#667eea', color: 'white' }
    : { ...base, background: '#f1f1f1', color: '#333' };

  return (
    <button style={styles} {...rest}>
      {children}
    </button>
  );
};

export default Button;
