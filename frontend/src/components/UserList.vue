<template>
    <div class="container">
      <SearchBarUsers @search="updateSearchQuery" />

        <table>
          <thead>
            <tr>
              <th class="first">Name</th>
              <th>Email</th>
              <th>Phone nr</th>
              <th class="last">Role</th>
            </tr>
        </thead>
          <tbody>
            <tr v-for="user in filteredUsers" :key="user.email" class="user-list">
              <td>
                <router-link :to="`/user/${user.email}`" class="user-item">
                <img v-if="user.profilePictureUrl" :src="user.profilePictureUrl" class="avatar sm rounded-pill me-3 flex-shrink-0" :alt="user.firstName + ' ' + user.lastName + ' Profile Picture'">
                <img v-else src="../assets/no-profile-picture.png" class="avatar sm rounded-pill me-3 flex-shrink-0" alt="No Profile Picture">
                  {{ user.firstName }} {{ user.lastName }}
                </router-link>
              </td>
              <td>{{ user.email }}</td> 
              <td>{{ user.phoneNumber }}</td> 
              <td>{{ user.role }}</td>
            </tr>
          </tbody>
        </table>
      </div>
  
    <div class="viewmore-container">
      <button class="viewmore-btn btn btn-primary" @click="loadMoreUsers" v-if="filteredUsers.length >= 10">
        Load More
      </button>
    </div>
  </template>
  
  <script>
  import { ref, computed, onMounted } from 'vue';
  import { useUserStore } from '@/stores/user';
  import SearchBarUsers from '@/components/SearchBarUsers.vue';
  
  export default {
    setup() {
      const userStore = useUserStore();
      const searchQuery = ref('');
      const displayedUsersCount = ref(10);
  
      const filteredUsers = computed(() => {
      const allUsers = userStore.searchUsers(searchQuery.value);
      return allUsers.slice(0, displayedUsersCount.value).map(user => ({
        ...user,
        role: user.role === 'ROLE_ADMIN' ? 'Admin' : 'User'
      }));
    });
  
      const updateSearchQuery = (value) => {
        searchQuery.value = value;
      };
  
      const loadMoreUsers = () => {
        displayedUsersCount.value += 10;
      };
  
      onMounted(() => {
        userStore.getAllUsers();
      });
  
      return {
        searchQuery,
        filteredUsers,
        updateSearchQuery,
        loadMoreUsers,
      };
    },
    components: { SearchBarUsers }
  };
  </script>
  
  <style scoped>

  .container {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: 150px;
  }

  table {
    margin-top: 30px;
    background-color: var(--blue);
    width: 980px;
    border-radius: 9px;
    color: var(--white);
  }

    table th {
        padding: 10px;
        font-size: 14px;
        background-color: #222E58;
    }

    table th.first {
        border-top-left-radius: 9px;
    }

    table th.last {
        border-top-right-radius: 9px;
    }

    table td {
        padding: 10px;
        font-size: 16px;
        border-bottom: 2px solid var(--dark-blue);
    }

    .user-item {
      color: var(--white);
      text-decoration: none;
    }

    .user-item:hover {
      text-decoration: underline;
    }

    .avatar.sm {
      width: 35px;
      height: 35px;
    }

    .viewmore-container {
      display: flex;
      justify-content: center;
      margin-top: 20px;
    }

    .viewmore-btn {
      padding: 5px 15px;
      border-radius: 9px;
      background-color: var(--blue);
      border-color: var(--blue);
      color: var(--white);
    }

    .viewmore-btn:hover {
      border: 1px solid var(--orange);
    }

    </style>
