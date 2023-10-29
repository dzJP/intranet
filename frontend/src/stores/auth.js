import { defineStore } from 'pinia';
import router from '@/router';
import axios from 'axios';

export const useAuthStore = defineStore({
  id: 'auth',
  state: () => {
    return {
      user: localStorage.getItem('user') || '', 
      token: localStorage.getItem('token') || '',
      returnUrl: '/'
    }
  },
  actions: {
    async login(email, password) {
      try {
        const response = await axios.post('http://localhost:8080/api/v1/signin', {
          email,
          password
        }, {
          headers: {
            'Content-Type': 'application/json'
          }
        });

        if (response.status === 200) {
          const token = response.data.token;
          localStorage.setItem('user', email);
          localStorage.setItem('token', token); 
          this.user = email;
          this.token = token;
          router.push(this.returnUrl || '/');
        }
      } catch (error) {
        console.error('Login failed:', error);
      }
    },
    logout() {
      this.user = '';
      this.token = '';
      localStorage.removeItem('user');
      localStorage.removeItem('token');
      router.push('/login');
    }
  }

});

