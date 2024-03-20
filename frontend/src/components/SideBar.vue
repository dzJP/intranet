<template> 
    <nav  class="sidebar">
      <img src="../assets/logo.png" alt="logo" class="logo" />
      <ul>
        <li v-if="userRole === 'ROLE_USER'" class="nav-item">
        <router-link to="/">Dashboard</router-link> 
        </li>
        <li v-if="userRole === 'ROLE_USER'" class="nav-item" >
        <router-link to="/profile">Profile</router-link> 
        </li>
        <li v-if="userRole === 'ROLE_USER'" class="nav-item">
        <router-link to="/colleagues">Colleagues</router-link> 
        </li>
        <li v-if="userRole === 'ROLE_USER'" class="nav-item">
        <router-link to="/news">Latest news</router-link>
        </li>
      </ul>

      <div class="logout" v-if="userRole">
          <a href="#" @click="logout">Logout</a>
        </div>
    </nav>
    
  </template>
  
  <script>
  import { useAuthStore } from '@/stores/auth';
  
  export default {
    name: 'side-bar',
    computed: {
      userRole() {
        const auth = useAuthStore();
        return auth.role;
      },
    },
    methods: {
      logout() {
        const auth = useAuthStore();
        auth.logout();
      },
    },
  };

  </script>
  
<style scoped>
.sidebar {
  position: fixed;
  top: 0;
  left: 0;
  height: 100%;
  width: 260px;
  border-right: 1px solid hsla(0, 0%, 100%, 0.5);
  background-color: var(--dark-blue);
  z-index: 1;
}

  .logo {
  width: 150px;
  height: 50px;
  margin-top: 60px;
  margin-left: 40px;
}

.sidebar ul {
  display: flex;
  flex-direction: column;
  justify-content: center;
  margin-top: 80px;
  width: 100%;
  list-style: none;
  padding: 0;
}

 .sidebar ul li a {
   display: block;
   padding: 0 40px;
   margin-bottom: 15px;
   font-size: 22px;
   font-weight: 600;
   text-transform: uppercase;
   text-decoration: none;
   color: var(--white);
   font-family: 'Oxanium', sans-serif;
  }

.sidebar ul li a:hover {
  color: var(--orange);
}

.sidebar li a.router-link-exact-active {
  border-left: 10px solid var(--orange); 
}

.logout {
  position: fixed;
  bottom: 50px; 
  left: 60px; 
  list-style: none;
}

.logout a {
  color: var(--white);
  text-decoration: none;
  padding: 10px 30px;
  font-size: 16px; 
  font-weight: 600;
  border: 1px solid var(--orange); 
  border-radius: 5px; 
  transition: background-color 0.3s ease, color 0.3s ease; 
  text-transform: uppercase;
  letter-spacing: 1px;
}

.logout a:hover {
  background-color: var(--orange); 
  color: var(--white); 
}

</style>