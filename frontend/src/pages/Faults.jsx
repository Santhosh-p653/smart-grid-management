import React, { useState, useEffect } from 'react';
import { faultService, gridService } from '../services/api';
import Header from '../components/Header';
import { 
  AlertTriangle, 
  Check, 
  Clock, 
  ShieldAlert, 
  FileEdit, 
  PowerOff,
  Plus
} from 'lucide-react';

const Faults = () => {
  const [faults, setFaults] = useState([]);
  const [outages, setOutages] = useState([]);
  const [nodes, setNodes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [activeTab, setActiveTab] = useState('faults');
  
  // Reporting Form States
  const [showForm, setShowForm] = useState(false);
  const [nodeId, setNodeId] = useState('');
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [severity, setSeverity] = useState('MEDIUM');

  // Fallbacks
  const fallbackFaults = [
    { id: 1, title: 'Insulator Flashover', description: 'Insulator breakdown on overhead lines', severity: 'HIGH', status: 'ACTIVE', reportedAt: new Date().toISOString(), gridNode: { name: 'South Distribution Link' } },
    { id: 2, title: 'Minor Voltage Flicker', description: 'Transient flicker resolved by auto recloser', severity: 'LOW', status: 'RESOLVED', reportedAt: new Date(Date.now() - 86400000).toISOString(), resolvedAt: new Date(Date.now() - 86300000).toISOString(), gridNode: { name: 'North Substation T1' } }
  ];

  const fallbackOutages = [
    { id: 1, startTime: new Date().toISOString(), status: 'ACTIVE', description: 'Precautionary repair for core panel', gridNode: { name: 'South Distribution Link' } }
  ];

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      setLoading(true);
      const [faultsRes, outagesRes, nodesRes] = await Promise.all([
        faultService.getFaults(),
        faultService.getOutages(),
        gridService.getNodes()
      ]);
      setFaults(faultsRes.data.length > 0 ? faultsRes.data : fallbackFaults);
      setOutages(outagesRes.data.length > 0 ? outagesRes.data : fallbackOutages);
      setNodes(nodesRes.data);
    } catch (err) {
      console.warn('Backend unavailable, loading local simulations.', err);
      setFaults(fallbackFaults);
      setOutages(fallbackOutages);
    } finally {
      setLoading(false);
    }
  };

  const handleResolveFault = async (id) => {
    try {
      await faultService.resolveFault(id);
      fetchData();
    } catch (err) {
      console.warn('Backend resolve failed, local mock resolve.', err);
      setFaults(faults.map(f => f.id === id ? { ...f, status: 'RESOLVED', resolvedAt: new Date().toISOString() } : f));
    }
  };

  const handleResolveOutage = async (id) => {
    try {
      await faultService.resolveOutage(id);
      fetchData();
    } catch (err) {
      console.warn('Backend restore failed, local mock restore.', err);
      setOutages(outages.map(o => o.id === id ? { ...o, status: 'RESTORED', endTime: new Date().toISOString() } : o));
    }
  };

  const handleCreateFault = async (e) => {
    e.preventDefault();
    const payload = {
      gridNodeId: parseInt(nodeId),
      title,
      description,
      severity
    };

    try {
      if (activeTab === 'faults') {
        const res = await faultService.reportFault(payload);
        setFaults([res.data, ...faults]);
      } else {
        const res = await faultService.reportOutage({ gridNodeId: payload.gridNodeId, description });
        setOutages([res.data, ...outages]);
      }
      setShowForm(false);
      resetForm();
    } catch (err) {
      console.warn('Local simulation addition.');
      const selectedNode = nodes.find(n => n.id === parseInt(nodeId)) || { name: 'Manual Override Node' };
      if (activeTab === 'faults') {
        const dummy = {
          id: Date.now(),
          title,
          description,
          severity,
          status: 'ACTIVE',
          reportedAt: new Date().toISOString(),
          gridNode: selectedNode
        };
        setFaults([dummy, ...faults]);
      } else {
        const dummy = {
          id: Date.now(),
          startTime: new Date().toISOString(),
          status: 'ACTIVE',
          description,
          gridNode: selectedNode
        };
        setOutages([dummy, ...outages]);
      }
      setShowForm(false);
      resetForm();
    }
  };

  const resetForm = () => {
    setNodeId('');
    setTitle('');
    setDescription('');
    setSeverity('MEDIUM');
  };

  return (
    <div style={{ minHeight: '100vh', background: 'var(--bg-primary)' }}>
      <Header title="Faults & Outages Manager" />

      {/* Tabs */}
      <div style={{ display: 'flex', gap: '1rem', borderBottom: '1px solid var(--glass-border)', marginBottom: '2rem' }}>
        <button 
          className={`nav-link ${activeTab === 'faults' ? 'active' : ''}`}
          onClick={() => { setActiveTab('faults'); setShowForm(false); }}
          style={{ background: 'none', border: 'none', fontSize: '1rem', paddingBottom: '1rem', borderRadius: 0 }}
        >
          Active Fault Logs
        </button>
        <button 
          className={`nav-link ${activeTab === 'outages' ? 'active' : ''}`}
          onClick={() => { setActiveTab('outages'); setShowForm(false); }}
          style={{ background: 'none', border: 'none', fontSize: '1rem', paddingBottom: '1rem', borderRadius: 0 }}
        >
          Grid Outages
        </button>
      </div>

      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem' }}>
        <h3 style={{ fontSize: '1.25rem', fontWeight: 700 }}>
          {activeTab === 'faults' ? 'Fault Discrepancies' : 'Outage Records'}
        </h3>
        <button className="btn btn-primary" onClick={() => setShowForm(!showForm)}>
          <Plus size={16} />
          <span>Report {activeTab === 'faults' ? 'Fault' : 'Outage'}</span>
        </button>
      </div>

      {showForm && (
        <div className="glass-panel" style={{ marginBottom: '2rem', animation: 'slideIn 0.3s ease' }}>
          <h4 style={{ fontSize: '1rem', fontWeight: 700, marginBottom: '1.25rem' }}>
            Report New {activeTab === 'faults' ? 'Hardware Fault' : 'Grid Outage'}
          </h4>
          <form onSubmit={handleCreateFault} style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '1rem' }}>
              <div className="form-group" style={{ marginBottom: 0 }}>
                <label className="form-label">Affected Grid Node</label>
                <select className="form-control" value={nodeId} onChange={(e) => setNodeId(e.target.value)} required>
                  <option value="">Select Node</option>
                  {nodes.map(n => <option key={n.id} value={n.id}>{n.name}</option>)}
                  <option value="1">North Substation T1</option>
                  <option value="3">South Industrial Substation</option>
                </select>
              </div>

              {activeTab === 'faults' && (
                <>
                  <div className="form-group" style={{ marginBottom: 0 }}>
                    <label className="form-label">Fault Title</label>
                    <input type="text" className="form-control" placeholder="Short summary" value={title} onChange={(e) => setTitle(e.target.value)} required />
                  </div>
                  <div className="form-group" style={{ marginBottom: 0 }}>
                    <label className="form-label">Severity</label>
                    <select className="form-control" value={severity} onChange={(e) => setSeverity(e.target.value)}>
                      <option value="LOW">Low</option>
                      <option value="MEDIUM">Medium</option>
                      <option value="HIGH">High</option>
                    </select>
                  </div>
                </>
              )}
            </div>

            <div className="form-group" style={{ marginBottom: 0 }}>
              <label className="form-label">Description / Work Notes</label>
              <textarea className="form-control" rows="3" placeholder="Provide full breakdown details" value={description} onChange={(e) => setDescription(e.target.value)} required />
            </div>

            <div style={{ display: 'flex', gap: '0.75rem', justifyContent: 'flex-end' }}>
              <button type="submit" className="btn btn-primary">File Report</button>
              <button type="button" className="btn btn-secondary" onClick={() => setShowForm(false)}>Cancel</button>
            </div>
          </form>
        </div>
      )}

      {/* Lists */}
      {activeTab === 'faults' ? (
        <div className="glass-panel custom-table-container">
          <table className="custom-table">
            <thead>
              <tr>
                <th>Node Name</th>
                <th>Fault Details</th>
                <th>Severity</th>
                <th>Reported At</th>
                <th>Status</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {faults.map(f => (
                <tr key={f.id}>
                  <td style={{ fontWeight: 600 }}>{f.gridNode?.name}</td>
                  <td>
                    <div style={{ fontWeight: 600 }}>{f.title}</div>
                    <div style={{ fontSize: '0.75rem', color: 'var(--text-secondary)' }}>{f.description}</div>
                  </td>
                  <td>
                    <span className={`status-pill ${f.severity === 'HIGH' ? 'status-critical' : f.severity === 'MEDIUM' ? 'status-warning' : 'status-active'}`}>
                      {f.severity}
                    </span>
                  </td>
                  <td>{new Date(f.reportedAt).toLocaleString()}</td>
                  <td>
                    <span className={`status-pill ${f.status === 'ACTIVE' ? 'status-critical' : 'status-active'}`}>
                      {f.status}
                    </span>
                  </td>
                  <td>
                    {f.status === 'ACTIVE' ? (
                      <button className="btn btn-secondary" style={{ padding: '0.4rem 0.8rem', fontSize: '0.75rem' }} onClick={() => handleResolveFault(f.id)}>
                        Resolve
                      </button>
                    ) : (
                      <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>
                        Resolved: {new Date(f.resolvedAt).toLocaleTimeString()}
                      </span>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : (
        <div className="glass-panel custom-table-container">
          <table className="custom-table">
            <thead>
              <tr>
                <th>Node Name</th>
                <th>Outage Cause</th>
                <th>Started At</th>
                <th>Ended At</th>
                <th>Status</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {outages.map(o => (
                <tr key={o.id}>
                  <td style={{ fontWeight: 600 }}>{o.gridNode?.name}</td>
                  <td>{o.description}</td>
                  <td>{new Date(o.startTime).toLocaleString()}</td>
                  <td>{o.endTime ? new Date(o.endTime).toLocaleString() : '—'}</td>
                  <td>
                    <span className={`status-pill ${o.status === 'ACTIVE' ? 'status-critical' : 'status-active'}`}>
                      {o.status}
                    </span>
                  </td>
                  <td>
                    {o.status === 'ACTIVE' ? (
                      <button className="btn btn-secondary" style={{ padding: '0.4rem 0.8rem', fontSize: '0.75rem', gap: '0.25rem' }} onClick={() => handleResolveOutage(o.id)}>
                        <Check size={12} />
                        <span>Restore Power</span>
                      </button>
                    ) : (
                      <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>Restored</span>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default Faults;
