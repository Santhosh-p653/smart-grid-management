import React, { useState, useEffect } from 'react';
import { analyticsService } from '../services/api';
import Header from '../components/Header';
import TelemetryChart from '../components/TelemetryChart';
import { 
  Users, 
  Activity, 
  AlertTriangle, 
  Zap, 
  TrendingUp, 
  Clock, 
  Bell,
  Gauge
} from 'lucide-react';

const Dashboard = () => {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  // Fallback demo data if backend connection fails or tables are empty
  const fallbackStats = {
    totalConsumers: 1248,
    totalGridNodes: 14,
    activeFaults: 2,
    activeOutages: 1,
    currentVoltage: 231.8,
    currentLoad: 84.5,
    totalEnergyConsumption: 54120.4,
    recentAlerts: [
      { id: 1, title: 'Voltage Flicker Detected', message: 'Node: North Substation T1 reported brief transient voltage spike.', severity: 'WARNING', createdAt: new Date().toISOString() },
      { id: 2, title: 'High Ambient Temp Alert', message: 'Transformer S4 reporting core temperature above normal threshold.', severity: 'CRITICAL', createdAt: new Date(Date.now() - 3600000).toISOString() },
      { id: 3, title: 'System Normalization', message: 'Central Distribution Link has successfully restored standard frequency.', severity: 'INFO', createdAt: new Date(Date.now() - 7200000).toISOString() },
    ],
    monthlyConsumption: [
      { month: 'Jan', consumption: 45000 },
      { month: 'Feb', consumption: 42000 },
      { month: 'Mar', consumption: 49000 },
      { month: 'Apr', consumption: 51000 },
      { month: 'May', consumption: 58000 },
      { month: 'Jun', consumption: 64000 },
    ],
    faultSeverityStats: [
      { severity: 'LOW', count: 4 },
      { severity: 'MEDIUM', count: 2 },
      { severity: 'HIGH', count: 1 },
    ],
    loadDistribution: [
      { zoneName: 'North Zone', currentLoad: 24.5, capacity: 50.0 },
      { zoneName: 'South Zone', currentLoad: 42.0, capacity: 150.0 },
      { zoneName: 'Central Zone', currentLoad: 18.0, capacity: 25.0 },
    ]
  };

  useEffect(() => {
    fetchStats();
  }, []);

  const fetchStats = async () => {
    try {
      setLoading(true);
      const res = await analyticsService.getDashboardStats();
      if (res.data) {
        setStats(res.data);
      } else {
        setStats(fallbackStats);
      }
    } catch (err) {
      console.warn('Backend unavailable, showing dashboard stats demo mode.', err);
      setStats(fallbackStats);
      setError('Live connection failed. Displaying cached simulation dashboard data.');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div style={{ display: 'flex', minHeight: '100vh', alignItems: 'center', justifyContent: 'center', background: 'var(--bg-primary)' }}>
        <div style={{ textAlign: 'center' }}>
          <Zap size={40} className="status-active" style={{ animation: 'pulseGlow 1.5s infinite', margin: '0 auto 1.5rem' }} />
          <h3 style={{ fontWeight: 600 }}>Loading Telemetry Dashboard...</h3>
          <p style={{ color: 'var(--text-secondary)', fontSize: '0.875rem', marginTop: '0.5rem' }}>Synching parameters with grid nodes...</p>
        </div>
      </div>
    );
  }

  const data = stats || fallbackStats;

  return (
    <div style={{ minHeight: '100vh', background: 'var(--bg-primary)' }}>
      <Header title="Telemetry Dashboard" />

      {error && (
        <div className="glass-panel" style={{ padding: '0.75rem 1.25rem', borderLeft: '4px solid var(--accent-purple)', marginBottom: '1.5rem', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <span style={{ fontSize: '0.875rem', color: 'var(--text-secondary)' }}>{error}</span>
          <button onClick={() => setError('')} style={{ background: 'none', border: 'none', color: 'var(--text-muted)', cursor: 'pointer', fontSize: '0.75rem' }}>Dismiss</button>
        </div>
      )}

      {/* Metrics Section */}
      <section className="stats-grid">
        <div className="glass-panel stat-card">
          <div className="stat-header">
            <span>Total Active Consumers</span>
            <div className="stat-icon-wrapper cyan-glow">
              <Users size={20} />
            </div>
          </div>
          <div>
            <div className="stat-value">{data.totalConsumers}</div>
            <div style={{ fontSize: '0.75rem', color: '#10b981', display: 'flex', alignItems: 'center', gap: '0.25rem' }}>
              <TrendingUp size={14} />
              <span>+3.2% from last month</span>
            </div>
          </div>
        </div>

        <div className="glass-panel stat-card">
          <div className="stat-header">
            <span>Active Grid Nodes</span>
            <div className="stat-icon-wrapper purple-glow">
              <Activity size={20} />
            </div>
          </div>
          <div>
            <div className="stat-value">{data.totalGridNodes}</div>
            <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>
              Nodes online: {data.totalGridNodes - data.activeOutages} / {data.totalGridNodes}
            </div>
          </div>
        </div>

        <div className="glass-panel stat-card">
          <div className="stat-header">
            <span>Grid Fault Outbreaks</span>
            <div className="stat-icon-wrapper pink-glow">
              <AlertTriangle size={20} />
            </div>
          </div>
          <div>
            <div className="stat-value">{data.activeFaults}</div>
            <div style={{ fontSize: '0.75rem', color: data.activeFaults > 0 ? '#ef4444' : 'var(--text-muted)' }}>
              {data.activeFaults > 0 ? `${data.activeFaults} unresolved issues` : 'All systems operating normally'}
            </div>
          </div>
        </div>

        <div className="glass-panel stat-card">
          <div className="stat-header">
            <span>Active Load / Supply</span>
            <div className="stat-icon-wrapper blue-glow">
              <Gauge size={20} />
            </div>
          </div>
          <div>
            <div className="stat-value">{data.currentLoad.toFixed(1)} MW</div>
            <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>
              Average Grid Voltage: {data.currentVoltage.toFixed(1)}V
            </div>
          </div>
        </div>
      </section>

      {/* Charts Section */}
      <section style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(400px, 1fr))', gap: '1.5rem', marginBottom: '2rem' }}>
        <div className="glass-panel">
          <TelemetryChart 
            data={data.monthlyConsumption} 
            type="line"
            xKey="month" 
            yKey="consumption" 
            title="Monthly Aggregate Consumption (kWh)" 
          />
        </div>

        <div className="glass-panel">
          <h4 style={{ fontSize: '1rem', fontWeight: 600, marginBottom: '1.5rem', color: 'var(--text-secondary)' }}>Zone Load vs. Total Capacity</h4>
          <div style={{ display: 'flex', flexDirection: 'column', gap: '1.25rem' }}>
            {data.loadDistribution.map((zone, i) => {
              const percentage = (zone.currentLoad / zone.capacity) * 100;
              return (
                <div key={i}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '0.875rem', marginBottom: '0.25rem' }}>
                    <span style={{ fontWeight: 500 }}>{zone.zoneName}</span>
                    <span style={{ color: 'var(--text-secondary)' }}>
                      {zone.currentLoad.toFixed(1)} / {zone.capacity} MW ({Math.round(percentage)}%)
                    </span>
                  </div>
                  <div className="metric-bar">
                    <div 
                      className="metric-fill" 
                      style={{ 
                        width: `${Math.min(percentage, 100)}%`,
                        background: percentage > 80 
                          ? 'linear-gradient(90deg, #ef4444, #f87171)' 
                          : percentage > 60 
                            ? 'linear-gradient(90deg, #f59e0b, #fbbf24)' 
                            : 'linear-gradient(90deg, var(--accent-blue), var(--accent-cyan))'
                      }}
                    />
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      </section>

      {/* Alerts and Logs Section */}
      <section style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(400px, 1fr))', gap: '1.5rem' }}>
        <div className="glass-panel">
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginBottom: '1.5rem' }}>
            <Bell size={18} style={{ color: 'var(--accent-pink)' }} />
            <h4 style={{ fontSize: '1rem', fontWeight: 600 }}>Critical System Alerts</h4>
          </div>
          
          <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
            {data.recentAlerts.map((alert) => (
              <div 
                key={alert.id} 
                className="glass-panel" 
                style={{ 
                  padding: '1rem', 
                  background: 'rgba(255,255,255,0.02)',
                  borderColor: alert.severity === 'CRITICAL' 
                    ? 'rgba(239, 68, 68, 0.2)' 
                    : alert.severity === 'WARNING' 
                      ? 'rgba(245, 158, 11, 0.2)' 
                      : 'rgba(79, 172, 254, 0.1)'
                }}
              >
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '0.25rem' }}>
                  <span style={{ 
                    fontWeight: 700, 
                    fontSize: '0.875rem',
                    color: alert.severity === 'CRITICAL' 
                      ? '#ef4444' 
                      : alert.severity === 'WARNING' 
                        ? '#f59e0b' 
                        : 'var(--text-primary)'
                  }}>
                    {alert.title}
                  </span>
                  <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)', display: 'flex', alignItems: 'center', gap: '0.25rem' }}>
                    <Clock size={12} />
                    {new Date(alert.createdAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                  </span>
                </div>
                <p style={{ fontSize: '0.8125rem', color: 'var(--text-secondary)', lineHeight: 1.4 }}>{alert.message}</p>
              </div>
            ))}
          </div>
        </div>

        <div className="glass-panel" style={{ display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
          <div>
            <h4 style={{ fontSize: '1rem', fontWeight: 600, marginBottom: '1.5rem' }}>Fault Statistics Overview</h4>
            <div style={{ display: 'flex', justifyContent: 'center', padding: '1.5rem 0' }}>
              {/* Circular Gauge Representation */}
              <div style={{ position: 'relative', width: '120px', height: '120px' }}>
                <svg width="100%" height="100%" viewBox="0 0 40 40">
                  <circle cx="20" cy="20" r="16" fill="none" stroke="var(--glass-border)" strokeWidth="3" />
                  <circle cx="20" cy="20" r="16" fill="none" stroke="var(--accent-purple)" strokeWidth="3.5" 
                    strokeDasharray="100" strokeDashoffset={100 - (100 * (1 - (data.activeFaults / 10)))}
                    strokeLinecap="round" style={{ transform: 'rotate(-90deg)', transformOrigin: '50% 50%', transition: 'stroke-dashoffset 1s ease' }}
                  />
                </svg>
                <div style={{ position: 'absolute', top: 0, left: 0, width: '100%', height: '100%', display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center' }}>
                  <span style={{ fontSize: '1.5rem', fontWeight: 800 }}>{data.activeFaults}</span>
                  <span style={{ fontSize: '0.625rem', color: 'var(--text-muted)', textTransform: 'uppercase' }}>Active</span>
                </div>
              </div>
            </div>
          </div>
          
          <div className="glass-panel" style={{ background: 'rgba(255,255,255,0.01)', padding: '1rem' }}>
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: '1rem', textAlign: 'center' }}>
              {data.faultSeverityStats.map((stat, i) => (
                <div key={i}>
                  <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>{stat.severity}</div>
                  <div style={{ fontSize: '1.25rem', fontWeight: 700, marginTop: '0.25rem' }}>{stat.count}</div>
                </div>
              ))}
            </div>
          </div>
        </div>
      </section>
    </div>
  );
};

export default Dashboard;
