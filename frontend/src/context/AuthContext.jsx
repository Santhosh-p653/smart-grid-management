import React, { createContext, useState, useEffect, useContext } from 'react';
import { authService } from '../services/api';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const currentUser = authService.getCurrentUser();
    if (currentUser) {
      setUser(currentUser);
    }
    setLoading(false);
  }, []);

  const login = async (username, password) => {
    try {
      const data = await authService.login(username, password);
      setUser(data);
      return data;
    } catch (err) {
      console.warn("Backend auth offline. Trying simulation login...", err);
      if (
        (username === 'admin' && password === 'admin123') ||
        (username === 'operator1' && password === 'operator123')
      ) {
        const mockUser = {
          username,
          fullName: username === 'admin' ? 'Chief Administrator' : 'Grid Operator',
          role: username === 'admin' ? 'ROLE_ADMIN' : 'ROLE_OPERATOR',
          token: 'simulation-token-only'
        };
        localStorage.setItem('token', mockUser.token);
        localStorage.setItem('user', JSON.stringify(mockUser));
        setUser(mockUser);
        return mockUser;
      }
      throw err;
    }
  };

  const logout = () => {
    authService.logout();
    setUser(null);
  };

  const isAdmin = () => {
    return user && (user.role === 'ROLE_ADMIN' || user.role === 'ADMIN');
  };

  return (
    <AuthContext.Provider value={{ user, login, logout, loading, isAdmin }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
