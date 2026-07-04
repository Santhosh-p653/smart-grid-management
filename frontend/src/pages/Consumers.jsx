import React, { useState, useEffect } from 'react';
import { consumerService } from '../services/api';
import Header from '../components/Header';
import { 
  Users, 
  UserPlus, 
  Trash2, 
  Search, 
  Mail, 
  Phone, 
  MapPin, 
  TrendingUp,
  FileSpreadsheet
} from 'lucide-react';

const Consumers = () => {
  const [consumers, setConsumers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showAddForm, setShowAddForm] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');

  // Form states
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [phone, setPhone] = useState('');
  const [address, setAddress] = useState('');
  const [capacity, setCapacity] = useState('');

  const fallbackConsumers = [
    { id: 1, name: 'Global Steel Works', email: 'energy@globalsteel.com', phone: '+1-555-0199', address: 'Plot 12, Industrial Sector, South District', contractCapacity: 2500.0, accountNumber: 'ACC-54129' },
    { id: 2, name: 'Downtown Mall Complex', email: 'facilities@downtownmall.com', phone: '+1-555-0145', address: '450 Broadway St, Central District', contractCapacity: 800.0, accountNumber: 'ACC-32148' },
    { id: 3, name: 'Robert Johnson (Residential)', email: 'robert.j@gmail.com', phone: '+1-555-0122', address: '104 Maple Ave, North District', contractCapacity: 15.0, accountNumber: 'ACC-89104' }
  ];

  useEffect(() => {
    fetchConsumers();
  }, []);

  const fetchConsumers = async () => {
    try {
      setLoading(true);
      const res = await consumerService.getConsumers();
      setConsumers(res.data.length > 0 ? res.data : fallbackConsumers);
    } catch (err) {
      console.warn('Backend unavailable, showing mock consumers list.', err);
      setConsumers(fallbackConsumers);
    } finally {
      setLoading(false);
    }
  };

  const handleRegisterConsumer = async (e) => {
    e.preventDefault();
    const payload = {
      name,
      email,
      phone,
      address,
      contractCapacity: parseFloat(capacity)
    };

    try {
      const res = await consumerService.createConsumer(payload);
      setConsumers([...consumers, res.data]);
      setShowAddForm(false);
      resetForm();
    } catch (err) {
      console.warn('Backend unavailable, mock adding locally.');
      const dummy = {
        id: Date.now(),
        name,
        email,
        phone,
        address,
        contractCapacity: parseFloat(capacity),
        accountNumber: `ACC-${Math.floor(10000 + Math.random() * 90000)}`
      };
      setConsumers([...consumers, dummy]);
      setShowAddForm(false);
      resetForm();
    }
  };

  const resetForm = () => {
    setName('');
    setEmail('');
    setPhone('');
    setAddress('');
    setCapacity('');
  };

  const filteredConsumers = consumers.filter(c => 
    c.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
    c.accountNumber?.toLowerCase().includes(searchQuery.toLowerCase())
  );

  return (
    <div style={{ minHeight: '100vh', background: 'var(--bg-primary)' }}>
      <Header title="Consumer Registry & Load Capacity" />

      {/* Register Consumer Form Toggle */}
      <div style={{ display: 'flex', justifyContent: 'space-between', gap: '1rem', alignItems: 'center', marginBottom: '2.0rem' }}>
        
        {/* Search */}
        <div style={{ position: 'relative', width: '350px' }}>
          <Search size={16} style={{ position: 'absolute', left: '12px', top: '15px', color: 'var(--text-muted)' }} />
          <input 
            type="text" 
            className="form-control" 
            placeholder="Search by name or account no." 
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            style={{ paddingLeft: '2.5rem' }}
          />
        </div>

        <button className="btn btn-primary" onClick={() => setShowAddForm(!showAddForm)}>
          <UserPlus size={16} />
          <span>Register Consumer</span>
        </button>
      </div>

      {showAddForm && (
        <div className="glass-panel" style={{ marginBottom: '2rem', animation: 'slideIn 0.3s ease' }}>
          <h4 style={{ fontSize: '1rem', fontWeight: 700, marginBottom: '1.25rem' }}>Register New Electricity Consumer</h4>
          
          <form onSubmit={handleRegisterConsumer} style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: '1rem' }}>
            <div className="form-group" style={{ marginBottom: 0 }}>
              <label className="form-label">Full Name / Corporate Entity</label>
              <input type="text" className="form-control" placeholder="e.g. Acme Corp" value={name} onChange={(e) => setName(e.target.value)} required />
            </div>

            <div className="form-group" style={{ marginBottom: 0 }}>
              <label className="form-label">Contact Email</label>
              <input type="email" className="form-control" placeholder="contact@domain.com" value={email} onChange={(e) => setEmail(e.target.value)} required />
            </div>

            <div className="form-group" style={{ marginBottom: 0 }}>
              <label className="form-label">Phone Number</label>
              <input type="text" className="form-control" placeholder="e.g. +1-555-0150" value={phone} onChange={(e) => setPhone(e.target.value)} required />
            </div>

            <div className="form-group" style={{ marginBottom: 0 }}>
              <label className="form-label">Contract Capacity (kW)</label>
              <input type="number" step="0.1" className="form-control" placeholder="e.g. 50" value={capacity} onChange={(e) => setCapacity(e.target.value)} required />
            </div>

            <div className="form-group" style={{ gridColumn: 'span 2', marginBottom: 0 }}>
              <label className="form-label">Physical Address</label>
              <input type="text" className="form-control" placeholder="Street Address, City" value={address} onChange={(e) => setAddress(e.target.value)} required />
            </div>

            <div style={{ gridColumn: 'span 2', display: 'flex', gap: '0.75rem', justifyContent: 'flex-end', marginTop: '0.5rem' }}>
              <button type="submit" className="btn btn-primary">Save Registry</button>
              <button type="button" className="btn btn-secondary" onClick={() => setShowAddForm(false)}>Cancel</button>
            </div>
          </form>
        </div>
      )}

      {/* Consumers Grid */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(340px, 1fr))', gap: '1.5rem' }}>
        {filteredConsumers.map((consumer) => (
          <div key={consumer.id} className="glass-panel" style={{ display: 'flex', flexDirection: 'column', gap: '1.25rem' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'start' }}>
              <div>
                <h4 style={{ fontSize: '1.125rem', fontWeight: 700 }}>{consumer.name}</h4>
                <span style={{ fontSize: '0.6875rem', color: 'var(--text-muted)' }}>ACCOUNT: {consumer.accountNumber || 'PENDING'}</span>
              </div>
              <div className="status-pill status-active" style={{ padding: '0.3rem 0.6rem', fontSize: '0.6875rem', display: 'flex', gap: '0.25rem', alignItems: 'center' }}>
                <TrendingUp size={12} />
                <span>{consumer.contractCapacity} kW</span>
              </div>
            </div>

            <div style={{ display: 'flex', flexDirection: 'column', gap: '0.5rem', fontSize: '0.8125rem', color: 'var(--text-secondary)' }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                <Mail size={14} style={{ color: 'var(--accent-blue)' }} />
                <span>{consumer.email}</span>
              </div>
              <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                <Phone size={14} style={{ color: 'var(--accent-purple)' }} />
                <span>{consumer.phone}</span>
              </div>
              <div style={{ display: 'flex', alignItems: 'start', gap: '0.5rem' }}>
                <MapPin size={14} style={{ color: 'var(--accent-pink)', marginTop: '0.15rem', flexShrink: 0 }} />
                <span>{consumer.address}</span>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Consumers;
