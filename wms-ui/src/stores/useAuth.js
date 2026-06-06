import { create } from 'zustand';
import { getCurrentUser } from '../api/auth';

const useAuth = create((set, get) => ({
  user: JSON.parse(localStorage.getItem('wms_me') || 'null'),
  permissions: JSON.parse(localStorage.getItem('wms_perms') || '[]'),
  initialized: false,

  setUser: (user) => {
    localStorage.setItem('wms_me', JSON.stringify(user));
    set({ user });
  },

  setPermissions: (permissions) => {
    localStorage.setItem('wms_perms', JSON.stringify(permissions));
    set({ permissions });
  },

  initAuth: async () => {
    const { user } = get();
    if (user) {
      set({ initialized: true });
      return true;
    }
    try {
      const data = await getCurrentUser();
      if (data && data.user) {
        get().setUser(data.user);
        get().setPermissions(data.perms || []);
        set({ initialized: true });
        return true;
      }
    } catch {
      get().clearAuth();
    }
    set({ initialized: true });
    return false;
  },

  clearAuth: () => {
    localStorage.removeItem('wms_me');
    localStorage.removeItem('wms_perms');
    set({ user: null, permissions: [], initialized: true });
  },

  logout: async () => {
    try {
      const { logout } = await import('../api/auth');
      await logout();
    } catch {
      // ignore
    }
    get().clearAuth();
  },

  hasPermission: (perm) => {
    const { permissions } = get();
    return permissions.includes('*') || permissions.includes(perm);
  },

  hasAnyPermission: (perms) => {
    const { permissions } = get();
    if (permissions.includes('*')) return true;
    return perms.some(p => permissions.includes(p));
  },
}));

export default useAuth;
