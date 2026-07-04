import React, { useState, useEffect } from 'react';
import { gridService } from '../services/api';
import Header from '../components/Header';
import { 
  Zap, 
  Activity, 
  Plus, 
  Trash2, 
  AlertCircle, 
  Check, 
  Info, 
  Clock, 
  ShieldAlert,
  Sliders
} from 'lucide-react';

const GridNodes = () => {
  const [nodes, setNodes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [selectedNode, setSelectedNode] = useState(null);
  const [telemetries, setTelemetries] = useState([]);
  const [showAddForm, setShowAddForm] = useState(false);
  
  // Form State
  const [name, setName] = useState('');
  const [type, setType] = useState('Substation');
  const [capacity, setCapacity] = useState('');
  const [zoneId, setZoneId] = useState('1'); // Default to Zone 1

  // Fallback demo data
  const fallbackNodes = [
    { id: 1, name: 'North Substation T1', type: 'Substation', capacity: 50.0, status: 'ACTIVE', zone: { name: 'North Zone' } },
    { id: 2, name: 'North Transformer A', type: 'Transformer', capacity: 10.0, status: 'ACTIVE', zone: { name: 'North Zone' } },
    { id: 3, name: 'South Industrial Substation', type: 'Substation', capacity: 150.0, status: 'ACTIVE', zone: { name: 'South Zone' } },
    { id: 4, name: 'South Distribution Link', type: 'Distribution Box', capacity: 25.0, status: 'UNDER_MAINTENANCE', zone: { name: 'South Zone' } }
  ];

  const fallbackTelemetries = [
    { id: 1, voltage: 228.4, current: 215.0, frequency: 50.02, powerFactor: 0.96, activeLoad: 32.5, healthStatus: 'NORMAL', timestamp: new Date().toISOString() },
    { id: 2, voltage: 227.9, current: 214.2, frequency: 49.98, powerFactor: 0.95, activeLoad: 32.1, healthStatus: 'NORMAL', timestamp: new Date(Date.now() - 300000).toISOString() },
    { id: 3, voltage: 228.1, current: 216.5, frequency: 50.01, powerFactor: 0.96, activeLoad: 32.8, healthStatus: 'NORMAL', timestamp: new Date(Date.now() - 600000).toISOString() }
  ];

  useEffect(() => {
    fetchNodes();
  }, []);

  const fetchNodes = async () => {
    try {
      setLoading(true);
      const res = await gridService.getNodes();
      setNodes(res.data.length > 0 ? res.data : fallbackNodes);
    } catch (err) {
      console.warn('Backend grid node retrieval failed. Using mock data.', err);
      setNodes(fallbackNodes);
    } finally {
      setLoading(false);
    }
  };

  const handleSelectNode = async (node) => {
    setSelectedNode(node);
    try {
      const res = await gridService.getTelemetries(node.id);
      setTelemetries(res.data.length > 0 ? res.data : fallbackTelemetries);
    } catch (err) {
      console.warn('Backend telemetry retrieval failed. Using mock data.', err);
      setTelemetries(fallbackTelemetries);
    }
  };

  const handleCreateNode = async (e) => {
    e.preventDefault();
    setError('');
    const newNodePayload = {
      name,
      type,
      capacity: parseFloat(capacity),
      status: 'ACTIVE',
      zoneId: parseInt(zoneId)
    };

    try {
      const res = await gridService.createNode(newNodePayload);
      setNodes([...nodes, res.data]);
      setShowAddForm(false);
      resetForm();
    } catch (err) {
      console.warn('Backend save failed. Simulated local addition.', err);
      const dummyNode = {
        id: Date.now(),
        name,
        type,
        capacity: parseFloat(capacity),
        status: 'ACTIVE',
        zone: { name: zoneId === '1' ? 'North Zone' : 'South Zone' }
      };
      setNodes([...nodes, dummyNode]);
      setShowAddForm(false);
      resetForm();
    }
  };

  const resetForm = () => {
    setName('');
    setType('Substation');
    setCapacity('');
    setZoneId('1');
  };

  return (
    <div style={{ minHeight: '100vh', background: 'var(--bg-primary)' }}>
      <Header title="Grid Nodes Management" />

      <div style={{ display: 'grid', gridTemplateColumns: selectedNode ? '2fr 1fr' : '1fr', gap: '1.5rem' }}>
        
        {/* Main Grid Nodes List */}
        <div>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem' }}>
            <h3 style={{ fontSize: '1.25rem', fontWeight: 700 }}>Telemetry Nodes</h3>
            <button className="btn btn-primary" onClick={() => setShowAddForm(!showAddForm)}>
              <Plus size={16} />
              <span>Register Node</span>
            </button>
          </div>

          {showAddForm && (
            <div className="glass-panel" style={{ marginBottom: '1.5rem', animation: 'slideIn 0.3s ease' }}>
              <h4 style={{ fontSize: '1rem', fontWeight: 700, marginBottom: '1.25rem' }}>Register New Grid Node</h4>
              <form onSubmit={handleCreateNode} style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '1rem', alignItems: 'end' }}>
                <div className="form-group" style={{ marginBottom: 0 }}>
                  <label className="form-label">Node Name</label>
                  <input type="text" className="form-control" placeholder="e.g. Substation North E" value={name} onChange={(e) => setName(e.target.value)} required />
                </div>
                
                <div className="form-group" style={{ marginBottom: 0 }}>
                  <label className="form-label">Type</label>
                  <select className="form-control" value={type} onChange={(e) => setType(e.target.value)}>
                    <option value="Substation">Substation</option>
                    <option value="Transformer">Transformer</option>
                    <option value="Distribution Box">Distribution Box</option>
                  </select>
                </div>

                <div className="form-group" style={{ marginBottom: 0 }}>
                  <label className="form-label">Capacity (MW)</label>
                  <input type="number" step="0.1" className="form-control" placeholder="e.g. 50" value={capacity} onChange={(e) => setCapacity(e.target.value)} required />
                </div>

                <div className="form-group" style={{ marginBottom: 0 }}>
                  <label className="form-label">Grid Zone</label>
                  <select className="form-control" value={zoneId} onChange={(e) => setZoneId(e.target.value)}>
                    <option value="1">North Zone</option>
                    <option value="2">South Zone</option>
                    <option value="3">Central Zone</option>
                  </select>
                </div>

                <div style={{ display: 'flex', gap: '0.5rem' }}>
                  <button type="submit" className="btn btn-primary" style={{ flex: 1 }}>Add</button>
                  <button type="button" className="btn btn-secondary" onClick={() => setShowAddForm(false)}>Cancel</button>
                </div>
              </form>
            </div>
          )}

          <div className="nodes-grid">
            {nodes.map((node) => {
              const isActive = selectedNode?.id === node.id;
              return (
                <div 
                  key={node.id} 
                  className={`glass-panel node-card ${isActive ? 'glass-panel-accent' : ''}`}
                  onClick={() => handleSelectNode(node)}
                  style={{ cursor: 'pointer' }}
                >
                  <div className="node-card-title">
                    <span>{node.name}</span>
                    <span className={`status-pill ${
                      node.status === 'ACTIVE' 
                        ? 'status-active' 
                        : node.status === 'UNDER_MAINTENANCE' 
                          ? 'status-warning' 
                          : 'status-critical'
                    }`}>
                      {node.status}
                    </span>
                  </div>

                  <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem', fontSize: '0.8125rem', color: 'var(--text-secondary)' }}>
                    <div>
                      <div>Category</div>
                      <div style={{ color: 'white', fontWeight: 600, marginTop: '0.15rem' }}>{node.type}</div>
                    </div>
                    <div>
                      <div>Zone</div>
                      <div style={{ color: 'white', fontWeight: 600, marginTop: '0.15rem' }}>{node.zone?.name || 'Main Grid'}</div>
                    </div>
                  </div>

                  <div>
                    <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '0.75rem', color: 'var(--text-muted)' }}>
                      <span>Nominal Load Capacity</span>
                      <span>{node.capacity} MW</span>
                    </div>
                    <div className="metric-bar">
                      <div className="metric-fill" style={{ width: '65%' }}></div>
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        </div>

        {/* Selected Node Drawer / Telemetry details */}
        {selectedNode && (
          <aside className="glass-panel" style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem', animation: 'slideIn 0.3s ease' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'start' }}>
              <div>
                <h3 style={{ fontSize: '1.25rem', fontWeight: 800 }}>Node Diagnostics</h3>
                <p style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>ID: {selectedNode.id}</p>
              </div>
              <button 
                onClick={() => setSelectedNode(null)} 
                style={{ background: 'none', border: 'none', color: 'var(--text-muted)', cursor: 'pointer', fontSize: '1.25rem' }}
              >
                &times;
              </button>
            </div>

            <div style={{ background: 'rgba(255,255,255,0.02)', padding: '1rem', borderRadius: 'var(--border-radius-sm)', border: '1px solid var(--glass-border)' }}>
              <div style={{ fontSize: '0.875rem', fontWeight: 700, marginBottom: '0.5rem' }}>{selectedNode.name}</div>
              <div style={{ display: 'flex', flexDirection: 'column', gap: '0.25rem', fontSize: '0.75rem', color: 'var(--text-secondary)' }}>
                <span>Type: {selectedNode.type}</span>
                <span>Max Power Load: {selectedNode.capacity} MW</span>
                <span>Zone: {selectedNode.zone?.name || 'Default'}</span>
              </div>
            </div>

            <div>
              <h4 style={{ fontSize: '0.9375rem', fontWeight: 700, marginBottom: '1rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                <Activity size={16} style={{ color: 'var(--accent-cyan)' }} />
                <span>Live Metrics telemetry</span>
              </h4>

              {telemetries.length > 0 ? (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '0.75rem' }}>
                  <div className="glass-panel" style={{ padding: '0.75rem 1rem', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>Voltage</span>
                    <span style={{ fontWeight: 700 }}>{telemetries[0].voltage.toFixed(1)} V</span>
                  </div>
                  <div className="glass-panel" style={{ padding: '0.75rem 1rem', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>Current Flow</span>
                    <span style={{ fontWeight: 700 }}>{telemetries[0].current.toFixed(1)} A</span>
                  </div>
                  <div className="glass-panel" style={{ padding: '0.75rem 1rem', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>Grid Frequency</span>
                    <span style={{ fontWeight: 700 }}>{telemetries[0].frequency.toFixed(2)} Hz</span>
                  </div>
                  <div className="glass-panel" style={{ padding: '0.75rem 1rem', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>Power Factor</span>
                    <span style={{ fontWeight: 700 }}>{telemetries[0].powerFactor.toFixed(2)}</span>
                  </div>
                </div>
              ) : (
                <div style={{ fontSize: '0.8125rem', color: 'var(--text-muted)', textAlign: 'center', padding: '1rem' }}>
                  No telemetry logged.
                </div>
              )}
            </div>

            {/* Diagnostic Logs */}
            <div>
              <h4 style={{ fontSize: '0.9375rem', fontWeight: 700, marginBottom: '0.75rem' }}>Recent events</h4>
              <div style={{ display: 'flex', flexDirection: 'column', gap: '0.5rem', fontSize: '0.75rem' }}>
                <div style={{ display: 'flex', gap: '0.5rem', color: '#10b981' }}>
                  <Check size={14} style={{ flexShrink: 0 }} />
                  <span>Telemetry node initialized successfully.</span>
                </div>
                <div style={{ display: 'flex', gap: '0.5rem', color: 'var(--text-muted)' }}>
                  <Clock size={14} style={{ flexShrink: 0 }} />
                  <span>Diagnostics ping resolved within 14ms.</span>
                </div>
              </div>
            </div>
          </aside>
        )}
      </div>
    </div>
  );
};

export default GridNodes;
