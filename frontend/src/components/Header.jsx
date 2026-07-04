import React from 'react';
import { useAuth } from '../context/AuthContext';
import { Shield, Radio, Power } from 'lucide-react';

const Header = ({ title }) => {
  const { user } = useAuth();
  
  // Real-time Date representation
  const currentDate = new Date().toLocaleDateString('en-US', {
    weekday: 'long',
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  });

  return (
    <header className="top-header">
      <div>
        <h2 style={{ fontSize: '1.75rem', fontWeight: 800, letterSpacing: '-0.5px' }}>{title}</h2>
        <p style={{ fontSize: '0.875rem', color: 'var(--text-secondary)' }}>{currentDate}</p>
      </div>

      <div style={{ display: 'flex', gap: '1rem', alignItems: 'center' }}>
        <div className="glass-panel" style={{ padding: '0.5rem 1rem', display: 'flex', alignItems: 'center', gap: '0.5rem', borderRadius: '50px' }}>
          <Radio size={16} className="status-active" style={{ animation: 'pulseGlow 2s infinite' }} />
          <span style={{ fontSize: '0.75rem', fontWeight: 600, color: 'var(--text-secondary)' }}>
            Grid Telemetry: <span style={{ color: '#10b981' }}>ONLINE</span>
          </span>
        </div>

        <div className="glass-panel" style={{ padding: '0.5rem 1rem', display: 'flex', alignItems: 'center', gap: '0.5rem', borderRadius: '50px' }}>
          <Shield size={16} style={{ color: 'var(--accent-cyan)' }} />
          <span style={{ fontSize: '0.75rem', fontWeight: 600, color: 'var(--text-secondary)' }}>
            Secure Session
          </span>
        </div>
      </div>
    </header>
  );
};

export default Header;
